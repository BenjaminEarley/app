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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import Adapters.CustomRecyclerAdapter2;

public class Tab2Fragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private static final String TAG = "RecyclerView2Fragment";

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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.tab2_view, container, false);
        rootView.setTag(TAG);
        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView2);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomRecyclerAdapter2(mDataset, getActivity());
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

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[1];
        mDataset[0] = "Loading...";

        MainActivity activity = (MainActivity)getActivity();
        String name = activity.name;
        String passhash = activity.passwd;

        String URL = "http://api.gostalk.me/relation/" + name;
        try {
            URL += "?" + "passhash=" + URLEncoder.encode(passhash, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("IDK", e);
        }

        StringRequest jsLoginPostRequest = new StringRequest( Request.Method.GET, URL,
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
        requestQueue.add(jsLoginPostRequest);

    }

    private void renderLoadedData(String stringResponse) {
        try {
            JSONObject response = new JSONObject(stringResponse);
            String code = response.getJSONObject("meta").getString("code");
            if (code.equals("200")) {
                JSONArray people = response.getJSONArray("response");
                mDataset = new String[people.length()];
                for ( int i = 0; i < people.length(); i++ ) {
                    JSONObject person = (JSONObject) people.get(i);
                    mDataset[i] = person.getString("Username");
                }
            } else {
                Log.e("Tab2", "Had trouble loading data");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mAdapter = new CustomRecyclerAdapter2(mDataset, getActivity());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }


    private void searchAndInitDataset(String search) {
        mDataset = new String[60];
        for (int i=0; i < 60; i++) {
            if (mDataset[i].toLowerCase().contains(search.toLowerCase()))
            mDataset[i] = "This is friend #" + i;
        }
    }
}