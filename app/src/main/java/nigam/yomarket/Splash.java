package nigam.yomarket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;

public class Splash extends AppCompatActivity {
String email,pwd;
    ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RelativeLayout rl= (RelativeLayout) findViewById(R.id.rl);
        ImageView iv= (ImageView) findViewById(R.id.imageView);
        final String MyPREFERENCES = "logindata" ;
        SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (pref.contains("email") && pref.contains("password"))
        {
            email= pref.getString("email", null);
            pwd  = pref.getString("password", null);
            Statics.Username=email;
            Statics.password=pwd;
            Statics.isLogin=true;

        }

        checkForPermissions();


        /*new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {

                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                Splash.this.finish();


            }
        }.start();*/
    }

    void startMain(){
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {

                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                Splash.this.finish();


            }
        }.start();
    }


    void checkForPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.CAMERA);
            }
            if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            try {
                if (permissionsList.size() > 0) {
                    String[] mPermissions = new String[permissionsList.size()];
                    mPermissions = permissionsList.toArray(mPermissions);
                    ActivityCompat.requestPermissions(Splash.this, mPermissions, 100);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        startMain();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startMain();

    }
}
