package me.gostalk.stalkme;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
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
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import Adapters.CustomRecyclerAdapter2;

public class Tab2Fragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private static final String TAG = "RecyclerView2Fragment";

    RequestQueue requestQueue;
    View dialogLayout;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        MainActivity main = (MainActivity)getActivity();
        if (main.session.isLoggedIn())
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
        mRecyclerView.setOnTouchListener(new ShowHideOnScroll(fab1));

        //Set floating button Click Listener
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 dialogLayout = inflater.inflate(R.layout.add_friend, container, false);
                 builder = new AlertDialog.Builder(getActivity());
                SetDialog(dialogLayout);
                builder.setView(dialogLayout);

                builder.show();
            }
        });

        return rootView;
    }

private void SetDialog(View view)
{

    //200 add success 304 relation exits 403 u1 not exist 402 u2 dne
    // String[] people = {"person1", "person2", "person3"}; // /relation/username1/add/username2?passhash=$passhash
   final EditText txtSearch = (EditText) view.findViewById(R.id.txtSearchPeople);
    //Button btnAdd = (Button) view.findViewById(R.id.btnAddFriend);
   // Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

/*    btnAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });*/

            builder.setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SessionManager session = new SessionManager(getActivity());
                    String URL = "https://api.gostalk.me/relation/";
                    HashMap<String, String> user = session.getUserDetails();
                    String name = user.get(SessionManager.KEY_NAME);
                    String passhash = user.get(SessionManager.KEY_PASSWORD);

                    try {
                        URL += URLEncoder.encode(name, "UTF-8") + "/add/" + txtSearch.getText() + "?passhash=" + passhash;
                    }
                    catch (UnsupportedEncodingException e) {
                        Log.wtf("Login", e);
                    }
                    StringRequest jsLoginPostRequest = new StringRequest( Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    GetResponse(response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("RegisterQuery", "Failed to register " + error);
                        }
                    });
                    requestQueue.add(jsLoginPostRequest);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
}
    private void GetResponse(String stringResponse)
    {
        try {
            JSONObject response = new JSONObject(stringResponse);
            String code = response.getJSONObject("meta").getString("code");

            switch (code)
            {
                case "200":
                    Toast.makeText(getActivity().getApplicationContext(), "Friend added", Toast.LENGTH_LONG).show();
                    initDataset();
                    break;
                case "304":
                    Toast.makeText(getActivity().getApplicationContext(), "You've already added this friend", Toast.LENGTH_LONG).show();
                    break;
                case "402":
                    Toast.makeText(getActivity().getApplicationContext(), "Friend does not exist", Toast.LENGTH_LONG).show();
                    break;
                case "403":
                    Toast.makeText(getActivity().getApplicationContext(), "User does not exist", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[1];
        mDataset[0] = "";

        SessionManager session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME);
        String passhash = user.get(SessionManager.KEY_PASSWORD);

        String URL = "https://api.gostalk.me/relation/" + name;
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