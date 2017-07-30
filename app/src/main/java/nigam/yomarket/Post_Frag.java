package nigam.yomarket;


import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import nigam.yomarket.Adapters.WrapLinearLayoutManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nigam.yomarket.Adapters.main_frag_rview;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;


/**
 * A simple {@link Fragment} subclass.
 */
public class Post_Frag extends Fragment implements ViewSwitcher.ViewFactory {
LinearLayout ll;
    FloatingActionButton addpost;
    main_frag_rview adapter;
    RecyclerView rv;
    String productlist[]={"Fruit","Vegetables","fruits and vegetables","transport"};
    String Professionlist[]={"Wholeseller","Farmer","Retailer","Exporter","Importer","Commision Agent","Transporter"};
    static ArrayList<HomeListGetSet> list =new ArrayList();
    public Post_Frag() {
        // Required empty public constructor
    }
    int index = 0;
   /* private static final int imgs[] =
            {
                    R.drawable.one,
                    R.drawable.two,
                    R.drawable.three,
                    R.drawable.four,
                    R.drawable.five,
                    R.drawable.five


            };*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_post_frag, container, false);
        setHasOptionsMenu(true);
        rv= (RecyclerView) v.findViewById(R.id.recycler_home);

        //final ImageSwitcher imageSwitcher = (ImageSwitcher) v.findViewById(R.id.imageswitch);
        ll= (LinearLayout) v.findViewById(R.id.LL1);
        addpost= (FloatingActionButton) v.findViewById(R.id.FAB_addpost);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Statics.isLogin)
                    startActivity(new Intent(getActivity(),post_Activity.class));
                else
                    Toast.makeText(getActivity(),"Login First To Post",Toast.LENGTH_LONG).show();
            }
        });

        adapter=new main_frag_rview((AppCompatActivity) getActivity(),list);
        RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        /*imageSwitcher.setFactory(this);
        imageSwitcher.postDelayed(new Runnable() {
            public void run() {
                imageSwitcher.setImageResource(imgs[index]);
                if(index==(imgs.length-1))
                    index = 0;
                else
                    index++;
                imageSwitcher.postDelayed(this, 3000);
            }
        }, 3);*/

       if(list.isEmpty()){
           if (Utilities.isInternetOn(getActivity()))
               new data().execute();
           else
               Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();
       }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(list.isEmpty()){
            if (Utilities.isInternetOn(getActivity()))
                new data().execute();
            else
                Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.frag_home, menu);
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        switch (item.getItemId()) {
            case R.id.filter:
                filter();
                return true;


        }
        return onOptionsItemSelected(item);
    }

    public void refresh(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
        new data().execute();
    }



    private void filter()
    {
        final Dialog builder = new Dialog(getActivity());
        builder.setContentView(R.layout.filterpostview);
        builder.setTitle("Filter");
        final Button filter= (Button) builder.findViewById(R.id.filterphonebookfilter);
        Button clearfilter= (Button) builder.findViewById(R.id.filterphonebookclearfilter);

        final Spinner product= (Spinner) builder.findViewById(R.id.filterpostproduct);

        final Spinner profession= (Spinner) builder.findViewById(R.id.filterpostprofssion);
        ArrayAdapter<String> as=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, Professionlist);
        as.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profession.setAdapter(as);

        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, productlist);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        product.setAdapter(spinnerAdapter1);



        clearfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isInternetOn(getActivity()))
                new data().execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

                builder.dismiss();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productfilter,professionfilter;
                    productfilter=product.getSelectedItem().toString();
                    professionfilter=profession.getSelectedItem().toString();
                if (Utilities.isInternetOn(getActivity()))
                new datafilter(professionfilter,productfilter).execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

                builder.dismiss();
            }
        });


        builder.show();
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(
                new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }
    class datafilter extends AsyncTask
    {
        ArrayList<HomeListGetSet> list =new ArrayList();
        String profession,product;
        public datafilter(String profession,String product) {
            super();
            this.product=Utilities.replaceSpaceInString(product);
            this.profession=Utilities.replaceSpaceInString(profession);
        }

        private  final String TAG = Post_Frag.class.getSimpleName();

        @Override
        protected Object doInBackground(Object[] params) {
            //list.clear();
            try {
                String baseURL = apis.BASE_API+apis.FILTER_POST+"?profession="+profession+"&product="+product ;


                String jsonString = Utilities.readJson(getActivity(), "GET", baseURL);
                Log.i("PostFrag_datafilter", "From "+baseURL+" "+jsonString);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");
                //Log.e(TAG, "doInBackground: "+reader );

                for (int i = 0; i < data.length(); i++)
                {
                    HomeListGetSet ps=new HomeListGetSet();
                    JSONObject obj = data.getJSONObject(i);

                    //ps.setS_no(obj.getString("s_no"));
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
                    ps.setPosted_by_name(obj.getString("post_posted_by"));
                    ps.setposted_by_id(obj.getString("post_posted_by_id"));

                    ps.setDate(obj.getString("date"));
                    ps.setTime(obj.getString("time"));
                    //ps.setMobile_no(obj.getString("register_mobile_no"));
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
            adapter=new main_frag_rview((AppCompatActivity) getActivity(),list);
            RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getContext());
            //rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }
    class data extends AsyncTask
    {

        ArrayList<HomeListGetSet> local_list =new ArrayList();
        private  final String TAG = Post_Frag.class.getSimpleName();

        @Override
        protected Object doInBackground(Object[] params) {
        //list.clear();
            try {
                String baseURL = apis.BASE_API+apis.posts_API ;
                String jsonString = Utilities.readJson(getActivity(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");
                Log.i("PostFrag_data", "From "+baseURL+" "+data);

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
                    ps.setMobile_no(obj.getString("register_mobile_no"));

                    ps.setDate(obj.getString("date"));
                    ps.setTime(obj.getString("time"));


                    local_list.add(ps);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            list = local_list ;
            adapter=new main_frag_rview((AppCompatActivity) getActivity(),local_list);
            RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getContext());
            //rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }

}
