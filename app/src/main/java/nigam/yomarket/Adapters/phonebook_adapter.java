package nigam.yomarket.Adapters;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import nigam.yomarket.Posts_activity;
import nigam.yomarket.R;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.getset.phonebook;
import nigam.yomarket.imagehelper.ImageLoader;
import nigam.yomarket.utils.apis;

/**
 * Created by alokit nigam on 5/16/2017.
 */

public class phonebook_adapter extends RecyclerView.Adapter<phonebook_adapter.ViewHolder> {
    View rootView;
    AppCompatActivity activity;
    ArrayList<phonebook> list;
    public phonebook_adapter(AppCompatActivity activity, ArrayList<phonebook> gridSetter) {
        this.activity = activity;
        this.list = gridSetter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_phonebook, null);
        phonebook_adapter.ViewHolder holder = new phonebook_adapter.ViewHolder(rootView);

        return holder;
    }
    phonebook ph;
    ImageLoader imgLoader;
    String image_url;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        int size = list.size() ;
        ph = list.get(size-position-1);
        Log.e( " PUL onBindViewHolder: ","name="+ph.getName()+"  contact :"+ph.getContact() );
        holder.name.setText(ph.getName());
        holder.contact.setText(ph.getContact());
        holder.city.setText(ph.getCity());
        holder.profession.setText(ph.getProfession());
        holder.firmname.setText(ph.getFirm_name());
        final String image_url1 = apis.IMAGE_PHONEBOOK+ph.getRegisterid()+"/"+ph.getPic();

        Log.e("PhonebookAdapter",image_url1);
         //imgLoader = new ImageLoader(activity);
        Glide.with(activity).load(image_url1).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).error(R.drawable.logo_main).fitCenter().into(holder.pic);


         /*image_url ="http://simption.com/images/Simption%20Logo.png";
       // imgLoader.DisplayImage(image_url,  R.drawable.logo_main, holder.pic);
        imgLoader.DisplayImage(image_url1, R.drawable.logo_main, holder.pic);*/
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog builder = new Dialog(view.getContext());
                builder.setContentView(R.layout.fullimageview);
                builder.setTitle("Filter");
                ImageView img= (ImageView) builder.findViewById(R.id.fullimage);

               /* String image_url1 =apis.IMAGE_API+data.getPost_id();
                imgLoader.DisplayImage(image_url1+"/3.jpg", R.drawable.logo_main, img);*/
                Glide.with(view.getContext())
                        .load(image_url1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        //.placeholder(R.drawable.logo_main)
                        .into(img);

                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,contact,city,profession,firmname;
        ImageView pic;
        LinearLayout cv;
        public ViewHolder(View itemView)
        {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.phonebook_name);
            contact= (TextView) itemView.findViewById(R.id.phonebook_contact);
            pic= (ImageView) itemView.findViewById(R.id.phonebook_pic);
            cv= (LinearLayout) itemView.findViewById(R.id.card_phone);
            city= (TextView) itemView.findViewById(R.id.phonebook_city);
            profession= (TextView) itemView.findViewById(R.id.phonebook_profession);
            firmname= (TextView) itemView.findViewById(R.id.phonebook_firm);

        }
}
}
