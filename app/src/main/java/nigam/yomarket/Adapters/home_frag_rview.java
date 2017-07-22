package nigam.yomarket.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import nigam.yomarket.Posts_activity;
import nigam.yomarket.R;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.imagehelper.ImageLoader;
import nigam.yomarket.utils.apis;

/**
 * Created by alokit nigam on 5/14/2017.
 */

public class home_frag_rview extends RecyclerView.Adapter<home_frag_rview.ViewHolder> {
    View rootView;
    AppCompatActivity activity;
    ArrayList<HomeListGetSet> list;


    public home_frag_rview(AppCompatActivity activity, ArrayList<HomeListGetSet> gridSetter)
    {
        this.activity = activity;
        this.list = gridSetter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_resentposts1, null);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }
    HomeListGetSet hl;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int size = list.size();
         hl =list.get(size-1-position);

        holder.postedby.setText(hl.getPosted_by_name());
        holder.price.setText(hl.getPost_price());
        holder.product.setText(hl.getPost_product());
        holder.city.setText(hl.getPost_city());
        //Log.i("", "onBindViewHolder: "+hl.getPost_quantity()+hl.getPost_price());
///documents/post/post_image_1

        int loader = R.drawable.logo_main;

        String image_url1 =apis.IMAGE_API+hl.getPost_id();



       // http://findyourcampus.com/pulkit/cms/documents/image.php?path=post/102/aa.jpg
        //ImageLoader imgLoader = new ImageLoader(activity);


       // imgLoader.DisplayImage(image_url1+"/1.jpg", loader, holder.pimage);

//        notifyDataSetChanged();

        final ArrayList<String> image_list = new ArrayList<>();
        int count = 0 ;
        if(!hl.getPost_image_1().equalsIgnoreCase("null")){
            count++;
            image_list.add(hl.getPost_image_1());
        }
        if(!hl.getPost_image_2().equalsIgnoreCase("null")){
            count++;
            image_list.add(hl.getPost_image_2());
        }
        if(!hl.getPost_image_3().equalsIgnoreCase("null")){
            count++;
            image_list.add(hl.getPost_image_3());
        }
        if(!hl.getPost_image_4().equalsIgnoreCase("null")){
            count++;
            image_list.add(hl.getPost_image_4());
        }

        final int finalcount=count;



        if(count>0){
            Log.i("homeFrag_adapter", image_url1+"/"+image_list.get(0)+".jpg");

            Glide.with(holder.pimage.getContext())
                    .load(image_url1+"/"+image_list.get(0)+".jpg")
                    .error(loader)
                    .centerCrop()
                    .into(holder.pimage);
        }else{
            Glide.with(holder.pimage.getContext())
                    .load(loader)
                    .error(loader)
                    .centerCrop()
                    .into(holder.pimage);
        }


//    Product,Post_ID_City,Profession,Quantity,Price,Description,Image_1,Image_2,Image_3,Image_4	;

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity,Posts_activity.class);
                Context c=v.getContext();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA",list.get(size-position-1));
                bundle.putInt("count",finalcount);
                bundle.putStringArrayList("list",image_list);
                i.putExtras(bundle);
                c.startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        LinearLayout card;
        ImageView pimage,image2,image3,image4;
        TextView postedby,city,product,price;
        public ViewHolder(final View rView) {
            super(rView);
            postedby= (TextView) rView.findViewById(R.id.posts_posted_by);
            city= (TextView) rView.findViewById(R.id.posts_city_home);
            product= (TextView) rView.findViewById(R.id.posts_product_home);
            price= (TextView) rView.findViewById(R.id.posts_price_home);
            pimage= (ImageView) rView.findViewById(R.id.posts_pic_home);

            card = (LinearLayout) rView.findViewById(R.id.card_home);


        }
    }

}
