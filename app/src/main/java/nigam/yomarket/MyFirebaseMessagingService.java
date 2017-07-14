package nigam.yomarket;



import java.util.ArrayList;
import java.util.Map;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import nigam.yomarket.getset.HomeListGetSet;


/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        Map<String,String> data = remoteMessage.getData();


        //Calling method to generate notification
        sendNotification(data);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(Map<String,String> data) {
        String body = data.get("comment");
        String title = data.get("comment_user_name");

        HomeListGetSet h = new HomeListGetSet();
        h.setPost_id(data.get("post_id"));
        h.setPost_product(data.get("post_product"));
        h.setPost_city(data.get("post_city"));
        h.setPost_profession(data.get("post_profession"));
        h.setPost_quantity(data.get("post_quantity"));
        h.setPost_price(data.get("post_price"));
        h.setPost_description(data.get("post_description"));
        h.setposted_by_id(data.get("post_posted_by_id"));
        h.setPosted_by_name(data.get("post_posted_by"));
        h.setPost_image_1(data.get("post_image_1"));
        h.setPost_image_2(data.get("post_image_2"));
        h.setPost_image_3(data.get("post_image_3"));
        h.setPost_image_4(data.get("post_image_4"));

        final ArrayList<String> image_list = new ArrayList<>();
        int count = 0 ;
        if(!h.getPost_image_1().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_1());
        }
        if(!h.getPost_image_2().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_2());
        }
        if(!h.getPost_image_3().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_3());
        }
        if(!h.getPost_image_4().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_4());
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA",h);
        bundle.putInt("count",count);
        bundle.putStringArrayList("list",image_list);






        Intent intent = new Intent(this, Posts_activity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}