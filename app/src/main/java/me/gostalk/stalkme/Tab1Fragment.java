package me.gostalk.stalkme;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import Adapters.CustomRecyclerAdapter;
import Adapters.CustomRecyclerAdapter2;

public class Tab1Fragment extends Fragment  {

    private static final String TAG = "RecyclerViewFragment";

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    //Bitmap locationImage;
    protected double[] longitude;
    protected double[] latitude;

    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab1_view, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomRecyclerAdapter(longitude, latitude, mDataset, getActivity());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)

        return rootView;
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    //private void initDataset() {
    //    mDataset = new String[60];
    //    latitude = new double[60];
    //    longitude = new double[60];
    //    for (int i=0; i < 60; i++) {
    //        mDataset[i] = "This is notification #" + i;
    //        latitude[i] = 36.7163306 + i;
    //        longitude[i] = -76.2652907 + i;
    //    }
    //}

    private void initDataset() {
        mDataset = new String[1];
        mDataset[0] = "Loading...";

        latitude = new double[1];
        latitude[0] = 0D;

        longitude = new double[1];
        longitude[0] = 0D;

        SessionManager session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);
        String passhash = user.get(SessionManager.KEY_PASSWORD);
        MainActivity activity = (MainActivity)getActivity();
        passhash = activity.passwd;
        name = activity.name;

        String URL = "http://api.gostalk.me/user/" + name + "/since/1";
        try {
            URL += "?" + "passhash=" + URLEncoder.encode(passhash, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("IDK", e);
        }

        StringRequest getNotificationsRequest = new StringRequest( Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        renderLoadedData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginQuery", "Failed to login " + error);
            }
        });
        requestQueue.add(getNotificationsRequest);

    }

    private void renderLoadedData(String stringResponse) {
        try {
            JSONObject response = new JSONObject(stringResponse);
            String code = response.getJSONObject("meta").getString("code");
            if (code.equals("200")) {
                JSONArray notifications = response.getJSONArray("response");
                mDataset = new String[notifications.length()];
                longitude = new double[notifications.length()];
                latitude = new double[notifications.length()];
                for ( int i = 0; i < notifications.length(); i++ ) {
                    JSONObject notification = (JSONObject) notifications.get(i);
                    mDataset[i] = notification.getString("Sender");
                    latitude[i] = Double.valueOf(notification.getString("Lat"));
                    longitude[i] = Double.valueOf(notification.getString("Lng"));
                }
            } else {
                Log.e("Tab2", "Had trouble loading data");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mAdapter = new CustomRecyclerAdapter(longitude, latitude, mDataset, getActivity());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }
}