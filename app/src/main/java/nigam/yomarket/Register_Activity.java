package nigam.yomarket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nigam.yomarket.getset.countrypojo;
import nigam.yomarket.getset.statepojo;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;

import static android.graphics.BitmapFactory.decodeFile;

public class Register_Activity extends AppCompatActivity {
EditText name,email,password,mobile,firm;
    Spinner type,product;
    AutoCompleteTextView country,city,state;
    TextView upload,login;
    Button register;
    ProgressDialog pg;
    private  final String TAG = Register_Activity.class.getSimpleName();
    Bitmap image;
    String Professionlist[] = {"Select Profession","Wholeseller", "Farmer", "Retailer", "Exporter", "Importer", "Commision Agent", "Transporter"};
    String productlist[] = {"Select Product","Fruit", "Vegetables","Transport","fruits and vegetables"};
ImageView imageregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_);
        setTitle("Register");

        pg=new ProgressDialog(this);
        pg.setMessage("Getting you Registered!!! Please Wait ");
        pg.setCancelable(false);
        imageregister= (ImageView) findViewById(R.id.imageregister);
        name = (EditText) findViewById(R.id.registername);
        email = (EditText) findViewById(R.id.registeremail);
        password = (EditText) findViewById(R.id.registerpassword);
        mobile = (EditText) findViewById(R.id.registermobile);
        firm = (EditText) findViewById(R.id.registerfirmname);
        type= (Spinner) findViewById(R.id.registertype);
        product= (Spinner) findViewById(R.id.registerproduct);
        country= (AutoCompleteTextView) findViewById(R.id.registercountry);
        city= (AutoCompleteTextView) findViewById(R.id.registercity);
        state= (AutoCompleteTextView) findViewById(R.id.registerstate);
        upload= (TextView) findViewById(R.id.registeruploadimage);
        login= (TextView) findViewById(R.id.registerlogin);
        register= (Button) findViewById(R.id.registerbtn);

        if (Utilities.isInternetOn(getBaseContext()))
        new country().execute();
        else
            Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.registerspinner, Professionlist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        type.setAdapter(spinnerAdapter);

        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this,  R.layout.registerspinner, productlist);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        product.setAdapter(spinnerAdapter1);


        country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                countrypojo selected = (countrypojo) adapterView.getAdapter().getItem(i);
                if (Utilities.isInternetOn(getBaseContext()))
                new state(selected.getId()).execute(selected);
                else
                    Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

            }
        });
        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                statepojo selected = (statepojo) adapterView.getAdapter().getItem(i);
                if (Utilities.isInternetOn(getBaseContext()))
                new city(selected.getStateid()).execute(selected);
                else
                    Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

            }
        });

        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new googlecity(s).execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register_Activity.this,Login_Activity.class));
            }
        });
        imageregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                builder.setTitle("Upload photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, 1);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, 2);

                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                builder.setTitle("Upload photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, 1);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, 2);

                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name,phone,email,password,firmname,profession,product,country,state,city;
                name=Register_Activity.this.name.getText().toString();
                phone=mobile.getText().toString();
                email=Register_Activity.this.email.getText().toString();
                password=Register_Activity.this.password.getText().toString();
                firmname=firm.getText().toString();
                product=Register_Activity.this.product.getSelectedItem().toString();
                profession=Register_Activity.this.type.getSelectedItem().toString();
                country=Register_Activity.this.country.getText().toString();
                state=Register_Activity.this.state.getText().toString();
                city=Register_Activity.this.city.getText().toString();

                Log.i(TAG, "onClick: cityu"+city+"  name"+name);

/*                if (name.equals("")||state.equals("")||city.equals("")||phone.equals("")||profession.equals("")||
                        country.equals("")||email.equals("")
                        || password.equals("")|| firmname.equals("")|| product.equals(""))*/
                if (name.equals("")||state.equals("")||city.equals("")||phone.equals("")||profession.equals("")||
                        country.equals("")||email.equals("")
                        || password.equals("")|| firmname.equals("")|| product.equals(""))
                {
                    Log.i(TAG, "onClick: cityu"+city+"  name"+name);
                    Toast.makeText(getApplicationContext(),"All Fields are Mandatory",Toast.LENGTH_LONG).show();
                }
                else if (profession.equalsIgnoreCase("Select Profession"))
                {
                    Toast.makeText(getApplicationContext(),"Select Profession",Toast.LENGTH_LONG).show();

                }
                else if (product.equalsIgnoreCase("Select Product"))
                {
                    Toast.makeText(getApplicationContext(),"Select City",Toast.LENGTH_LONG).show();

                }
                if (image==null)
                {
                    Toast.makeText(getApplicationContext(),"Please Upload Image First",Toast.LENGTH_LONG).show();

                }
                else
                {
                    if (Utilities.isInternetOn(getBaseContext()))
                    submit(name,email,password,phone,firmname,profession,product,country,state,city);
                    else
                        Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

                    // new submit(name,email,password,phone,firmname,profession,product,country,state,city).execute();

                }
            }
        });
    }
    public void submit(String... args)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

       pg.show();
        final String name,phone,email,password,firmname,profession,product,country,state,city,image;
        final String[] resp = new String[1];
        name=(args[0]);
        phone=(args[3]);
        password=(args[2]);
        email=(args[1]);
        profession=args[5];
        firmname=(args[4]);
        product=(args[6]);
        country=(args[7]);
        state=(args[8]);
        city=(args[9]);
        image=Utilities.getStringImage(this.image);
        final ArrayList<HashMap<String,String>> datalist=new ArrayList<>();
        String url = apis.BASE_API+apis.REGISTER1;
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: "+response.toString());
                try {
                    JSONObject head = new JSONObject(response);
                    resp[0] =head.getString("server response");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(resp[0].toString().equalsIgnoreCase("Registered sucessfully"))
                {
                    Toast.makeText(getApplicationContext(),"Registered!!!",Toast.LENGTH_LONG).show();
                    Statics.isLogin=true;
                    Statics.Username=Register_Activity.this.email.getText().toString();
                    Statics.password=Register_Activity.this.password.getText().toString();
                    pg.dismiss();

                    startActivity(new Intent(Register_Activity.this,MainActivity.class));
                    Register_Activity.this.finish();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error submitting data",Toast.LENGTH_LONG).show();
                    pg.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            /*String params="name="+name+"&email="+email+"&password="+password+"&mobile_no="+phone+
                        "&firm_name="+firmname+"&profession_type="+profession+
                        "&product="+ product+"&country="+country+"&state="+state+"&city="+city+"&image="+encoded;*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("name", name);
                params.put("email",email);
                params.put("password", password);
                params.put("mobile_no",phone);
                params.put("firm_name", firmname);
                params.put("profession_type",profession);
                params.put("product", product);
                params.put("country",country);
                params.put("state", state);
                params.put("city",city);
                params.put("image", image);

                return params;
            }
        };

        requestQueue.add(req);
    }
    class submit extends AsyncTask
    {
        String name,phone,email,password,firmname,profession,product,country,state,city;
        String jsonString,response;
        public submit(String... args) {
            super();
            //pg.show();
            this.name=Utilities.replaceSpaceInString(args[0]);
            phone=Utilities.replaceSpaceInString(args[3]);
            password=Utilities.replaceSpaceInString(args[2]);
            email=Utilities.replaceSpaceInString(args[1]);
            profession=Utilities.replaceSpaceInString(args[5]);
            firmname=Utilities.replaceSpaceInString(args[4]);
            product=Utilities.replaceSpaceInString(args[6]);
            country=Utilities.replaceSpaceInString(args[7]);
            state=Utilities.replaceSpaceInString(args[8]);
            city=Utilities.replaceSpaceInString(args[9]);
        }
        @Override
        protected Object doInBackground(Object[] objects) {


            ByteArrayOutputStream output = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, output);
            byte[] byteArray = output.toByteArray();
            String encoded = URLEncoder.encode(Base64.encodeToString(byteArray, Base64.DEFAULT));
            Log.i(TAG, "doInBackground: dataaaa"+encoded);
            try {
                String params="name="+name+"&email="+email+"&password="+password+"&mobile_no="+phone+
                        "&firm_name="+firmname+"&profession_type="+profession+
                        "&product="+ product+"&country="+country+"&state="+state+"&city="+city+"&image="+encoded;
                String baseURL1 = apis.BASE_API+apis.REGISTER1;
                String baseURL = apis.BASE_API+apis.REGISTER1+"?name="+name+"&email="+email+
                        "&password="+password+"&mobile_no="+phone+"&firm_name="+firmname+"&profession_type="+profession+
                        "&product="+ product+"&country="+country+"&state="+state+"&city="+city
                        +"&image="+encoded;
                Log.i("doInBackground:response",baseURL);
               // jsonString = Utilities.readJson(getBaseContext(), "GET", baseURL);
               //  jsonString = Utilities.sendData(baseURL1,"GET", params);
                jsonString=Utilities.sendDataRaw(baseURL1,"GET",params);
                JSONObject head = new JSONObject(jsonString);

                response=head.getString("server response");
                Log.i("doInBackground:response",baseURL);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(response.toString().equalsIgnoreCase("Registered sucessfully"))
            {
                pg.dismiss();
                Toast.makeText(getApplicationContext(),"Registered!!!",Toast.LENGTH_LONG).show();
                Statics.isLogin=true;
                Statics.Username=Register_Activity.this.email.getText().toString();
                Statics.password=Register_Activity.this.password.getText().toString();

                startActivity(new Intent(Register_Activity.this,MainActivity.class));
                Register_Activity.this.finish();
            }
            else
                {
                Toast.makeText(getApplicationContext(),"Error submitting data",Toast.LENGTH_LONG).show();
            }
            Log.i("doInBackground:response",response.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
               /* Uri img=data.getData();
                image= ImageHelper.loadSizeLimitedBitmapFromUri(img,getContentResolver());
                upload.setText("image uploaded");
                imageregister.setImageBitmap(image);
*/

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                image=BitmapFactory.decodeFile(picturePath);
                imageregister.setImageBitmap(image);



            } else if (requestCode == 1) {

                try {
                    File filea = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                    image = decodeSampledBitmapFromFile(filea.getAbsolutePath(), 1000, 700);


                    imageregister.setImageBitmap(image);
                    upload.setText("image uploaded");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    class country extends AsyncTask
    {
ArrayList<countrypojo> countrylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            countrylist=new ArrayList<>();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                String baseURL = apis.BASE_API+apis.COUNTRIES ;
                Log.i(TAG, "doInBackground: "+baseURL);

                String jsonString = Utilities.readJson(getBaseContext(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");
                for (int i = 0; i < data.length(); i++)
                {
                    countrypojo ps=new countrypojo();
                    JSONObject obj = data.getJSONObject(i);
                    ps.setCountry(obj.getString("name"));
                    ps.setId(obj.getString("id"));

                    Log.i(TAG, "doInBackground: "+obj.getString("name"));

                    countrylist.add(ps);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayAdapter<countrypojo> adapter = new ArrayAdapter(getBaseContext(), R.layout.registerspinner1,  countrylist);
            //Getting the instance of AutoCompleteTextView
            country.setThreshold(1);//will start working from first character
            //setting the adapter data into the AutoCompleteTextView
            // Log.e(TAG, "pul: setShopAdapter: list:"+shopNameList.toString() );

            country.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }
    class state extends AsyncTask
    {
        public state(String id) {
            super();
            this.id=id;
        }
        ArrayList<statepojo> statelist=new ArrayList<>();
        String id;
        @Override
        protected Object doInBackground(Object[] objects) {



            try {
                String baseURL = apis.BASE_API+apis.STATE+"?countryid="+id ;
                Log.i(TAG, "doInBackgroundstate: "+baseURL);

                String jsonString = Utilities.readJson(getBaseContext(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");
                for (int i = 0; i < data.length(); i++)
                {
                    statepojo ps=new statepojo();
                    JSONObject obj = data.getJSONObject(i);
                    ps.setState(obj.getString("name"));
                    ps.setStateid(obj.getString("id"));

                    Log.i(TAG, "doInBackgroundstate: "+obj.getString("name"));

                    statelist.add(ps);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayAdapter<statepojo> adapter = new ArrayAdapter(getBaseContext(), R.layout.registerspinner1,  statelist);
            //Getting the instance of AutoCompleteTextView
            state.setThreshold(1);//will start working from first character
            //setting the adapter data into the AutoCompleteTextView
            // Log.e(TAG, "pul: setShopAdapter: list:"+shopNameList.toString() );


            state.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    class city extends AsyncTask
    {

        String id;
        ArrayList<String> citylist=new ArrayList<>();
        public city(String id) {
            super();
            this.id=id;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String baseURL = apis.BASE_API+apis.CITIES+"?stateid="+id ;
                Log.i(TAG, "doInBackgroundstate: "+baseURL);

                String jsonString = Utilities.readJson(getBaseContext(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");
                for (int i = 0; i < data.length(); i++)
                {

                    JSONObject obj = data.getJSONObject(i);

                    Log.i(TAG, "doInBackgroundstate: "+obj.getString("name"));

                    citylist.add(obj.getString("name"));
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayAdapter<String> adapter = new ArrayAdapter(getBaseContext(), R.layout.registerspinner1,  citylist);
            //Getting the instance of AutoCompleteTextView
            city.setThreshold(1);//will start working from first character
            //setting the adapter data into the AutoCompleteTextView
            // Log.e(TAG, "pul: setShopAdapter: list:"+shopNameList.toString() );


            city.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    class googlecity extends AsyncTask
    {

        CharSequence id;
        ArrayList<String> citylist=new ArrayList<>();
        public googlecity(CharSequence id) {
            super();
            this.id=id;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                //String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&sensor=false&key="+apis.API+"&input=";
                String baseURL = apis.PLACE_API+id ;
                //String baseURL = apis.BASE_API+apis.CITIES+"?stateid="+id ;
                Log.i(TAG, "doInBackgroundstate: "+baseURL);

                String jsonString = Utilities.readJson(getBaseContext(), "GET", baseURL);
                JSONObject reader = new JSONObject(jsonString);
                String status = reader.getString("status");
                if(status.equalsIgnoreCase("OK")) {

                    JSONArray data = reader.getJSONArray("predictions");
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject obj = data.getJSONObject(i);

                        String city_desc = obj.getString("description");
                        Log.i(TAG, "doInBackgroundstate: " + city_desc);
                        citylist.add(city_desc);
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayAdapter<String> adapter = new ArrayAdapter(getBaseContext(), R.layout.registerspinner1,  citylist);
            //Getting the instance of AutoCompleteTextView
            city.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String prediction = citylist.get(position);
                    String main = prediction.substring(0,prediction.indexOf(','));
                    city.setText(main);
                }
            });
            city.setThreshold(1);//will start working from first character
            //setting the adapter data into the AutoCompleteTextView
            // Log.e(TAG, "pul: setShopAdapter: list:"+shopNameList.toString() );

            city.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
