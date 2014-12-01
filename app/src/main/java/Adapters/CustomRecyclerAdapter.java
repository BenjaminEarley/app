package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

import me.gostalk.stalkme.MainActivity;
import me.gostalk.stalkme.NotificationViewActivity;
import me.gostalk.stalkme.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet;
    private Context mContext;
    //private Bitmap locationImage;
    private double[] longitude, latitude;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom viewholder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;
        private final TextView mTextView2;
        //private final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            mTextView2 = (TextView) itemView.findViewById(R.id.textView2);
            //mImageView = (ImageView) itemView.findViewById(R.id.info_image);
            /*
            File root = mContext.getFilesDir();
            locationImage = BitmapFactory.decodeFile(root+"/location.PNG");
            mImageView.setImageBitmap(locationImage);
            */
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            mContext,
                            "onItemClick - " + getPosition() + " - "
                                    + mTextView.getText().toString() + " - "
                                    + mDataSet[getPosition()], Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(mContext, NotificationViewActivity.class);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity) mContext,
                                    mImageView,   // The view which starts the transition
                                    "test"    // The transitionName of the view we’re transitioning to
                            );*/
                    //intent.putExtra("EXTRA_MESSAGE",mTextView.getText());
                    //intent.putExtra("EXTRA_BITMAP", getByteArray(locationImage));
                    //ActivityCompat.startActivity((MainActivity) mContext, intent, options.toBundle());
                }
            });
        }

        public TextView getmTextView() {
            return mTextView;
        }

        public TextView getmTextView2() {
            return mTextView2;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomRecyclerAdapter(double[] lo, double[] la, String[] dataSet, Context context) {
        mDataSet = dataSet;
        mContext = context;
        longitude = lo;
        latitude = la;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notifications_row_item, viewGroup, false);

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
        viewHolder.getmTextView2().setText("Latitude :" + String.valueOf(latitude[position]) + "\nLongitude: " + String.valueOf(longitude[position]));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }
}
