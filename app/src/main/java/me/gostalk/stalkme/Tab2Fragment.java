package me.gostalk.stalkme;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import Adapters.CustomRecyclerAdapter2;

public class Tab2Fragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private GoogleMap map;
    private MapView mapView;
    protected String[] mDataset;
    double latitude, logitude;
    private static final String TAG = "RecyclerView2Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
        //searchAndInitDataset("7");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.tab2_view, container, false);
        rootView.setTag(TAG);

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) rootView.findViewById(R.id.map_invisible);
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
            CenterMap();
        } catch (InflateException e) {
            // map is already there, just return view as it is
        }

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView2);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomRecyclerAdapter2(latitude, logitude,map, mDataset, getActivity());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        // Make this {@link Fragment} listen for changes FABs.
        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        //Set floating button Click Listener
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "test", Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setOnTouchListener(new ShowHideOnScroll(fab1));

        //Set floating button Click Listener
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogLayout = inflater.inflate(R.layout.add_friend, container, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                SetDialog(dialogLayout);
                builder.setView(dialogLayout);
                builder.show();
            }
        });


        return rootView;
    }

private void SetDialog(View view)
{
    ListView lstPeople = (ListView) view.findViewById(R.id.lstPeople);
     String[] people = {"person1", "person2", "person3"};
     ArrayAdapter<String> adapter;
    EditText txtSearch = (EditText) view.findViewById(R.id.txtSearchPeople);

    adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, people);
    lstPeople.setAdapter(adapter);
   lstPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //TODO add friend when clicked
       }
   });

    txtSearch.addTextChangedListener(new TextWatcher() {

        public void afterTextChanged(Editable s) {

           //TODO implement Search/ update adapter

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    });



}

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[60];
        for (int i=0; i < 60; i++) {
            mDataset[i] = "This is friend #" + i;
        }
    }

    private void searchAndInitDataset(String search) {
        mDataset = new String[1];
        //if(mDataset[i].toLowerCase().contains(search.toLowerCase()))
            mDataset[0] = "This is friend #" + 7;
    }

    private void CenterMap() {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)

        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            latitude = location.getLatitude();
            logitude = location.getLongitude();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }
}