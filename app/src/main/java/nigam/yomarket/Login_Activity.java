package nigam.yomarket;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nigam.yomarket.connectivity.ConnectivityReceiver;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;

public class Login_Activity extends AppCompatActivity implements Callback {
EditText email,password;
    Button login;
    ProgressDialog csprogress;
    String emailid,pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        final String MyPREFERENCESa = "notifydata" ;
        SharedPreferences a = getSharedPreferences(MyPREFERENCESa, Context.MODE_PRIVATE);
        SharedPreferences.Editor editora = a.edit();
        editora.putString("id", "0");
        editora.commit();

        email= (EditText) findViewById(R.id.input_id);
        password= (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        csprogress=new ProgressDialog(Login_Activity.this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                csprogress.setMessage("Loading...");
                csprogress.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        csprogress.dismiss();
//whatever you want just you have to launch overhere.

                        dologin();


                    }
                }, 1000);


            }
        });
    }
    private Boolean checkConnection() {
        Log.v("checking....","entered");

        boolean isConnected = ConnectivityReceiver.isConnected(getApplicationContext());
        if (isConnected)
            return true;
        else
            return false;
    }
    private void dologin()
    {
        if (checkConnection())
        {
            if (validate())
            {
                emailid=email.getText().toString();
                pwd=password.getText().toString();
                new back().execute();
            }
        }
        else
            Snackbar.make(findViewById(R.id.layoutlogin),"No Internet Connection!!!",Snackbar.LENGTH_LONG).show();


    }

    private Boolean validate()
    {
        if(email.getText().toString()=="")
        {
            email.setError("FIeld must not be empty");
            return false;
        }
        if(password.getText().toString()=="")
        {
            email.setError("Field must not be empty");
            return false;
        }
           return true;


    }

    public void register(View view) {
        Intent intent = new Intent(this,Register_Activity.class);
        startActivity(intent);
    }

    ProgressDialog progressDialog;
    public void forgotPassword(View view) {
        //Log.e("LOGIN","FORGOT CLICKE");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);


        final EditText edittext = new EditText(this);
        //edittext.setRawInputType(InputType.TYPE_CLASS_PHONE);
        alert.setMessage("Please enter your userid");
        alert.setTitle("Forgot Password");

        alert.setView(edittext);
        alert.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = new ProgressDialog(Login_Activity.this);
                progressDialog.setMessage("Sending password");
                progressDialog.setIndeterminate(true);
                progressDialog.show();

                String userid = edittext.getText().toString();
                new forgotPassword_detail(userid).execute();
                //
            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }



    @Override
    public void onFailure(Call call, IOException e) {
        progressDialog.cancel();
        Toast.makeText(Login_Activity.this, "Network error! Try later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Call call, final okhttp3.Response response) throws IOException {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.e("OTP", response.toString());
                try {
                    progressDialog.cancel();
                    JSONObject object = new JSONObject(response.body().string());
                    String status_st = object.getString("ErrorCode");
                    int error_code = Integer.parseInt(status_st);
                    int code = Integer.valueOf(error_code);
                    switch (code) {
                        case 0:
                            Toast.makeText(Login_Activity.this, "Password SENDed", Toast.LENGTH_SHORT).show();
                            break;

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    class back extends AsyncTask
    {
        String jsonString,response;

        @Override
        protected Object doInBackground(Object[] params) {
            try {
            String baseURL = apis.BASE_API+apis.LOGIN_API+"?id="+emailid+"&password="+pwd ;
            //String baseURL =  apis.local+apis.folder+"login.php?uhid="+UHID+"&phone="+phone ;
//http://122.168.199.236:4589/aiimsone/loginuhid.php?password=ppppp&id=111111111
            jsonString = Utilities.readJson(getBaseContext(), "POST", baseURL);

            JSONObject head = new JSONObject(jsonString);

            response=head.getString("server response");
                Log.i("doInBackground:response",response.toString());

            if(response.toString().equalsIgnoreCase("failure"))
            {
                Snackbar.make(findViewById(R.id.layoutlogin),"Wrong Credentials please try again!!!",Snackbar.LENGTH_INDEFINITE).show();
            }
            else
            {
                Statics.password=pwd;
                Statics.Username=emailid;
                Statics.isLogin=true;

                final String MyPREFERENCES = "logindata" ;
                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("email", emailid);
                editor.putString("password",pwd);
                editor.commit();



                startActivity(new Intent(Login_Activity.this,MainActivity.class));
                Login_Activity.this.finish();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }


    class forgotPassword_detail extends AsyncTask{
        String userid;
        String response;
        String mobileno;
        String password;

        public forgotPassword_detail(String user){
            userid =  user ;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try{
            String url = apis.BASE_API+apis.FORGOT_PASSWORD+userid ;
            String jsonString = Utilities.readJson(getBaseContext(), "GET", url);

            JSONObject head = new JSONObject(jsonString);

            response=head.getString("server response");
            Log.i("forgotPassword",response.toString());


            {
                JSONArray result = head.getJSONArray("server response");
                JSONObject objec = result.getJSONObject(0);
                mobileno = objec.getString("register_mobile_no");
                password = objec.getString("register_password");


            }
        } catch (Exception e) {
            e.printStackTrace();
                Snackbar.make(findViewById(R.id.layoutlogin),"Something wrong!!!",Snackbar.LENGTH_INDEFINITE).show();
        }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(mobileno != null){
                sendUserDetail(mobileno,password);
            }
        }
    }



    private void sendUserDetail(String mobileno,String password){

        String append = "number=91"+mobileno+"&text="+"Your password is "+password ;

        String url = apis.OTP_API+append ;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(20000, TimeUnit.MILLISECONDS)
                .build();

        okHttpClient.newCall(
                new okhttp3.Request.Builder().url(url).build()
        ).enqueue(this);
    }
}
