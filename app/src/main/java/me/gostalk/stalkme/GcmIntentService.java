package me.gostalk.stalkme;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import Adapters.CustomRecyclerAdapter;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    String Latitude = "";
    String Longitude = "";
    String From = "";
    Bitmap remote_picture = null;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras.getString("message");
        renderLoadedData(message);
        message = "Position: " + Latitude + " " + Longitude;
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        SessionManager session = new SessionManager(this);

        if (!extras.isEmpty() && session.isLoggedIn()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                Log.i("GCM intent", "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(message);
                Log.i("GCM intent", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            remote_picture = BitmapFactory.decodeStream(
                    (InputStream) new URL("https://maps.googleapis.com/maps/api/streetview?size=600x300&location=" + Latitude + "," + Longitude + "&pitch=-0.76").getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_place_white_24dp)
                        .setLargeIcon(remote_picture)
                        .setAutoCancel(true)
                        .setContentTitle(From + "'s location")
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .setSummaryText(msg).setBigContentTitle(From + "'s location").bigPicture(remote_picture))
                        .setContentText(msg)
                        .setPriority(3)
                        .setLights(Color.parseColor("#D50000"), 1000, 3000)
                        .setColor(Color.parseColor("#F44336"))
                        .setSound(uri)
                        .setDefaults(Notification.DEFAULT_ALL);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void renderLoadedData(String stringNotification) {

        try {
            JSONObject response = new JSONObject(stringNotification);
            From = response.getString("from");
            Longitude = response.getString("longitude");
            Latitude = response.getString("latitude");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
