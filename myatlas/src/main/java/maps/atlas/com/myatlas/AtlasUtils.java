package maps.atlas.com.myatlas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class AtlasUtils {

    public static final String PLACES_ACCOUNTING = "accounting";
    public static final String PLACES_AIRPORT = "airport";
    public static final String PLACES_AMUSEMENT_PARK = "amusement_park";
    public static final String PLACES_AQUARIUM = "aquarium";
    public static final String PLACES_ART_GALLERY = "art_gallery";
    public static final String PLACES_ATM = "atm";
    public static final String PLACES_BAKERY = "bakery";
    public static final String PLACES_BANK = "bank";
    public static final String PLACES_BAR = "bar";
    public static final String PLACES_BEAUTY_SALON = "beauty_salon";
    public static final String PLACES_BICYCLE_STORE = "bicycle_store";
    public static final String PLACES_BOOK_STORE = "book_store";
    public static final String PLACES_BOWLING_ALLEY = "bowling_alley";
    public static final String PLACES_BUS_STATION = "bus_station";
    public static final String PLACES_CAFE = "cafe";
    public static final String PLACES_CAMPGROUND = "campground";
    public static final String PLACES_CAR_DEALER = "car_dealer";
    public static final String PLACES_CAR_RENTAL = "car_rental";
    public static final String PLACES_CAR_REPAIR = "car_repair";
    public static final String PLACES_CAR_WASH = "car_wash";
    public static final String PLACES_CASINO = "casino";
    public static final String PLACES_CEMETRY = "cemetry";
    public static final String PLACES_CHURCH = "church";
    public static final String PLACES_CITY_HALL = "city_hall";
    public static final String PLACES_CLOTHING_STORE = "clothing_store";
    public static final String PLACES_CONVENIENCE_STORE = "convenience_store";
    public static final String PLACES_COURTHOUSE = "courthouse";
    public static final String PLACES_DENTIST = "dentist";
    public static final String PLACES_DEPARTMENT_STORE = "department_store";
    public static final String PLACES_DOCTOR = "doctor";
    public static final String PLACES_ELECTRICIAN = "electrician";
    public static final String PLACES_ELECTRONICS_STORE = "electronics_store";
    public static final String PLACES_EMBASSY = "embassy";
    public static final String PLACES_FIRE_STATION = "fire_station";
    public static final String PLACES_FLORIST = "florist";
    public static final String PLACES_FUNERAL_HOME = "funeral_home";
    public static final String PLACES_FURNITURE_STORE = "furniture_store";
    public static final String PLACES_GAS_STATION = "gas_station";
    public static final String PLACES_GYM = "gym";
    public static final String PLACES_HAIR_CARE = "hair_care";
    public static final String PLACES_HARDWARE_STORE = "hardware_store";
    public static final String PLACES_HINDU_TEMPLE = "hindu_temple";
    public static final String PLACES_HOME_GOODS_STORE = "home_goods_store";
    public static final String PLACES_HOSPITAL = "hospital";
    public static final String PLACES_INSURANCE_AGENCY = "insurance_gency";
    public static final String PLACES_JEWELRY_STORE = "jewelry_store";
    public static final String PLACES_LAUNDRY = "laundry";
    public static final String PLACES_LAWYER = "lawyer";
    public static final String PLACES_LIBRARY = "library";
    public static final String PLACES_LIQUOR_STORE = "liquor_store";
    public static final String PLACES_LOCAL_GOVERNMENT_OFFICE = "local_government_office";
    public static final String PLACES_LOCKSMITH = "locksmith";
    public static final String PLACES_LODGING = "lodging";
    public static final String PLACES_MEAL_DELIVERY = "meal_delivery";
    public static final String PLACES_MEAL_TAKEAWAY = "meal_takeaway";
    public static final String PLACES_MOSQUE = "mosque";
    public static final String PLACES_MOVIE_RENTAL = "movie_rental";
    public static final String PLACES_MOVIE_THEATER = "movie_theater";
    public static final String PLACES_MOVIE_COMPANY = "movie_company";
    public static final String PLACES_MUSEUM = "museum";
    public static final String PLACES_NIGHT_CLUB = "night_club";
    public static final String PLACES_PAINTER = "painter";
    public static final String PLACES_PARK = "park";
    public static final String PLACES_PARKING = "parking";
    public static final String PLACES_PET_STORE = "pet_store";
    public static final String PLACES_PHARMACY = "pharmacy";
    public static final String PLACES_PHYSIOTHERAPIST = "physiotherapist";
    public static final String PLACES_PLUMBER = "plumber";
    public static final String PLACES_POLICE = "police";
    public static final String PLACES_POST_OFFICE = "post_office";
    public static final String PLACES_REAL_ESTATE_AGENCY = "real_estate_agency";
    public static final String PLACES_RESTAURANT = "restaurant";
    public static final String PLACES_ROOFING_CONTRACTOR = "roofing_contractor";
    public static final String PLACES_RV_PARK = "rv_park";
    public static final String PLACES_SCHOOL = "school";
    public static final String PLACES_SHOE_STORE = "shoe_store";
    public static final String PLACES_SHOPPING_MALL = "shopping_mall";
    public static final String PLACES_SPA = "spa";
    public static final String PLACES_STADIUM = "stadium";
    public static final String PLACES_STORAGE = "storage";
    public static final String PLACES_STORE = "store";
    public static final String PLACES_SUBWAY_STATION = "subway_station";
    public static final String PLACES_SUPERMARKET = "supermarket";
    public static final String PLACES_SYNAGOGUE = "synagogue";
    public static final String PLACES_TAXI_STAND = "taxi_stand";
    public static final String PLACES_TRAIN_STATION = "train_station";
    public static final String PLACES_TRANSIT_STATION = "transit_station";
    public static final String PLACES_TRAVEL_AGENCY = "travel_agency";
    public static final String PLACES_VETERINARY_CARE = "veterinary_care";
    public static final String PLACES_ZOO = "zoo";




    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    public static final int MAP_TYPE_HYBRID = 4;

    public static final int DIRECTIONS_MODE_DRIVING = 0;
    public static final int DIRECTIONS_MODE_WALKING = 1;
    public static final int DIRECTIONS_MODE_TRANSIT = 2;

    public static boolean HIDE_INFO_WINDOW = false;
    public static boolean ENABLE_POLYLINE_CLICK = true;


    public static boolean isLocationPermissionGranted(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}
