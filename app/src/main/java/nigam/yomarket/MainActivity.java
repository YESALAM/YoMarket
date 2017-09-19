package nigam.yomarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import nigam.yomarket.Adapters.main_tab_adapter;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private main_tab_adapter mSectionsPagerAdapter;
    Menu nav;
    private ViewPager mViewPager;
    NavigationView navigationView ;

    ProgressDialog csprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        csprogress=new ProgressDialog(MainActivity.this);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        boolean firstrun = sharedPreferences.getBoolean("firstrun",true);

        if(firstrun){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstrun",false);
            editor.commit();
        }


        new GetVersion().execute();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        nav=navigationView.getMenu();
        if(!Statics.isLogin){
            nav.findItem(R.id.logout).setVisible(false);
            nav.findItem(R.id.profile).setVisible(false);
        }
        if(Statics.isLogin)
        {
            csprogress.setMessage("Loading...");
            csprogress.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        csprogress.dismiss();
                    }catch (IllegalArgumentException ae){
                        ae.printStackTrace();
                    }
//whatever you want just you have to launch overhere.
                    Log.i( " PUL: run: ","Logged In");
                    if(Utilities.isInternetOn(getBaseContext()))
                    new datafetch().execute();
                    else
                        Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();




                }
            }, 1000);

        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new main_tab_adapter(getSupportFragmentManager(),4);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsaa);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               // mViewPager.setCurrentItem(tab.getPosition());
                Log.e("MainActivity","onTabSelected");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("MainActivity","onTabUnSelected");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("MainActivity","onTabReSelected");
                switch (tab.getPosition()){
                    case 0:
                        Post_Frag post_frag = (Post_Frag) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.container+":"+0);
                        post_frag.refresh();
                        break;
                }
            }
        });


        Intent intent = getIntent();
        boolean refreshRequired = intent.getBooleanExtra("refresh",false);
        if(refreshRequired){
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    Post_Frag post_frag = (Post_Frag) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.container+":"+0);
                    post_frag.refresh();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            MainActivity.this.finish();

        } else if (id == R.id.register) {
            startActivity(new Intent(MainActivity.this,Register_Activity.class));
        } else if (id == R.id.login) {
            startActivity(new Intent(MainActivity.this,Login_Activity.class));
        } else if (id == R.id.post) {
            if(Statics.isLogin)
            {
                startActivity(new Intent(MainActivity.this,post_Activity.class));
                this.finish();
            }
            else
                Snackbar.make(findViewById(R.id.relativelay_for_frags),"Login First To Post",Snackbar.LENGTH_LONG).show();

        } else if (id == R.id.profile) {
            Intent intent = new Intent(this,EditProfileActivity.class);
            //intent.putExtra("editmode",true);
            startActivity(intent);

        } else if (id == R.id.about_us) {
            startActivity(new Intent(MainActivity.this,about_us_Activity.class));

        }else if (id == R.id.contact_us) {
            startActivity(new Intent(MainActivity.this,contact_us_Activity.class));

        }
        else if (id == R.id.logout) {
            Statics.isLogin=false;
            Statics.password=null;
            Statics.Username=null;


            final String MyPREFERENCES = "logindata" ;
            SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            startActivity(new Intent(MainActivity.this,MainActivity.class));
            MainActivity.this.finish();

        } else if(id == R.id.share){
            Intent localIntent1 = new Intent();
            localIntent1.setAction("android.intent.action.SEND");
            localIntent1.putExtra("android.intent.extra.TEXT", "YoMarket is a commodity market platform .\n Don't forget to share with your mates.. \nJust click on the link given below:\n\nhttps://play.google.com/store/apps/details?id=nigam.yomarket\n Don't forget to share with your mates..");
            localIntent1.setType("text/plain");
            startActivity(Intent.createChooser(localIntent1, "Share via:"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    class datafetch extends AsyncTask
    {   JSONObject obj;
        JSONArray data;
        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Log.i("pul ",Statics.Username+"  "+Statics.password);

                String baseURL = apis.BASE_API+apis.DATA_AFTER_LOGIN+"?id="+Statics.Username+"&password="+Statics.password ;
                String jsonString = Utilities.readJson(getApplicationContext(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);


                 data = reader.getJSONArray("server response");
                Log.i(TAG, "doInBackground: "+data.toString());
                 obj = data.getJSONObject(0);
                Log.i("pul ",data.toString());




            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            nav.findItem(R.id.register).setVisible(false);
            nav.findItem(R.id.login).setVisible(false);
            super.onPostExecute(o);
            View hView =  navigationView.getHeaderView(0);
            TextView name= (TextView) hView.findViewById(R.id.vendor_name);
            CircleImageView iv= (CircleImageView) hView.findViewById(R.id.vendor_Image);
            try {

                Statics.id=obj.getString("register_id");
                Statics.name=obj.getString("register_name");
                Statics.city=obj.getString("register_city_type");
                Statics.phone=obj.getString("register_mobile_no");
                Statics.profession=obj.getString("register_profession_type");
                Statics.product=obj.getString("register_product");
                Statics.firmname=obj.getString("register_firm_name");
                String imgname=obj.getString("pic_name");
                String image_url1 =apis.IMAGE_PHONEBOOK+Statics.id+"/"+imgname;
                Log.i(TAG, "onPostExecute: "+imgname);
                //ImageLoader imgLoader = new ImageLoader(getBaseContext());

                Glide.with(MainActivity.this).load(image_url1).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.logo_main).fitCenter().into(iv);
                //imgLoader.DisplayImage(image_url1+".jpg", R.drawable.logo_main, iv);
                String token = FirebaseInstanceId.getInstance().getToken();
                if(token!=null){
                   new notify(token).execute();
                }


                Log.i( "onPostExecute: ", "ID =" + Statics.id);
                name.setText("Welcome: "+obj.getString("register_name"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class notify extends AsyncTask
    {
        String token;
        public notify(String token){
            this.token = token ;
        }


        @Override
        protected Object doInBackground(Object[] params) {
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

    class GetVersion extends AsyncTask {

        int versionPlaystore = 0;


        public GetVersion() {
            super();
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                Log.i("pul ", Statics.Username + "  " + Statics.password);

                String baseURL = apis.BASE_API + apis.VERSION ;
                String jsonString = Utilities.readJson(getApplicationContext(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);

                Log.i(TAG, "doInBackground: " + reader.toString());

                versionPlaystore = reader.getInt("version");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            int currentVersion = 0;
            try {
                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                int verCode = pInfo.versionCode;
                if(verCode<versionPlaystore){
                    AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                    alert.setMessage("App update is available! Please update your app.");
                    alert.setTitle("New Update available");
                    alert.setPositiveButton("update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                           /* String url = "https://play.google.com/store/apps/details?id=nigam.yomarket";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);*/

                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });
                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alert.show();
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }



        }
    }
}
