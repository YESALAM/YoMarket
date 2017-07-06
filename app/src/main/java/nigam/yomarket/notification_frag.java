package nigam.yomarket;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nigam.yomarket.Adapters.main_frag_rview;
import nigam.yomarket.Adapters.notificationadapter;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;


/**
 * A simple {@link Fragment} subclass.
 */
public class notification_frag extends Fragment {
RecyclerView rv;

    public notification_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_notification_frag, container, false);



        rv= (RecyclerView) v.findViewById(R.id.notification_recycler);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean firstrun = sharedPreferences.getBoolean("firstrun",true);
        if(!firstrun){


        }
        String id = sharedPreferences.getString("nid","0");

        if (Utilities.isInternetOn(getActivity()))
            new notify(id).execute();
        else
            Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();


        return v;
    }

    class notify extends AsyncTask
    {
        notificationadapter adapter;
        String jsonString,
                response;
        ArrayList<HomeListGetSet> list =new ArrayList();

        String id ;
        public notify(String id) {
            this.id = id;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                String baseURL = apis.BASE_API+apis.Notificatiom_API+"?id="+id;//+Statics.notificationcounterid;
                Log.i("doInBackground:response","aaaaaaada"+baseURL);

                jsonString = Utilities.readJson(getActivity(), "POST", baseURL);

                Log.i("doInBackground:response","aaaaaasadaada"+jsonString);
                JSONObject head = new JSONObject(jsonString);

                JSONArray data=head.getJSONArray("server response");
                Log.i("doInBackground:response","aaaaaaaaadsfsdfsdaaa"+data.toString());

                for (int i = 0; i < data.length(); i++)
                {
                    HomeListGetSet ps=new HomeListGetSet();
                    JSONObject obj = data.getJSONObject(i);

//                    ps.setS_no(obj.getString("s_no"));
                    ps.setPost_product(obj.getString("post_product"));
                    ps.setPost_city(obj.getString("post_city"));
                    ps.setPost_profession(obj.getString("post_profession"));
                    ps.setPost_quantity(obj.getString("post_quantity"));
                    ps.setPost_price(obj.getString("post_price"));
                    ps.setPost_description(obj.getString("post_description"));
                    ps.setPost_id(obj.getString("post_id"));
                    ps.setPost_image_1(obj.getString("post_image_1"));
                    ps.setPost_image_2(obj.getString("post_image_2"));
                    ps.setPost_image_3(obj.getString("post_image_3"));
                    ps.setPost_image_4(obj.getString("post_image_4"));



                    //Statics.notificationcounterid= Integer.parseInt(obj.getString("s_no"));
                    list.add(ps);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Log.i("doInBackground:response","aaaaaaaaaaaa list size"+list.size());


            adapter=new notificationadapter((AppCompatActivity) getActivity(),list);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);

            if(list.size()>0){
                int index = list.size()-1 ;
                String id = list.get(index).getPost_id();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("nid", id);
                editor.commit();

                // First let's define the intent to trigger when notification is selected
// Start out by creating a normal intent (in this case to open an activity)
                Intent intent = new Intent(getActivity(), MainActivity.class);
// Next, let's turn this into a PendingIntent using
//   public static PendingIntent getActivity(Context context, int requestCode,
//       Intent intent, int flags)
                int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
                int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
                PendingIntent pIntent = PendingIntent.getActivity(getContext(), requestID, intent, flags);
// Now we can attach the pendingIntent to a new notification using setContentIntent
                Notification noti = new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.logo_main)
                        .setContentTitle("Post added")
                        .setContentText("New items are posted in market")
                        .setContentIntent(pIntent)
                        .setAutoCancel(true) // Hides the notification after its been selected
                        .build();
// Get the notification manager system service
                NotificationManager mNotificationManager =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(0, noti);


            }






        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("nid","0");

        if (Utilities.isInternetOn(getActivity()))
            new notify(id).execute();
        else
            Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

    }
}
