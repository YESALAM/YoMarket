package nigam.yomarket.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nigam.yomarket.Posts_activity;
import nigam.yomarket.R;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.imagehelper.ImageLoader;
import nigam.yomarket.post_Activity;
import nigam.yomarket.utils.Statics;
import nigam.yomarket.utils.apis;

import static nigam.yomarket.utils.Utilities.replaceSpaceInString;

/**
 * Created by alokit nigam on 5/14/2017.
 */

public class main_frag_rview extends RecyclerView.Adapter<main_frag_rview.ViewHolder> {
    View rootView;
    AppCompatActivity activity;
    ArrayList<HomeListGetSet> list;


    public main_frag_rview(AppCompatActivity activity, ArrayList<HomeListGetSet> gridSetter)
    {
        this.activity = activity;
        this.list = gridSetter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_resentposts, null);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }
    HomeListGetSet hl;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final int size = list.size();
         hl =list.get(size-position-1);

        holder.postedby.setText(hl.getPosted_by_name());
        holder.city.setText(hl.getPost_city());
        holder.mobileno.setText(hl.getMobile_no());
        holder.profession.setText(hl.getPost_profession());
        holder.date_time.setText(hl.getDate()+" "+hl.getTime()+"   ");



        String price = hl.getPost_price().trim();
        if (price.equalsIgnoreCase("")) {
            holder.price.setVisibility(View.GONE);
            holder.price_tt.setVisibility(View.GONE);
        } else {
            holder.price.setVisibility(View.VISIBLE);
            holder.price_tt.setVisibility(View.VISIBLE);
            holder.price.setText(hl.getPost_price());
        }


        String desc = hl.getPost_description().trim();
        if (desc.equalsIgnoreCase("")) {
            holder.description.setVisibility(View.GONE);
            holder.desc_tt.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.desc_tt.setVisibility(View.VISIBLE);
            holder.description.setText(hl.getPost_description());
        }







        //Log.i(this.getClass().getSimpleName(), "onBindViewHolder: "+hl.getPost_quantity()+hl.getPost_price());
///documents/post/post_image_1
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

        showImage(count,image_list,hl.getPost_id(),holder);

        final int finalcount=count;

        /*int loader = R.drawable.logo_main;

        //String image_url ="http://simption.com/images/Simption%20Logo.png";
        String image_url1 =apis.IMAGE_API+hl.getPost_id();

        //Log.e("", "onBindViewHolder: "+image_url);
        Log.e("", "onBindViewHolder: "+image_url1);
       // http://findyourcampus.com/pulkit/cms/documents/image.php?path=post/102/aa.jpg
        ImageLoader imgLoader = new ImageLoader(activity);


        imgLoader.DisplayImage(image_url1+"/1.jpg", loader, holder.pimage);
        imgLoader.DisplayImage(image_url1+"/2.jpg", loader, holder.image2);
        imgLoader.DisplayImage(image_url1+"/3.jpg", loader, holder.image3);
        imgLoader.DisplayImage(image_url1+"/4.jpg", loader, holder.image4);*/


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
        LinearLayout image_holder;
        LinearLayout card;
        //ImageView pimage,image2,image3,image4;
        TextView postedby, city, description, price, mobileno, profession;
        TextView desc_tt,price_tt,date_time;
        public ViewHolder(final View rView) {
            super(rView);
            postedby= (TextView) rView.findViewById(R.id.posts_posted_by);
            city= (TextView) rView.findViewById(R.id.posts_city_home);
            description = (TextView) rView.findViewById(R.id.posts_product_home);
            price= (TextView) rView.findViewById(R.id.posts_price_home);
            mobileno = (TextView) rView.findViewById(R.id.posts_mobileno_home);
            profession = (TextView) rView.findViewById(R.id.posts_profession_home);

            desc_tt = (TextView) rView.findViewById(R.id.desc);
            price_tt = (TextView) rView.findViewById(R.id.price);

            date_time = (TextView) rView.findViewById(R.id.post_date_time);
            //image_holder = (LinearLayout) rView.findViewById(R.id.image_holder);
            card = (LinearLayout) rView.findViewById(R.id.card_home);


        }
    }


    private void showImage(int count,ArrayList<String> list,String postid,ViewHolder viewHolder){
        Log.i("MainFrag_adapter",count+" "+postid);
        ViewGroup parent = viewHolder.card ;
        View view = parent.findViewById(R.id.image_holder);
        if(view !=null) parent.removeView(view);
        /*ViewGroup parent = (ViewGroup) hol.getParent();
        int index = parent.indexOfChild(hol);
        if(parent != null) parent.removeView(hol);*/

        //parent.removeViewAt(index);
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        View hol = null ;

        switch (count){
            case 0:
                hol = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image,parent,false);
                imageView1 = (ImageView) hol.findViewById(R.id.posts_pic_home);
                Glide.with(activity)
                        .load(R.drawable.logo_main)
                        .error(R.drawable.logo_main)
                        .fitCenter()
                        .into(imageView1);
                break;

            case 1:
                hol = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_image,parent,false);
                imageView1 = (ImageView) hol.findViewById(R.id.posts_pic_home);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(0)+".jpg")
                        .error(R.drawable.logo_main)
                        .centerCrop()
                        .into(imageView1);
                break;
            case 2:
                hol = LayoutInflater.from(parent.getContext()).inflate(R.layout.double_image,parent,false);
                imageView1 = (ImageView) hol.findViewById(R.id.posts_pic_home);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(0)+".jpg")
                        .error(R.drawable.logo_main)
                        .centerCrop()
                        .into(imageView1);

                imageView2 = (ImageView) hol.findViewById(R.id.image2);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(1)+".jpg")
                        .error(R.drawable.logo_main)
                        .centerCrop()
                        .into(imageView2);
                break;

            case 3:
                Log.e("main","under 3");
                hol = LayoutInflater.from(parent.getContext()).inflate(R.layout.triple_image,parent,false);
                imageView1 = (ImageView) hol.findViewById(R.id.posts_pic_home);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(0)+".jpg")
                        .error(R.drawable.logo_main)
                        .centerCrop()
                        .into(imageView1);

                imageView2 = (ImageView) hol.findViewById(R.id.image2);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(1)+".jpg")
                        .error(R.drawable.logo_main)
                        .centerCrop()
                        .into(imageView2);

                imageView3 = (ImageView) hol.findViewById(R.id.image3);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(2)+".jpg")
                        .error(R.drawable.logo_main)
                        .centerCrop()
                        .into(imageView3);
                break;
            case 4:
                Log.e("main","under 4");
                hol = LayoutInflater.from(parent.getContext()).inflate(R.layout.quadruple_image,parent,false);
                imageView1 = (ImageView) hol.findViewById(R.id.posts_pic_home);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(0)+".jpg")
                        .centerCrop()
                        .error(R.drawable.logo_main)
                        .into(imageView1);

                imageView2 = (ImageView) hol.findViewById(R.id.image2);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(1)+".jpg")
                        .centerCrop()
                        .error(R.drawable.logo_main)
                        .into(imageView2);

                imageView3 = (ImageView) hol.findViewById(R.id.image3);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(2)+".jpg")
                        .centerCrop()
                        .error(R.drawable.logo_main)
                        .into(imageView3);

                imageView4 = (ImageView) hol.findViewById(R.id.image4);
                Glide.with(activity)
                        .load(apis.IMAGE_API+postid+"/"+list.get(3)+".jpg")
                        .centerCrop()
                        .error(R.drawable.logo_main)
                        .into(imageView4);
                break;


        }

        parent.addView(hol,0);
    }

}
