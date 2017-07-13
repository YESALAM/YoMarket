package nigam.yomarket;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import nigam.yomarket.Adapters.main_frag_rview;
import nigam.yomarket.Adapters.phonebook_adapter;
import nigam.yomarket.getset.HomeListGetSet;
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
                layout.setVisibility(View.VISIBLE);
                String cityselected = String.valueOf(cityactv.getText());
                if (Utilities.isInternetOn(getActivity()))
                    new datafilter(productselected,cityselected).execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_SHORT).show();
            }
        });
        cityactv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityselected = true ;
                String cityselected=adapterView.getItemAtPosition(i).toString();
                if (Utilities.isInternetOn(getActivity()))
                new datafilter(productselected,cityselected).execute();
                else
                    Toast.makeText(getActivity(),"No Internet Commection!!!",Toast.LENGTH_LONG).show();


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

        @Override
        protected Object doInBackground(Object[] params) {
            list.clear();
            try {
                String baseURL = apis.BASE_API + apis.PHONEBOOK_API;
                String jsonString = Utilities.readJson(getActivity(), "POST", baseURL);
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
                    ps.setRegisterid(obj.getString("phonebook_main_id"));
                    ps.setFirm_name(obj.getString("phonebook_firm_name"));

                    Log.i( "doInBackground: ",""+obj.getString("phonebook_main_id")+"  "+obj.getString("phonebook_name"));
                    list.add(ps);
//                    adapter.notifyDataSetChanged();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter=new phonebook_adapter((AppCompatActivity) getActivity(),list);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }
    class datafilter extends AsyncTask
    {
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
                if (!cityselected) city = "All" ;
                String baseURL = apis.BASE_API + apis.FILTER_PHONEBOOK+"?city="+city+"&product="+product;
                Log.i(this.getClass().getSimpleName(), "doInBackgroundtesting:  "+baseURL);

                String jsonString = Utilities.readJson(getActivity(), "GET", baseURL);
                Log.e(this.getClass().getSimpleName(),jsonString);
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
                    Log.i( "doInBackground: ",""+obj.getString("phonebook_main_id")+"  "+obj.getString("phonebook_name"));
                    list.add(ps);
//                    adapter.notifyDataSetChanged();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter=new phonebook_adapter((AppCompatActivity) getActivity(),list);
            Log.i("", "doInBackgroundtesting: post ");

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rv.setHasFixedSize(true);
            rv.setLayoutManager(mLayoutManager);
            //rv.addItemDecoration(new DividerItemDecoration(getActivity()));
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setAdapter(adapter);
        }
    }

    class city extends AsyncTask{
        ArrayList<String> citylist = new ArrayList<>();

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                String baseURL = apis.BASE_API + apis.PHONEBOOKCITY ;
                Log.i(this.getClass().getSimpleName(), "doInBackgroundtesting:  "+baseURL);

                String jsonString = Utilities.readJson(getActivity(), "GET", baseURL);
                Log.e(this.getClass().getSimpleName(),jsonString);
                JSONObject reader = new JSONObject(jsonString);

                JSONArray data = reader.getJSONArray("server response");


                for (int i = 0; i < data.length(); i++)                {

                    JSONObject obj = data.getJSONObject(i);
                    String city = obj.getString("phonebook_city");
                    Log.i(this.getClass().getSimpleName(),city);
                    citylist.add(city);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            as=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, citylist);
            as.setDropDownViewResource(R.layout.registerspinner);
            Log.i("", "doInBackgroundtesting: post ");
            cityactv.setAdapter(as);
        }
    }

}
