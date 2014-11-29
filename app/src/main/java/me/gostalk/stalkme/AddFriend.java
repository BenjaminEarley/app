package me.gostalk.stalkme;

//import android.app.Fragment;
import android.app.ListActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static me.gostalk.stalkme.R.layout.add_friend;

/**
 * Created by Ryan on 11/29/2014.
 */
public class AddFriend extends Fragment{

    private ListView lstPeople;
    private TextView txtSearch;
    private String[] people = {"person1", "person2", "person3"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.tab2_view, container, false);
        //rootView.setTag(TAG);

        lstPeople = (ListView) rootView.findViewById(R.id.lstPeople);
        txtSearch = (TextView) rootView.findViewById(R.id.txtSearchPeople);

      //  rootView.setContentView(add_friend);
      //  setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, people));



        return rootView;
    }
}
