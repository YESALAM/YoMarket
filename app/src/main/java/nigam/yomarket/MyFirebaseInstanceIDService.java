package nigam.yomarket;


import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import nigam.yomarket.Adapters.notificationadapter;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Belal on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        new notify(refreshedToken).doInBackground();

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project



    }



    class notify
    {
        String token;
        public notify(String token){
            this.token = token ;
        }

        protected Object doInBackground() {

        try {
            if(Statics.id == null) return null ;
            String baseURL = apis.BASE_API+apis.TOKEN_UPDATE+"?r_id="+ Statics.id+"&d_id="+token;//+Statics.notificationcounterid;
            Log.i(this.getClass().getSimpleName(),"updating token --> "+Statics.id+"  --  "+token);

             Utilities.readJson(getBaseContext(), "POST", baseURL);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    }
}