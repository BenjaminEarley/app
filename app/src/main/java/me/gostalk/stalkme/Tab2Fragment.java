package me.gostalk.stalkme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shamanland.fab.FloatingActionButton;
import com.shamanland.fab.ShowHideOnScroll;

public class Tab2Fragment extends Fragment {

    private final static String TAG = "FloatingActionButtonBasicFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab2_view, container, false);

        // Make this {@link Fragment} listen for changes in both FABs.
        FloatingActionButton fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_1);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnTouchListener(new ShowHideOnScroll(fab1));

        return rootView;
    }

}