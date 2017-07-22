package nigam.yomarket;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import nigam.yomarket.Adapters.WrapLinearLayoutManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import nigam.yomarket.Adapters.commentsAdapter;
import nigam.yomarket.Adapters.main_frag_rview;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.getset.getcomments;
import nigam.yomarket.imagehelper.ImageLoader;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;

import static nigam.yomarket.utils.Utilities.replaceSpaceInString;

public class Posts_activity extends AppCompatActivity {
    TextView quantity,postedby,city,product,price,post_id,profession,Discription,commenthere,totalcomments;
    Button comment;
    LinearLayout comments;
    HomeListGetSet data;
    ProgressDialog pd;

    String date,time;
    RecyclerView rv;
    ImageView imagemain,image1,image2,image3,image4;
    CardView images ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Post ");
        setContentView(R.layout.activity_posts_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        totalcomments= (TextView) findViewById(R.id.totalcomments);
        pd=new ProgressDialog(Posts_activity.this);
        pd.setTitle("Loading...");
        comments= (LinearLayout) findViewById(R.id.comment_layout);
        imagemain= (ImageView) findViewById(R.id.post_image_switcher);
        image1= (ImageView) findViewById(R.id.image1);
        image2= (ImageView) findViewById(R.id.image2);
        image3= (ImageView) findViewById(R.id.image3);
        image4= (ImageView) findViewById(R.id.image4);
        images = (CardView) findViewById(R.id.images_cardview);

        data = (HomeListGetSet)getIntent().getExtras().getSerializable("DATA");
        rv= (RecyclerView) findViewById(R.id.recent_comments);

        /*quantity= (TextView) findViewById(R.id.posts_quantity);
        city = (TextView) findViewById(R.id.posts_city);
        product= (TextView) findViewById(R.id.posts_product);
        price= (TextView) findViewById(R.id.posts_price);
        post_id= (TextView) findViewById(R.id.posts_ID);
        profession= (TextView) findViewById(R.id.posts_Profrssion);
        Discription= (TextView) findViewById(R.id.posts_Discription);
        postedby= (TextView) findViewById(R.id.post_posted_by);*/

        commenthere= (TextView) findViewById(R.id.posts_comment);

        comment= (Button) findViewById(R.id.post_comment_button);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (Statics.isLogin)
                {
                    chkdata();
                }
                else
               {
                   AlertDialog.Builder builder;
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       builder = new AlertDialog.Builder(new ContextThemeWrapper(Posts_activity.this, R.style.myDialog));
                   } else {
                       builder = new AlertDialog.Builder(new ContextThemeWrapper(Posts_activity.this, R.style.myDialog));
                   }
                   builder.setTitle("Login First")
                           .setMessage("Login to post comment!!!")
                           .setPositiveButton("Login ", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   startActivity(new Intent(Posts_activity.this,Login_Activity.class));                               }
                           })
                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();



               }
            }
        });





        /*quantity.setText(data.getPost_quantity());
        city.setText(data.getPost_city());
        product.setText(data.getPost_product());
        price.setText(data.getPost_price());
        post_id.setText(data.getPost_id());
        profession.setText(data.getPost_profession());
        Discription.setText(data.getPost_description());
        postedby.setText(data.getPosted_by_name());*/



       /* String image_url =" http://simption.com/yomarket/cms/documents/image.php?path=post/"
                + data.getPost_id()+"/"+replaceSpaceInString(data.getPost_image_1());
        Log.e( "onCreate: ",image_url);*/

       /* String image_url1 =apis.IMAGE_API+data.getPost_id();
        ImageLoader imgLoader = new ImageLoader(getBaseContext());
        int loader = R.drawable.logo_main;

        imgLoader.DisplayImage(image_url1+"/1.jpg", loader, image1);
        imgLoader.DisplayImage(image_url1+"/2.jpg", loader, image2);
        imgLoader.DisplayImage(image_url1+"/3.jpg", loader,image3);
        imgLoader.DisplayImage(image_url1+"/4.jpg", loader, image4);

        imgLoader.DisplayImage(image_url1+"/1.jpg", loader, imagemain);*/


        int count = getIntent().getExtras().getInt("count");
        final ArrayList<String> list = getIntent().getExtras().getStringArrayList("list");
        showImages(count,list);

        Log.e(this.getClass().getSimpleName(),count+" -->  "+list.size()) ;

        if (count == 0) {
            Glide.with(this)
                    .load(R.drawable.logo_main)
                    .placeholder(R.drawable.logo_main)
                    .into(imagemain);
        } else {
            Glide.with(this)
                    .load(apis.IMAGE_API + data.getPost_id() +"/"+list.get(0)+".jpg")
                    //.placeholder(R.drawable.logo_main)
                    .into(imagemain);
        }



         //   new DownloadImageTask(imagemain).execute(image_url);
        if (Utilities.isInternetOn(getBaseContext()))
        new getcomment().execute();
        else
            Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();


        Log.e( "onCreate: test intent ",data.getPost_price()+" "+data.getPost_quantity() );

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog builder = new Dialog(Posts_activity.this);
                builder.setContentView(R.layout.fullimageview);
                builder.setTitle("Filter");
                ImageLoader imgLoader = new ImageLoader(getBaseContext());
                ImageView img= (ImageView) builder.findViewById(R.id.fullimage);

                /*String image_url1 =apis.IMAGE_API+data.getPost_id();
                imgLoader.DisplayImage(image_url1+"/1.jpg", R.drawable.logo_main, img);*/
                Glide.with(getBaseContext())
                        .load(apis.IMAGE_API+data.getPost_id()+ "/"+list.get(0)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(img);

                builder.show();

            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog builder = new Dialog(Posts_activity.this);
                builder.setContentView(R.layout.fullimageview);
                builder.setTitle("Filter");
                ImageLoader imgLoader = new ImageLoader(getBaseContext());
                ImageView img= (ImageView) builder.findViewById(R.id.fullimage);

               /* String image_url1 =apis.IMAGE_API+data.getPost_id();
                imgLoader.DisplayImage(image_url1+"/2.jpg", R.drawable.logo_main, img);*/
                Glide.with(getBaseContext())
                        .load(apis.IMAGE_API+data.getPost_id()+ "/"+list.get(1)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(img);

                builder.show();
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog builder = new Dialog(Posts_activity.this);
                builder.setContentView(R.layout.fullimageview);
                builder.setTitle("Filter");
                ImageLoader imgLoader = new ImageLoader(getBaseContext());
                ImageView img= (ImageView) builder.findViewById(R.id.fullimage);

               /* String image_url1 =apis.IMAGE_API+data.getPost_id();
                imgLoader.DisplayImage(image_url1+"/3.jpg", R.drawable.logo_main, img);*/
                Glide.with(getBaseContext())
                        .load(apis.IMAGE_API+data.getPost_id()+ "/"+list.get(2)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(img);

                builder.show();
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog builder = new Dialog(Posts_activity.this);
                builder.setContentView(R.layout.fullimageview);
                builder.setTitle("Filter");
                ImageLoader imgLoader = new ImageLoader(getBaseContext());
                ImageView img= (ImageView) builder.findViewById(R.id.fullimage);

               /* String image_url1 =apis.IMAGE_API+data.getPost_id();
                imgLoader.DisplayImage(image_url1+"/4.jpg", R.drawable.logo_main, img);*/
                Glide.with(getBaseContext())
                        .load(apis.IMAGE_API+data.getPost_id()+ "/"+list.get(3)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(img);

                builder.show();
            }
        });
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.i( "onCreate: ",urls[0]);

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            pd.dismiss();
            Bitmap b=Utilities.compressImage(result);
            bmImage.setImageBitmap(b);
        }
    }

    public String getdate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate =  mdformat.format(calendar.getTime());
        return strDate;
    }
    private  String gettime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh-mm-aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        Log.i( "gettime: ",formattedDate);
        return formattedDate;
    }
    public void chkdata()
    {

        String comment;

        date=getdate();
        time=gettime();

        comment=commenthere.getText().toString();


        if (comment.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(),"Fields Must Not be Empty",Toast.LENGTH_LONG).show();

        }
        else
            if (Utilities.isInternetOn(getBaseContext()))
            new commenttask().execute();
        else
                Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();



    }
    class commenttask extends AsyncTask
    {
        String jsonString,response;
        String postid;
        String name,city,phone,profession,comment,id;
        boolean successful  = false ;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
                comment=Utilities.replaceSpaceInString(commenthere.getText().toString());
                name=Statics.name;
                city=Statics.city;
                phone=Statics.phone;
                profession=Statics.profession;
                postid=data.getPost_id();
                if (Statics.isLogin)
                {
                    id=Statics.id;
                }
                else
                    id="null";
        }
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String baseURL = apis.BASE_API+apis.COMMENT_API+"?name="+Utilities.replaceSpaceInString(name)+"&id="+id+
                        "&phone="+Utilities.replaceSpaceInString(phone)+"&date="+Utilities.replaceSpaceInString(date)+"&time="+Utilities.replaceSpaceInString(time)+"&city="+Utilities.replaceSpaceInString(city)+"&profession="+Utilities.replaceSpaceInString(profession)
                        +"&comment="+comment+"&post_id="+postid;
                Log.i("doInBackground:response",baseURL);
                jsonString = Utilities.readJson(getBaseContext(), "GET", baseURL);
                Log.e(this.getClass().getSimpleName(),jsonString);

                JSONObject head = new JSONObject(jsonString);

                response=head.getString("server response");

                if(response.toString().equalsIgnoreCase("comment sucessfull"))
                {
                    successful = true ;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(successful)
            {
                Toast.makeText(getApplicationContext(),"comment sucessfull!!!",Toast.LENGTH_LONG).show();
                commenthere.setText("");
                if (Utilities.isInternetOn(getBaseContext()))
                new getcomment().execute();
                else
                    Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
            }
        }
    }
    ArrayList<getcomments> arraycomment;
    class getcomment extends AsyncTask
    {
        commentsAdapter adapter;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arraycomment =new ArrayList<>();

        }

        String jsonString,name,date_time,commentmade;
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String baseURL = apis.BASE_API+apis.GET_COMMENT_API+"?post_id="+data.getPost_id();
                Log.i("doInBackground:response",baseURL);
                jsonString = Utilities.readJson(getBaseContext(), "POST", baseURL);

                JSONObject reader = new JSONObject(jsonString);
                JSONArray data = reader.getJSONArray("server response");
                for (int i = 0; i < data.length() ; i++) {

                    JSONObject obj = data.getJSONObject(i);

                    getcomments gc=new getcomments();

                    gc.setComments(obj.getString("comment"));
                    gc.setDate_time(obj.getString("comment_date")+" "+obj.getString("comment_time"));
                    gc.setName(obj.getString("comment_user_name"));

                    arraycomment.add(gc);
                    Log.i("doInBackground:response",obj.getString("comment")+"  "+obj.getString("comment_date")+" "+obj.getString("comment_time"));

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Collections.reverse(arraycomment);
            adapter=new commentsAdapter((AppCompatActivity) Posts_activity.this,arraycomment);
            RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getApplicationContext());
            rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);

            totalcomments.setText("Total Comments: "+arraycomment.size());

        }
    }

    private void showImages(int count,ArrayList<String> list){
        switch (count){
            case 4:
                Glide.with(this)
                        .load(apis.IMAGE_API + data.getPost_id() + "/"+list.get(0)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image1);
                Glide.with(this)
                        .load(apis.IMAGE_API+data.getPost_id()+"/"+list.get(1)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image2);

                Glide.with(this)
                        .load(apis.IMAGE_API+data.getPost_id()+"/"+list.get(2)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image3);

                Glide.with(this)
                        .load(apis.IMAGE_API+data.getPost_id()+"/"+list.get(3)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image4);
                break;
            case 3:
                Glide.with(this)
                        .load(apis.IMAGE_API + data.getPost_id() + "/"+list.get(0)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image1);
                Glide.with(this)
                        .load(apis.IMAGE_API+data.getPost_id()+"/"+list.get(1)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image2);

                Glide.with(this)
                        .load(apis.IMAGE_API+data.getPost_id()+"/"+list.get(2)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image3);
                image4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                Glide.with(this)
                        .load(apis.IMAGE_API + data.getPost_id() + "/"+list.get(0)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image1);
                Glide.with(this)
                        .load(apis.IMAGE_API+data.getPost_id()+"/"+list.get(1)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image2);
                image3.setVisibility(View.INVISIBLE);
                image4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                Glide.with(this)
                        .load(apis.IMAGE_API + data.getPost_id() + "/"+list.get(0)+".jpg")
                        //.placeholder(R.drawable.logo_main)
                        .into(image1);

                image2.setVisibility(View.INVISIBLE);
                image3.setVisibility(View.INVISIBLE);
                image4.setVisibility(View.INVISIBLE);
                break;
            case 0:
                images.setVisibility(View.GONE);
                image1.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                image3.setVisibility(View.INVISIBLE);
                image4.setVisibility(View.INVISIBLE);
        }


    }
}
