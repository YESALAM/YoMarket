package nigam.yomarket.Adapters;

import android.graphics.Color;
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

        Log.i("doInBackground:response","aaaasasaaasadaada creare");

        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.androidnotificton, null);
        ViewHolder holder = new ViewHolder(rootView);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int size = list.size();
        HomeListGetSet h = list.get(size-position-1);
        Log.i("doInBackground:response","aaaasasaaasadaada"+h.getPost_price());


            holder.notify.setText(h.getPost_product()+" Worth Rs. "+h.getPost_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView notify,text;
        public ViewHolder(final View rView) {
            super(rView);
            Log.i("doInBackground:response","aaaasasaaasadaada view holdar");

            notify = (TextView) rView.findViewById(R.id.notification);
            text= (TextView) rView.findViewById(R.id.textView15);

        }
    }

}
