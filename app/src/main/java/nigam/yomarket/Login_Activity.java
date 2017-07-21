package nigam.yomarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nigam.yomarket.connectivity.ConnectivityReceiver;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;

public class Login_Activity extends AppCompatActivity {
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

}
