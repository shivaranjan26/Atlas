package maps.atlas.com.myatlas;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.HashMap;
import java.util.List;

public abstract class AtlasActivity extends AppCompatActivity {

    public void onMarkerClicked(Marker marker) {};

    public abstract void onMapCreated(GoogleMap googleMap);

    public void onErrorCaught(String s) {}

    public void onGpsDisabled() {}

    public void onNetworkDisabled() {}

    public View getInfoWindowView(Marker marker) {
        return null;
    }

    public void onPreLoadingDirections() {}

    public void onLoadingDirections(Integer[] values) {}

    public void onLoadingDirectionsError(String s) {}

    public void onLoadingDirectionsComplete() {}

    public void datafromDirection(String duration, String distance, String startAddress, String endAddress) {}

    public void onRecievingNoDirections() {}

    public void onPolylineClicked(Polyline polyline) {}

    public void onMapClick(LatLng latLng) {}

    public void onInfoWindowClick(Marker marker) {}

    public void onInfoWindowLongClick(Marker marker) {}

    public void onInfoWindowClose(Marker marker) {}

    public void onMapLongClick(LatLng latLng) {}

    public void onPreLoadingPlaces() {}

    public void onNearbyPlacesFetched(List<HashMap<String, String>> placesList) {}
}
