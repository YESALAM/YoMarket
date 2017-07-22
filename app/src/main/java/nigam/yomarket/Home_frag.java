package nigam.yomarket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import nigam.yomarket.Adapters.WrapLinearLayoutManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nigam.yomarket.Adapters.home_frag_rview;
import nigam.yomarket.Adapters.main_frag_rview;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.imagehelper.ImageLoader;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;


public class Home_frag extends Fragment {

    public Home_frag() {
        // Required empty public constructor
    }

RecyclerView rv;

    home_frag_rview adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home_frag, container, false);

        setHasOptionsMenu(false);

        rv= (RecyclerView) v.findViewById(R.id.rvself);
        /*TextView nameph = (TextView) v.findViewById(R.id.dialog_phonebook_name);
        TextView contactph = (TextView) v.findViewById(R.id.dialog_phonebook_contact);
        TextView city = (TextView) v.findViewById(R.id.dialog_phonebook_city);
        TextView profession = (TextView) v.findViewById(R.id.dialog_phonebook_profession);
        TextView product = (TextView) v.findViewById(R.id.dialog_phonebook_product);
        TextView firm = (TextView) v.findViewById(R.id.dialog_phonebook_firmname);
        ImageView image= (ImageView) v.findViewById(R.id.dialog_phonebook_image);*/

       /* if (Statics.isLogin==false)
        {
            nameph.setText("guest");
            contactph.setText("guest");
            city.setText("guest");
            profession.setText("guest");
            product.setText("guest");
            firm.setText("guest");
            Toast.makeText(getActivity(),"Login to see details!!!",Toast.LENGTH_LONG).show();
        }
        else
        {
            String image_url1 =apis.IMAGE_FOR_PHONEBOOK+Statics.id+"/"+Statics.id;
            ImageLoader imgLoader = new ImageLoader(getActivity());

            imgLoader.DisplayImage(image_url1+".jpg", R.drawable.logo_main, image);


            Log.i( "onPostExecute: ", "ID =" + Statics.id);
            nameph.setText(Statics.name);
            contactph.setText(Statics.phone);
            city.setText(Statics.city);
            profession.setText(Statics.profession);
            product.setText(Statics.product);
            firm.setText(Statics.firmname);
            if (Utilities.isInternetOn(getActivity()))
            new data().execute();
            else
                Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();
        }*/

        if(Statics.isLogin){
            if (Utilities.isInternetOn(getActivity()))
                new data().execute();
            else
                Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();
        }else {
            TextView textView = (TextView) v.findViewById(R.id.textView30);
            textView.setText("Log In to see your Post");
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.filter:

                return true;


        }
        return onOptionsItemSelected(item);
    }

    class data extends AsyncTask
    {
        ArrayList<HomeListGetSet> list =new ArrayList();
        private  final String TAG = "HomeFrag_data" ;

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String baseURL = apis.BASE_API+"getpostbyid.php?id="+Statics.id ;
                String jsonString = Utilities.readJson(getActivity(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");
                //Log.e(TAG, "Received from "+baseURL+"  "+reader.toString());

                for (int i = 0; i < data.length(); i++)
                {
                    HomeListGetSet ps=new HomeListGetSet();
                    JSONObject obj = data.getJSONObject(i);


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
                    ps.setposted_by_id(obj.getString("post_posted_by_id"));
                    ps.setPosted_by_name(obj.getString("post_posted_by"));
                    list.add(ps);
//                    adapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter=new home_frag_rview((AppCompatActivity) getActivity(),list);
            RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getContext());
            //rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }
}
