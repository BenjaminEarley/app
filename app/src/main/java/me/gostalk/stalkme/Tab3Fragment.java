package me.gostalk.stalkme;

import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Tab3Fragment extends Fragment  {

    private GoogleMap map;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab3_view, container, false);
    }

    private GoogleMap getGoogleMap() {
        if (map == null && getActivity() != null && getActivity().getSupportFragmentManager()!= null) {
            SupportMapFragment smf = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            if (smf != null) {
                map = smf.getMap();
            }
        }
        return map;
    }
}