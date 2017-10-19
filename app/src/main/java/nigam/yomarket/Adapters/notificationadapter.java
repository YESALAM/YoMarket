package nigam.yomarket.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nigam.yomarket.Posts_activity;
import nigam.yomarket.R;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.utils.Statics;

/**
 * Created by alokit nigam on 5/20/2017.
 */

public class notificationadapter extends RecyclerView.Adapter<notificationadapter.ViewHolder> {
    View rootView;
    AppCompatActivity activity;
    ArrayList<HomeListGetSet> list;

    public notificationadapter(AppCompatActivity activity, ArrayList<HomeListGetSet> gridSetter)
    {
        this.activity = activity;
        this.list = gridSetter;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.androidnotificton, null);
        ViewHolder holder = new ViewHolder(rootView);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int size = list.size();
        HomeListGetSet h = list.get(size-position-1);

            if(h.getComment() == null){
                holder.notify.setText(h.getPost_product()+" Worth Rs. "+h.getPost_price());
            }else{
                String notif = h.getComment() ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    notif = String.valueOf((Html.fromHtml(notif, Html.FROM_HTML_MODE_COMPACT)));
                } else {
                    notif = String.valueOf((Html.fromHtml(notif)));
                }
                holder.notify.setText(notif);
                holder.text.setText(h.getCommentby()+" Commented");
               /* if(!h.read){
                    holder.noti_layout.setBackgroundColor(activity.getResources().getColor(android.R.color.darker_gray));
                }*/


            }

        final ArrayList<String> image_list = new ArrayList<>();
        int count = 0 ;
        if(!h.getPost_image_1().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_1());
        }
        if(!h.getPost_image_2().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_2());
        }
        if(!h.getPost_image_3().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_3());
        }
        if(!h.getPost_image_4().equalsIgnoreCase("null")){
            count++;
            image_list.add(h.getPost_image_4());
        }

        final int finalcount=count;
        //Log.i("NotificationAdapter","onViewHolder");


//    Product,Post_ID_City,Profession,Quantity,Price,Description,Image_1,Image_2,Image_3,Image_4	;

        holder.card.setOnClickListener(new OnClickListener() {
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

        TextView notify,text;
        LinearLayout card;
        LinearLayout noti_layout;
        public ViewHolder(final View rView) {
            super(rView);
            card = (LinearLayout) rView.findViewById(R.id.notification_card);
            notify = (TextView) rView.findViewById(R.id.notification);
            text= (TextView) rView.findViewById(R.id.textView15);
            noti_layout = (LinearLayout) rView.findViewById(R.id.noti_layout);
        }
    }

}
