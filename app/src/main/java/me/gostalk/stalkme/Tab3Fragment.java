package me.gostalk.stalkme;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tab3Fragment extends Fragment {

    private GoogleMap map;
    private MapView mapView;
    private static View view;
    private List<Marker> markers;
    //TODO set markers

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.tab3_view, container, false);
            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);

            // Gets to GoogleMap from the MapView and does initialization stuff
            map = mapView.getMap();
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(true);

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            try {
                MapsInitializer.initialize(this.getActivity());
            } catch (Exception e) {
                e.printStackTrace();

            }
            //CenterMap();
            AddMarkers();
            CenterMapOnMarkers();
        } catch (InflateException e) {

// map is already there, just return view as it is
            //CenterMap();
        }

        return view;

    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        map.setMyLocationEnabled(true);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        map.setMyLocationEnabled(false);
    }
    
    @Override
    public void onStop()
    {
        super.onStop();
        map.setMyLocationEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        map.setMyLocationEnabled(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void CenterMap() {

        //  LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //  Criteria criteria = new Criteria();

        //   Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        // if (location != null)

        // {
/*            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));*/
        MainActivity activity = (MainActivity)getActivity();
        Double lat = activity.latitude;
        Double lng = activity.longitude;
      //  String title = activity.marker

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng), 13));

        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red_24px_highres))
                        // .title(title)
                .position(new LatLng(lat, lng)));

        activity.latitude = 0D;
        activity.longitude = 0D;
         /*   CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        //  }
    }

    private void AddMarkers()
    {
        MainActivity activity = (MainActivity)getActivity();
        Double lat = activity.latitude;
        Double myLat = 0D;
        Double myLng = 0D;
        Double lng = activity.longitude;
        String title = activity.markerTitle;

        SessionManager session = new SessionManager(getActivity());

        session.checkLogin();


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
       String name = user.get(SessionManager.KEY_NAME);

        // password
       String passwd = user.get(SessionManager.KEY_PASSWORD);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
          Criteria criteria = new Criteria();

           Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
         if (location != null)
         {
             myLat = location.getLatitude();
             myLng = location.getLongitude();
         }
        //locationManager.removeGpsStatusListener();
        markers = new ArrayList<Marker>();

        Marker mark = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red_24px_highres))
                .title(title)
                .position(new LatLng(lat, lng)));

        markers.add(mark);

        Marker mark2 = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_red_24px_highres))
                .title(name)
                .visible(false)
                .position(new LatLng(myLat, myLng)));
        markers.add(mark2);

    }

    private void CenterMapOnMarkers()
    {
        CameraUpdate cu;
        if (markers.size() > 1)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            int width = getActivity().findViewById(R.id.pager).getWidth();
            int height = (int)(getActivity().findViewById(R.id.pager).getHeight() * .7); //HACK
            for (Marker marker : markers)
            {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        }
        else
        {
            cu = CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 12F);
        }

        map.moveCamera(cu); // map.moveCamera(cu);
    }
}
