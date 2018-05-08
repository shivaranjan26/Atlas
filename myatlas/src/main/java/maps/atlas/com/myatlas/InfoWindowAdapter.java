package maps.atlas.com.myatlas;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater;
    AtlasActivity activity;

    public InfoWindowAdapter(LayoutInflater inf, AtlasActivity callbacks) {
        inflater = inf;
        activity = callbacks;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = activity.getInfoWindowView(marker);

        if (popup == null) {
            return null;
        } else {
            return popup;
        }
    }
}
