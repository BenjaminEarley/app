package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.io.FileOutputStream;

import me.gostalk.stalkme.MainActivity;
import me.gostalk.stalkme.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomRecyclerAdapter2 extends RecyclerView.Adapter<CustomRecyclerAdapter2.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private Context mContext;
    private double _latitude, _longitude;
    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;



    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom viewholder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                    criteria = new Criteria();
                    location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    _latitude = location.getLatitude();
                    _longitude = location.getLongitude();

                    Toast.makeText(
                            mContext,
                            "onItemClick - " + getPosition() + " - "
                                    + mTextView.getText().toString() + " - "
                                    + mDataSet[getPosition()], Toast.LENGTH_LONG).show();

                    Toast.makeText(mContext,"Long:" + _longitude + " & Lat: " + _latitude, Toast.LENGTH_LONG).show();

                }
            });
        }

        public TextView getmTextView() {
            return mTextView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomRecyclerAdapter2(String[] dataSet, Context context) {
        mDataSet = dataSet;
        mContext = context;

    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notifications_row_item_2, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getmTextView().setText(mDataSet[position]);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

}
