package nigam.yomarket;


import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import nigam.yomarket.Adapters.WrapLinearLayoutManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import nigam.yomarket.Adapters.phonebook_adapter;
import nigam.yomarket.getset.phonebook;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.Utilities;
import nigam.yomarket.utils.apis;


/**
 * A simple {@link Fragment} subclass.
 */
public class phonebook_frag extends Fragment {

    View v;
    String productselected;
    phonebook_adapter adapter;
    RecyclerView rv;
    ArrayList<phonebook> list =new ArrayList();
    String productlist[]={"All","Fruit","Vegetables","fruits and vegetables","transport"};
    String Professionlist[]={"Wholeseller","Farmer","Retailer","Exporter","Importer","Commision Agent","Transporter"};
    boolean cityselected = false ;
    AutoCompleteTextView cityactv;
    ArrayAdapter<String> as;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_phonebook_frag, container, false);
        rv= (RecyclerView) v.findViewById(R.id.phonebook_recycler);
        final RadioButton fruit= (RadioButton) v.findViewById(R.id.fruit);
        final RadioButton fruitvegetables= (RadioButton) v.findViewById(R.id.fruitvegitables);
        final RadioButton vegetables= (RadioButton) v.findViewById(R.id.vegetables);
        final RadioButton transport= (RadioButton) v.findViewById(R.id.transport);
        RadioGroup group = (RadioGroup) v.findViewById(R.id.radioGroup);
        final TextInputLayout layout= (TextInputLayout) v.findViewById(R.id.citytextInputLayout);
        cityactv = (AutoCompleteTextView) v.findViewById(R.id.city);

        if (Utilities.isInternetOn(getActivity()))
        new data().execute();
        else
            Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();


        layout.setVisibility(View.GONE);
        new city().execute();
        as=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, Arrays.asList(Statics.citylist));
        as.setDropDownViewResource(R.layout.registerspinner);
        cityactv.setAdapter(as);
        cityactv.setThreshold(1);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                if (fruit.isChecked())
                {
                 productselected=productlist[1];
                }
                 if (fruitvegetables.isChecked())
                {
                    productselected=productlist[3];
                }
                 if (vegetables.isChecked())
                {
                    productselected=productlist[2];
                }
                if(transport.isChecked())
                {
                    productselected=productlist[4];
                }

                Log.e("phonebook","product selected");

               try{
                   String text = String.valueOf(cityactv.getText());
                   if(text.equalsIgnoreCase("")) cityselected = false ;
               }catch (Exception e){}



                layout.setVisibility(View.VISIBLE);
                String city_selected = String.valueOf(cityactv.getText());
                if (Utilities.isInternetOn(getActivity()))
                    new datafilter(productselected,city_selected).execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_SHORT).show();
            }
        });
        cityactv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityselected = true ;
                String prediction = adapterView.getItemAtPosition(i).toString();
                int comma_index = prediction.indexOf(',');
                String main = prediction ;
                if (comma_index > 0) {
                    main = prediction.substring(0, comma_index);
                    cityactv.setText(main);
                }


                String cityselected = main;  //adapterView.getItemAtPosition(i).toString();
                if (Utilities.isInternetOn(getActivity()))
                new datafilter(productselected,cityselected).execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();


            }
        });



        cityactv.addTextChangedListener(new TextWatcher() {
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


        return v;
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        inflater.inflate(R.menu.frag_home, menu);
//        return;
//    }
    //String productlist[] = {"All","Fruit", "Vegetables", "fruits and vegetables"};

    private void filter()
    {
        final Dialog builder = new Dialog(getActivity());
        builder.setContentView(R.layout.filterpfonebookview);
        final Button filter= (Button) builder.findViewById(R.id.filterphonebookfilter);
        Button clearfilter= (Button) builder.findViewById(R.id.filterphonebookclearfilter);
        final Spinner product= (Spinner) builder.findViewById(R.id.filterphonebookspinner);
        final Spinner city= (Spinner) builder.findViewById(R.id.filterphonebookcity);

        ArrayAdapter<String> as=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, Statics.citylist);
        as.setDropDownViewResource(R.layout.registerspinner);
        city.setAdapter(as);
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
                String productfilter,cityfilter;
                if (product.getSelectedItem().equals("All"))
                    productfilter="is NOT NULL";
                else
                    productfilter=product.getSelectedItem().toString();

                if (city.getSelectedItem().equals("All"))
                    cityfilter="is NOT NULL";
                else
                    cityfilter=city.getSelectedItem().toString();

                if (Utilities.isInternetOn(getActivity()))
                new datafilter(productfilter,cityfilter).execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();

                builder.dismiss();
            }
        });
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, productlist);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        product.setAdapter(spinnerAdapter1);

        builder.show();
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        Intent i;
//        switch (item.getItemId()) {
//            case R.id.filter:
//                filter();
//                return true;
//
//
//        }
//        return onOptionsItemSelected(item);
//    }

    class data extends AsyncTask
    {
        ArrayList<phonebook> list =new ArrayList();

        @Override
        protected Object doInBackground(Object[] params) {
            list.clear();
            try {
                String baseURL = apis.BASE_API + apis.PHONEBOOK_API;
                String jsonString = Utilities.readJson(getActivity(), "POST", baseURL);
                JSONObject reader = new JSONObject(jsonString);
                Log.i("PhonebookFrag_data",reader.toString());
                JSONArray data = reader.getJSONArray("server response");
                for (int i = 0; i < data.length(); i++)
                {
                    phonebook ps=new phonebook();
                    JSONObject obj = data.getJSONObject(i);

                    ps.setCity(obj.getString("phonebook_city"));
                    ps.setContact(obj.getString("phonebook_contact"));
                    ps.setName(obj.getString("phonebook_name"));
                    ps.setProduct(obj.getString("phonebook_product"));
                    ps.setProfession(obj.getString("phonebook_profession"));
                    ps.setPic(obj.getString("phonebook_pic"));
                    ps.setRegisterid(obj.getString("phonebook_main_id"));
                    ps.setFirm_name(obj.getString("phonebook_firm_name"));

                    //Log.i( "doInBackground: ",""+obj.getString("phonebook_main_id")+"  "+obj.getString("phonebook_name"));
                    list.add(ps);
//                    adapter.notifyDataSetChanged();
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
            adapter=new phonebook_adapter((AppCompatActivity) getActivity(),list);
            RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getContext());
            //rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
            //rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }
    class datafilter extends AsyncTask
    {
        ArrayList<phonebook> list =new ArrayList();
        String product;String city;
        public datafilter(String product,String city) {
            super();
            this.city=Utilities.replaceSpaceInString(city);
            this.product=Utilities.replaceSpaceInString(product);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            list.clear();
            try {
                if (!cityselected ) city = "All" ;
                String baseURL = apis.BASE_API + apis.FILTER_PHONEBOOK+"?city="+city+"&product="+product;


                String jsonString = Utilities.readJson(getActivity(), "GET", baseURL);
                Log.i("Phonebook_Frag_filter", "From "+baseURL+" "+jsonString.toString());
                JSONObject reader = new JSONObject(jsonString);


                JSONArray data = reader.getJSONArray("server response");
                for (int i = 0; i < data.length(); i++)
                {
                    phonebook ps=new phonebook();
                    JSONObject obj = data.getJSONObject(i);

                    ps.setCity(obj.getString("phonebook_city"));
                    ps.setContact(obj.getString("phonebook_contact"));
                    ps.setName(obj.getString("phonebook_name"));
                    ps.setProduct(obj.getString("phonebook_product"));
                    ps.setProfession(obj.getString("phonebook_profession"));
                    ps.setPic(obj.getString("phonebook_pic"));
                    ps.setFirm_name(obj.getString("phonebook_firm_name"));
                    ps.setRegisterid(obj.getString("phonebook_main_id"));
                    //Log.i( "doInBackground: ",""+obj.getString("phonebook_main_id")+"  "+obj.getString("phonebook_name"));
                    list.add(ps);
//                    adapter.notifyDataSetChanged();
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
            adapter=new phonebook_adapter((AppCompatActivity) getActivity(),list);
            //Log.i("", "doInBackgroundtesting: post ");

            RecyclerView.LayoutManager mLayoutManager = new WrapLinearLayoutManager(getContext());
            //rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
            //rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }

    class city extends AsyncTask{
        ArrayList<String> citylist = new ArrayList<>();

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String baseURL = apis.BASE_API + apis.PHONEBOOKCITY ;


                String jsonString = Utilities.readJson(getActivity(), "GET", baseURL);
                Log.i("PhonebookFrag_city", "From "+baseURL+" "+jsonString);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");


                for (int i = 0; i < data.length(); i++)                {

                    JSONObject obj = data.getJSONObject(i);
                    String city = obj.getString("phonebook_city");
                    //Log.i(this.getClass().getSimpleName(),city);
                    citylist.add(city);
                }
            }catch (Exception e)
            {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            as=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, citylist);
            as.setDropDownViewResource(R.layout.registerspinner);
            //Log.i("", "doInBackgroundtesting: post ");
            cityactv.setAdapter(as);
        }
    }

    class googlecity extends AsyncTask {

        String TAG = "phonebook_googlecity";
        CharSequence id;
        ArrayList<String> citylist = new ArrayList<>();

        public googlecity(CharSequence id) {
            super();
            this.id = id;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                //String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&sensor=false&key="+apis.API+"&input=";
                String baseURL = apis.PLACE_API + id;
                //String baseURL = apis.BASE_API+apis.CITIES+"?stateid="+id ;
                Log.i(TAG, "doInBackgroundstate: " + baseURL);

                String jsonString = Utilities.readJson(getActivity(), "GET", baseURL);
                JSONObject reader = new JSONObject(jsonString);
                String status = reader.getString("status");
                if (status.equalsIgnoreCase("OK")) {

                    JSONArray data = reader.getJSONArray("predictions");
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject obj = data.getJSONObject(i);

                        String city_desc = obj.getString("description");
                        Log.i(TAG, "doInBackgroundstate: " + city_desc);
                        citylist.add(city_desc);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Context context = getContext();
            if (context != null) {
                ArrayAdapter adapter = new ArrayAdapter(context, R.layout.registerspinner1, citylist);
            //Getting the instance of AutoCompleteTextView
            /*cityactv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String prediction = citylist.get(position);
                    String main = prediction.substring(0, prediction.indexOf(','));
                    cityactv.setText(main);
                }
            });*/
            cityactv.setThreshold(1);//will start working from first character
            //setting the adapter data into the AutoCompleteTextView
            // Log.e(TAG, "pul: setShopAdapter: list:"+shopNameList.toString() );

            cityactv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
