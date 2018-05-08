package maps.atlas.com.atlas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import maps.atlas.com.myatlas.AtlasActivity;
import maps.atlas.com.myatlas.AtlasFragment;
import maps.atlas.com.myatlas.AtlasUtils;

public class MainActivity extends AtlasActivity {

    AtlasFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = new AtlasFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame1, mapFragment);
        transaction.commit();
    }

    @Override
    public void onMapCreated(GoogleMap googleMap) {
        //Set Map type
        mapFragment.setMapType(AtlasUtils.MAP_TYPE_NORMAL);
    }

    public void getUserLocation(View view) {
        if(AtlasUtils.isLocationPermissionGranted(this)) {
            LatLng loc = mapFragment.getCurrentLocation();
            Toast.makeText(this, "" + loc.longitude + loc.latitude, Toast.LENGTH_SHORT).show();
        }
    }

    public void setCurrentLocationMarker(View view) {
        if(AtlasUtils.isLocationPermissionGranted(this)) {
            mapFragment.removeCustomInfoWindow();
            mapFragment.setCurrentLocationMarker(R.mipmap.ic_launcher, "Current Location", "This is my Location");
            mapFragment.zoomInToCurrentLocation(0, 10);
        }
    }

    public void setInfoWindow(View view) {
        if(AtlasUtils.isLocationPermissionGranted(this)) {
            mapFragment.enableCustomInfoWindow();
            mapFragment.setCurrentLocationMarker(R.mipmap.ic_launcher, "Current Location", "This is my Location");
            mapFragment.zoomInToCurrentLocation(0, 10);
        }
    }

    @Override
    public View getInfoWindowView(Marker marker) {
        View popup=getLayoutInflater().inflate(R.layout.info_window, null);
        return popup;
    }

    @Override
    public void onMarkerClicked(Marker marker) {
        Toast.makeText(this, "Clicked Marker", Toast.LENGTH_SHORT).show();
    }

    public void getDirections(View view) {
        if(AtlasUtils.isLocationPermissionGranted(this)) {
            mapFragment.removeAllPolylines();
            mapFragment.drawPathBetweenMarkers(mapFragment.getCurrentLocation(), new LatLng(11.926290, 79.834420), 10, Color.RED,
                    AtlasUtils.DIRECTIONS_MODE_WALKING);
            mapFragment.zoomInToCurrentLocation(0, 16);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(AtlasUtils.isLocationPermissionGranted(this)) {
            mapFragment.removeAllPolylines();
            mapFragment.drawPathBetweenMarkers(mapFragment.getCurrentLocation(), latLng, 10, Color.RED,
                    AtlasUtils.DIRECTIONS_MODE_WALKING);
            mapFragment.zoomInToCurrentLocation(0, 16);
        }
    }

    public void getNearbyPlaces(View view) {
        if(AtlasUtils.isLocationPermissionGranted(this)) {
            mapFragment.getNearbyPlaces(mapFragment.getCurrentLocation(), "5", AtlasUtils.PLACES_BAKERY,
                    "Your Places API Key", null);
            mapFragment.zoomInToCurrentLocation(0, 16);
        }
    }
}
