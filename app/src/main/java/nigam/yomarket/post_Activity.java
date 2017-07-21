package nigam.yomarket;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;


/**
 * A simple {@link Fragment} subclass.
 */
public class post_Activity extends AppCompatActivity {

    String TAG = "post_Activity" ;

    String imageviewselected;
    ImageView pic1,pic2,pic3,pic4;
    EditText Quantity,price,Discription;
    AutoCompleteTextView city;
    Spinner product,profession;
    Button post;
    Bitmap image;
    String image1,image2,image3,image4;
    ProgressDialog pg;
    ArrayAdapter<String> cityAdapter, professionadapter,productadapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_frag);
        city= (AutoCompleteTextView) findViewById(R.id.post_city_tv);
        product = (Spinner) findViewById(R.id.spinner_post_product);
        profession= (Spinner) findViewById(R.id.spinner_post_profession);
        pg=new ProgressDialog(this);
        pg.setMessage("Posting!!!");
        pg.setCancelable(false);
        String productlist[]={"Fruit","Vegetables","fruits and vegetables","transport"};
        String Professionlist[]={"Wholeseller","Farmer","Retailer","Exporter","Importer","Commision Agent","Transporter"};

        //Quantity = (EditText) findViewById(R.id.post_quantity);
        price = (EditText)findViewById(R.id.post_price);
        Discription=(EditText)findViewById(R.id.post_discription);


        pic1 = (ImageView) findViewById(R.id.post_pic1);
        pic2 = (ImageView) findViewById(R.id.post_pic2);
        pic3 = (ImageView) findViewById(R.id.post_pic3);
        pic4 = (ImageView) findViewById(R.id.post_pic4);

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewselected="pic1";
                getImage(pic1);
            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewselected="pic2";

                getImage(pic2);
            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewselected="pic3";

                getImage(pic3);
            }
        });
        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageviewselected="pic4";

                getImage(pic4);
            }
        });

        image1 = "null" ;
        image2 = "null" ;
        image3 = "null" ;
        image4 = "null" ;


        city.clearFocus();
        cityAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,Statics.citylist);
        city.setAdapter(cityAdapter);
        city.setThreshold(1);
        city.setText(Statics.city);
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



        productadapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,productlist);
        product.setAdapter(productadapter);
        int posti = Arrays.asList(productlist).indexOf(Statics.product);
        product.setSelection(posti);

        professionadapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,Professionlist);
        int position = Arrays.asList(Professionlist).indexOf(Statics.profession);
        profession.setAdapter(professionadapter);
        profession.setSelection(position);

        post = (Button) findViewById(R.id.post_button);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


    }
    private void validate()
    {

        String producta,professiona,citya,quantitya,pricea,disc;
        producta = product.getSelectedItem().toString();
        professiona = profession.getSelectedItem().toString();
        citya=city.getText().toString();
        //quantitya=Quantity.getText().toString();
        pricea=price.getText().toString();
        disc=Discription.getText().toString();

        //if (citya.equalsIgnoreCase("") || producta.equalsIgnoreCase("") || professiona.equalsIgnoreCase("") || pricea.equalsIgnoreCase("")  || disc.equalsIgnoreCase("") )
        if (citya.equalsIgnoreCase("") || producta.equalsIgnoreCase("") || professiona.equalsIgnoreCase("") )
        {
            Toast.makeText(getApplicationContext(),"All Fielda are Mandatory",Toast.LENGTH_LONG).show();
            return;
        }
        else
            /*if(image1.equalsIgnoreCase("null")) {
                Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (Utilities.isInternetOn(getBaseContext()))
            postata();
            else
                Toast.makeText(getBaseContext(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();
                return;

        //new postdata().execute();
    }

    private void getImage(ImageView image)
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(post_Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAPTURE_IMAGE);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, PICK_IMAGE);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                image=BitmapFactory.decodeFile(picturePath);


                setImageInView(image);
            }
            else if (requestCode == CAPTURE_IMAGE) {

                try {
                    File filea = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                    image = decodeSampledBitmapFromFile(filea.getAbsolutePath(), 1000, 700);

                    setImageInView(image);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    public String getStringImage(Bitmap bmpa){
        Bitmap bmp=Utilities.compressImage(bmpa);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void setImageInView(Bitmap bb)
    {   Bitmap b=bb;
        if (imageviewselected.equalsIgnoreCase("pic1"))
        {
            image1=getStringImage(b);
            pic1.setImageBitmap(b);
        }

        if (imageviewselected.equalsIgnoreCase("pic2"))
        {
            image2=getStringImage(b);
            pic2.setImageBitmap(b);
        }

        if (imageviewselected.equalsIgnoreCase("pic3"))
        {
            pic3.setImageBitmap(b);
            image3=getStringImage(b);
        }



        if (imageviewselected.equalsIgnoreCase("pic4"))
        {
            image4=getStringImage(b);

            pic4.setImageBitmap(b);

        }

    }

    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.postfrag, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_item:
                validate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void postata()
    {
        pg.show();
        final String producta,professiona,citya,quantitya,pricea,disc;
        final String[] resp= new String[1];;
        Log.i( "onResponse: ","uploading");
        producta = product.getSelectedItem().toString();
        professiona = profession.getSelectedItem().toString();
        citya=city.getText().toString();
        quantitya="";//Quantity.getText().toString();
        pricea=price.getText().toString();
        disc=Discription.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = apis.BASE_API+apis.POST_API1;
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i( "onResponse: ",response.toString());
                try {
                    JSONObject head = new JSONObject(response);
                    resp[0] =head.getString("server response");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(resp[0].toString().equalsIgnoreCase("post sucessfull"))
                {
                    Toast.makeText(getApplicationContext(),"Posted!!!",Toast.LENGTH_LONG).show();
                    post_Activity.this.onBackPressed();

                    pg.dismiss();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error submitting data",Toast.LENGTH_LONG).show();
                    pg.dismiss();

                }
                pg.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pg.dismiss();
                //Toast.makeText(getApplicationContext(),"Image Too large!!! try again",Toast.LENGTH_LONG).show();
            }
        }){

           /*tring baseURL = apis.BASE_API+apis.POST_API+"?product="+producta+"&profession="+professiona+
                        "&city="+citya+"&quantity="+quantitya+"&price="+pricea+"&discription="+disc+"&post_id="+Statics.id;
                      //  +"&image1="+image1+"&image2="+image2+"&image3="+image3+"&image4="+image4;*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("product", producta);
                params.put("profession",professiona);
                params.put("city", citya);
                params.put("quantity",quantitya);
                params.put("price", pricea);
                params.put("discription",disc);
                params.put("post_id", Statics.id);
                params.put("image1",image1);
                params.put("image2", image2);
                params.put("image3",image3);
                params.put("image4", image4);
                params.put("name",Statics.name);
                params.put("date",getdate());
                params.put("time",gettime());

                return params;
            }
        };

        requestQueue.add(req);

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
        String formattedDate = dateFormat.format(new Date());
        Log.i( "gettime: ",formattedDate);
        return formattedDate;
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
