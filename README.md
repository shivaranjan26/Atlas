Welcome to Atlas
This component will help you generate the Google Map v2 and also lets you use all the features with simple getter and setter methods.

Adding library to project :

Add it in your root build.gradle at the end of repositories:

allprojects {

    repositories {

        google()

        jcenter()

        maven { url 'https://jitpack.io' }

    }

}
  
Add the dependency :

dependencies {

    implementation 'com.github.shivaranjan26:Atlas:10.1'
    
}  


Usage :

Step 1 : Your activity  must extend MyAtlasActivity and override onMapCreated as shown below,

public class MainActivity extends AtlasActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onMapCreated(GoogleMap googleMap) {

    }
}

 


Step 2 : To use all of Atlas features, you must use the Atlas fragment as shown below,

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
    
}

Atlas is now ready to use.

Features :

1 ) Set Map Type :

Code :
atlasFragment.setMapType(AtlasUtils.MAP_TYPE_NORMAL)

Attributes : AtlasUtils class

MAP_TYPE_NONE

MAP_TYPE_NORMAL

MAP_TYPE_SATELLITE

MAP_TYPE_TERRAIN

MAP_TYPE_HYBRID

2 ) Enable Zoom Controls in Mapview

Code :
atlasFragment.shouldEnableZoomControls(true)

3 ) Zoom in / Out functionality : If you have custom buttons for Zoom, use this

Code :
atlasFragment.zoomIn(10, 1500) - 10 denotes the zoom in level and 1500 denotes the animation time to zoom in

atlasFragment.zoomOut(10, 1500) - 10 denotes the zoom out level and 1500 denotes the animation time to zoom out

4 ) Get User Current Location : returns LatLng object

Code :
atlasFragment.getCurrentLocation()

5 ) Zoom in to User Current Location :

Code :
atlasFragment.zoomInToCurrentLocation(10, 0)

10 denotes the amount to zoom in by. 0 denotes Fixed Zoom. If you want to keep the zoom level fixed at a certain amount, then provide any value > 0 for fixed zoom.

6 ) Zoom to a specific Location : Use this to zoom in to a specific Location

Code :
atlasFragment.zoomToLocation(float zoom, float fixedZoom, Location loc)

atlasFragment.zoomToLocation(float zoom, float fixedZoom, LatLng latlng)

7 ) Set Marker on Current Location

Code :
atlasFragment.setCurrentLocationMarker(Drawable image, String title, String msg)

atlasFragment.setCurrentLocationMarker(int image, String title, String msg)

8 ) Set Marker on Specific location

Code :
atlasFragment.setLocationMarker(LatLng latLong, Drawable image, String title, String msg)

atlasFragment.setLocationMarker(LatLng latLong, int image, String title, String msg)

9 ) Get the list of all the Markers placed on Map : Returns List<Marker>

Code :
atlasFragment.getMarkers()

10 ) Remove a specific marker

Code :
atlasFragment.removeMarker(Marker marker)

11 ) Remove all Markers in Map

Code :
atlasFragment.removeAllMarkers()

12 ) To Check if GPS and Network is enabled : Returns boolean

Code :
atlasFragment.isGpsAndNetworkEnabled()

13 ) Replace default marker windows with custom info window

Code :
atlasFragment.enableCustomInfoWindow()

Note :
Override getInfoWindow as shown below,

@Override

public View getInfoWindowView(Marker marker) {

return view;    }

14 ) Use default info windows for marker :

Code :
atlasFragment.removeCustomInfoWindow()

15 ) Draw Polyline between two locations

Code :
atlasFragment.drawPathBetweenMarkers(LatLng fromPath, LatLng toPath, int lineWidth, int lineColor, int mode)

Parameters : Mode : AtlasUtils class

DIRECTIONS_MODE_DRIVING
DIRECTIONS_MODE_WALKING
DIRECTIONS_MODE_TRANSIT

Callback Methods :

public void onPreLoadingDirections() {}

public void onLoadingDirections(Integer[] values) {}

public void onLoadingDirectionsError(String s) {}

public void onLoadingDirectionsComplete() {}

public void datafromDirection(String duration, String distance, String startAddress, String endAddress) {}

public void onRecievingNoDirections() {}

16 ) Get Nearby Places :

Code : 

atlasFragment.getNearbyPlaces(LatLng latLng, String kilometerRadius, String type, String placesKey, String nextPageToken)

