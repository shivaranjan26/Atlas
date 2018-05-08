package maps.atlas.com.myatlas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AtlasFragment extends Fragment {

    private String LOCATION_PROVIDER = "";

    private AtlasActivity callbacks;

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private List<Marker> markerList = new ArrayList<Marker>();
    private List<Polyline> polylineList = new ArrayList<Polyline>();

    private int mLineWidth;
    private int mLineColor;
    private int mMode = 0;
    private boolean isFirstSearch = true;
    private List<HashMap<String, String>> placesList;
    private LatLng nLatLng;
    private String nRadius, nType, nServerKey;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mapLayout = null;
        try {
            //inflate the mapview xml
            mapLayout = inflater.inflate(R.layout.atlas, null);

            //Initialize the abstract class
            callbacks = (AtlasActivity) getActivity();

            //Create the Mapview
            mapView = (MapView) mapLayout.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mGoogleMap) {
                    googleMap = mGoogleMap;
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            //Allow user to customise the marker click action
                            callbacks.onMarkerClicked(marker);
                            return AtlasUtils.HIDE_MARKER;
                        }
                    });

                    googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                        @Override
                        public void onPolylineClick(Polyline polyline) {
                            callbacks.onPolylineClicked(polyline);
                        }
                    });

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            callbacks.onMapClick(latLng);
                        }
                    });

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            callbacks.onInfoWindowClick(marker);
                        }
                    });

                    googleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                        @Override
                        public void onInfoWindowLongClick(Marker marker) {
                            callbacks.onInfoWindowLongClick(marker);
                        }
                    });

                    googleMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
                        @Override
                        public void onInfoWindowClose(Marker marker) {
                            callbacks.onInfoWindowClose(marker);
                        }
                    });

                    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            callbacks.onMapLongClick(latLng);
                        }
                    });

                    locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };

                    //Return callback to calling activity once map is loaded
                    callbacks.onMapCreated(googleMap);
                }
            });


        } catch (Exception e) {
            //Catch all exceptions and let user view it
            callbacks.onErrorCaught(e.toString());
        }
        return mapLayout;
    }



    public void setMapType(int mapType){
        if (googleMap != null) {
            googleMap.setMapType(mapType);
        }
    }

    public void shouldEnableZoomControls(boolean shouldEnable) {
        if (googleMap != null) {
            googleMap.getUiSettings().setZoomControlsEnabled(shouldEnable);
        }
    }

    public void zoomIn(float i, int time) {
        if (googleMap != null) {
            float zoomLevel = googleMap.getCameraPosition().zoom;
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel + i), time, null);
        }
    }

    public void zoomOut(float i, int time) {
        if (googleMap != null) {
            float zoomLevel = googleMap.getCameraPosition().zoom;
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel - i), time, null);
        }
    }

    public LatLng getCurrentLocation(){
        if (isGpsAndNetworkEnabled()) {
            Location location = getLastKnownLocation();
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
            return latLong;
        } else {
            return null;
        }
    }

    public void zoomInToCurrentLocation(float zoom, float fixedZoom) {
        float zoomLevel = googleMap.getCameraPosition().zoom;

        if (isGpsAndNetworkEnabled()) {
            Location location = getLastKnownLocation();
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation;
            if (fixedZoom > 0) {
                yourLocation = CameraUpdateFactory.newLatLngZoom(latLong, fixedZoom);
            } else {
                yourLocation = CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel + zoom);
            }

            googleMap.animateCamera(yourLocation);
        }
    }

    public void zoomToLocation(float zoom, float fixedZoom, Location loc) {
        float zoomLevel = googleMap.getCameraPosition().zoom;

        if (isGpsAndNetworkEnabled()) {
            Location location = loc;
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation;
            if (fixedZoom > 0) {
                yourLocation = CameraUpdateFactory.newLatLngZoom(latLong, fixedZoom);
            } else {
                yourLocation = CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel + zoom);
            }

            googleMap.animateCamera(yourLocation);
        }
    }

    public void zoomToLocation(float zoom, float fixedZoom, LatLng latlng) {
        float zoomLevel = googleMap.getCameraPosition().zoom;

        if (isGpsAndNetworkEnabled()) {
            LatLng latLong = latlng;
            CameraUpdate yourLocation;
            if (fixedZoom > 0) {
                yourLocation = CameraUpdateFactory.newLatLngZoom(latLong, fixedZoom);
            } else {
                yourLocation = CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel + zoom);
            }

            googleMap.animateCamera(yourLocation);
        }
    }

    public void setCurrentLocationMarker(Drawable image, String title, String msg) {
        if (isGpsAndNetworkEnabled()) {
            Location location = getLastKnownLocation();
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());

            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLong).title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(image))).snippet(msg));

            addMarkerToMarkerList(marker);
        }
    }

    public void setCurrentLocationMarker(int image, String title, String msg) {
        Drawable mImage = getResources().getDrawable(image);
        if (isGpsAndNetworkEnabled()) {
            Location location = getLastKnownLocation();
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());

            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLong).title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(mImage))).snippet(msg));

            addMarkerToMarkerList(marker);
        }
    }

    public void setLocationMarker(LatLng latLong, Drawable image, String title, String msg) {
        if (isGpsAndNetworkEnabled()) {
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLong).title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(image))).snippet(msg));
            addMarkerToMarkerList(marker);
        }
    }

    public void setLocationMarker(LatLng latLong, int image, String title, String msg) {
        if (isGpsAndNetworkEnabled()) {
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLong).title(title)
                    .icon(BitmapDescriptorFactory.fromResource(image)).snippet(msg));
            addMarkerToMarkerList(marker);
        }
    }

    public List<Marker> getMarkers(){
        return markerList;
    }

    public void removeMarker(Marker marker){
        marker.remove();
    }

    public void removeAllMarkers(){
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).remove();
        }
    }

    public void removeAllPolylines(){
        for (int i = 0; i < polylineList.size(); i++) {
            polylineList.get(i).remove();
        }
    }

    private void addMarkerToMarkerList(Marker marker){
        if(markerList.size() > 0) {
            for (int i = 0; i < markerList.size(); i++) {
                if(!markerList.get(i).getTitle().equalsIgnoreCase(marker.getTitle())) {
                    markerList.add(marker);
                }
            }
        } else {
            markerList.add(marker);
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, 0, 0, locationListener);
        return locationManager.getLastKnownLocation(LOCATION_PROVIDER);
    }

    public boolean isGpsAndNetworkEnabled() {
        // getting GPS status
        boolean checkGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean checkNetwork = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!checkGPS) {
            callbacks.onGpsDisabled();
            return false;
        }
        else if(!checkNetwork) {
            callbacks.onNetworkDisabled();
            return false;
        }
        else {
            if(checkNetwork) {
                LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
            } else {
                LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
            }
            return true;
        }
    }

    public void enableCustomInfoWindow() {
        googleMap.setInfoWindowAdapter(new InfoWindowAdapter(getActivity().getLayoutInflater(), callbacks));
    }

    public void removeCustomInfoWindow() {
        googleMap.setInfoWindowAdapter(null);
    }

    public void drawPathBetweenMarkers(LatLng fromPath, LatLng toPath, int lineWidth, int lineColor, int mode) {
        mLineWidth = lineWidth;
        mLineColor = lineColor;
        mMode = mode;

        // Getting URL to the Google Directions API
        String url = getUrl(fromPath, toPath);
        MapCommunicator communicator = new MapCommunicator();

        // Start downloading json data from Google Directions API
        communicator.execute(url);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(fromPath));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }

    public void getNearbyPlaces(LatLng latLng, String kilometerRadius, String type, String placesKey, String nextPageToken) {
        if (nextPageToken == null) {
            nextPageToken = "";
        }

        int mts = Integer.valueOf(kilometerRadius);
        mts = mts * 1000;

        nLatLng = latLng;
        nRadius = String.valueOf(mts);
        nType = type;
        nServerKey = placesKey;

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latLng.latitude + "," + latLng.longitude);
        googlePlacesUrl.append("&radius=" + nRadius);
        if(type == null || type.trim().equalsIgnoreCase("")) {
            type = "all";
        }
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + placesKey);

        if(!(nextPageToken.trim().equalsIgnoreCase(""))){
            googlePlacesUrl.append("&pagetoken=" + nextPageToken);
        }

        PlacesTask googlePlacesReadTask = new PlacesTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }














































    private class MapCommunicator extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbacks.onPreLoadingDirections();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            callbacks.onLoadingDirections(values);
        }

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            JSONObject data = null;

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0], null);
            } catch (Exception e) {
                callbacks.onLoadingDirectionsError(e.toString());
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

        private JSONObject downloadUrl(String url, List params) {
            URL _url;
            HttpURLConnection urlConnection;
            String output;
            InputStream is;
            JSONObject json = null;

            try {
                _url = new URL(url);
                urlConnection = (HttpURLConnection) _url.openConnection();
            }
            catch (Exception e) {
                return null;
            }

            try {
                is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder(is.available());
                String line;
                while ((line = reader.readLine()) != null) {
                    total.append(line).append('\n');
                }
                output = total.toString();
            }
            catch (Exception e) {
                return null;
            }
            finally{
                urlConnection.disconnect();
            }

            try {
                json = new JSONObject(output);
            }
            catch (JSONException e) {
            }
            return json;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);

                JSONArray routesArray = jObject.getJSONArray("routes");
                JSONObject routeObj = (JSONObject) routesArray.get(0);
                JSONArray legsArray = routeObj.getJSONArray("legs");
                JSONObject legsObj = (JSONObject) legsArray.get(0);

                JSONObject durationObject = legsObj.getJSONObject("duration");
                String duration = durationObject.getString("text");

                JSONObject distanceObject = legsObj.getJSONObject("distance");
                String distance = distanceObject.getString("text");


                String startAddress = legsObj.getString("start_address");
                String endAddress = legsObj.getString("end_address");

                callbacks.datafromDirection(duration, distance, startAddress, endAddress);

                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
            }

            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(mLineWidth);

                // Changing the color polyline according to the mode
                lineOptions.color(mLineColor);
            }

            if(result.size()<1){
                callbacks.onRecievingNoDirections();
                callbacks.onLoadingDirectionsComplete();
                return;
            }

            // Drawing polyline in the Google Map for the i-th route
            Polyline polyline = googleMap.addPolyline(lineOptions);
            polylineList.add(polyline);
            polyline.setClickable(AtlasUtils.ENABLE_POLYLINE_CLICK);
            callbacks.onLoadingDirectionsComplete();
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Travelling Mode
        String mode = "";
        if(mMode == 0) {
            mode = "mode=driving";
        } else if( mMode == 1) {
            mode = "mode=walking";
        } else if( mMode == 2) {
            mode = "mode=transit";
        } else {
            mode = "mode=driving";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }















































    //This is called to Process the Places call and return all nearby places fom the given location.
    public class PlacesTask extends AsyncTask<Object, Integer, String> {

        String googlePlacesData = null;
        GoogleMap googleMap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbacks.onPreLoadingPlaces();
        }

        @Override
        protected String doInBackground(Object... inputObj) {
            try {
                googleMap = (GoogleMap) inputObj[0];
                String googlePlacesUrl = (String) inputObj[1];
                Http http = new Http();
                googlePlacesData = http.read(googlePlacesUrl);
            } catch (Exception e) {

            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
            Object[] toPass = new Object[2];
            toPass[0] = googleMap;
            toPass[1] = result;
            placesDisplayTask.execute(toPass);
        }
    }

    public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

        JSONObject googlePlacesJson;
        GoogleMap googleMap;
        String next_page_token = "";

        @Override
        protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

            List<HashMap<String, String>> googlePlacesList = null;
            Places placeJsonParser = new Places();

            try {
                googleMap = (GoogleMap) inputObj[0];
                googlePlacesJson = new JSONObject((String) inputObj[1]);
                googlePlacesList = placeJsonParser.parse(googlePlacesJson);
                next_page_token = googlePlacesJson.getString("next_page_token");
            } catch (Exception e) {
            }
            return googlePlacesList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            googleMap.clear();
            if(!next_page_token.equalsIgnoreCase("")) {
                if(!isFirstSearch) {
                    for (int i = 0; i < list.size(); i++) {
                        placesList.add(list.get(i));
                    }
                } else {
                    placesList = list;
                    isFirstSearch = false;
                }
                getNearbyPlaces(nLatLng, nRadius, nType, nServerKey, next_page_token);
            } else {
                if(!isFirstSearch) {
                    for (int i = 0; i < list.size(); i++) {
                        placesList.add(list.get(i));
                    }
                } else {
                    placesList = list;
                }
                callbacks.onNearbyPlacesFetched(placesList);
            }
        }
    }
}