Pass nextpageToken as null always. Generate your Places API key and pass it as palceskey above. Type denotes the type of place you want to search. You can view the below attributes to see the list of available places. If you wish to search for all places, pass type as null.

Callback Methods :

public void onPreLoadingPlaces() {}

public void onNearbyPlacesFetched(List<HashMap<String, String>> placesList) {}

Attributes : Type : AtlasUtils class

PLACES_ACCOUNTING

PLACES_AIRPORT

PLACES_AMUSEMENT_PARK

PLACES_AQUARIUM

PLACES_ART_GALLERY

PLACES_ATM

PLACES_BAKERY

PLACES_BANK

PLACES_BAR

PLACES_BEAUTY_SALON

PLACES_BICYCLE_STORE

PLACES_BOOK_STORE

PLACES_BOWLING_ALLEY

PLACES_BUS_STATION

PLACES_CAFE

PLACES_CAMPGROUND

PLACES_CAR_DEALER

PLACES_CAR_RENTAL

PLACES_CAR_REPAIR

PLACES_CAR_WASH

PLACES_CASINO

PLACES_CEMETRY

PLACES_CHURCH

PLACES_CITY_HALL

PLACES_CLOTHING_STORE

PLACES_CONVENIENCE_STORE

PLACES_COURTHOUSE

PLACES_DENTIST

PLACES_DEPARTMENT_STORE

PLACES_DOCTOR

PLACES_ELECTRICIAN

PLACES_ELECTRONICS_STORE

PLACES_EMBASSY

PLACES_FIRE_STATION

PLACES_FLORIST

PLACES_FUNERAL_HOME

PLACES_FURNITURE_STORE

PLACES_GAS_STATION

PLACES_GYM

PLACES_HAIR_CARE

PLACES_HARDWARE_STORE

PLACES_HINDU_TEMPLE

PLACES_HOME_GOODS_STORE

PLACES_HOSPITAL

PLACES_INSURANCE_AGENCY

PLACES_JEWELRY_STORE

PLACES_LAUNDRY

PLACES_LAWYER

PLACES_LIBRARY

PLACES_LIQUOR_STORE

PLACES_LOCAL_GOVERNMENT_OFFICE

PLACES_LOCKSMITH

PLACES_LODGING

PLACES_MEAL_DELIVERY

PLACES_MEAL_TAKEAWAY

PLACES_MOSQUE

PLACES_MOVIE_RENTAL

PLACES_MOVIE_THEATER

PLACES_MOVIE_COMPANY

PLACES_MUSEUM

PLACES_NIGHT_CLUB

PLACES_PAINTER

PLACES_PARK

PLACES_PARKING

PLACES_PET_STORE

PLACES_PHARMACY

PLACES_PHYSIOTHERAPIST

PLACES_PLUMBER

PLACES_POLICE

PLACES_POST_OFFICE

PLACES_REAL_ESTATE_AGENCY

PLACES_RESTAURANT

PLACES_ROOFING_CONTRACTOR

PLACES_RV_PARK

PLACES_SCHOOL

PLACES_SHOE_STORE

PLACES_SHOPPING_MALL

PLACES_SPA

PLACES_STADIUM

PLACES_STORAGE

PLACES_STORE

PLACES_SUBWAY_STATION

PLACES_SUPERMARKET

PLACES_SYNAGOGUE

PLACES_TAXI_STAND

PLACES_TRAIN_STATION

PLACES_TRANSIT_STATION

PLACES_TRAVEL_AGENCY

PLACES_VETERINARY_CARE

PLACES_ZOO

17 ) Other Callback Methods :

public void onPolylineClicked(Polyline polyline) {}

public void onMapClick(LatLng latLng) {}

public void onInfoWindowClick(Marker marker) {}

public void onInfoWindowLongClick(Marker marker) {}

public void onInfoWindowClose(Marker marker) {}

public void onMapLongClick(LatLng latLng) {}

public void onMarkerClicked(Marker marker) {};

public void onErrorCaught(String s) {}

public void onGpsDisabled() {}

public void onNetworkDisabled() {}

18 ) Enabling Polyline Click : Use this if you want to display something to the user on click the direction line

Code :
AtlasUtils.ENABLE_POLYLINE_CLICK = true

19 ) Hiding Marker Infowindow : Use this if you want to bring your own layout on marker click

Code :
AtlasUtils.HIDE_INFO_WINDOW = true
