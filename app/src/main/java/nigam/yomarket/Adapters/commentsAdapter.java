package nigam.yomarket.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import nigam.yomarket.R;
import nigam.yomarket.getset.HomeListGetSet;
import nigam.yomarket.getset.getcomments;

/**
 * Created by alokit nigam on 5/19/2017.
 */

public class commentsAdapter extends RecyclerView.Adapter<commentsAdapter.ViewHolder>{
    View rootView;
    AppCompatActivity activity;
    ArrayList<getcomments> list;
    public commentsAdapter(AppCompatActivity activity, ArrayList<getcomments> gridSetter)
    {
        this.activity = activity;
        this.list = gridSetter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comments, null);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(commentsAdapter.ViewHolder holder, int position) {

        getcomments hl =list.get(position);
        holder.comment.setText(hl.getComments());
        holder.date_time.setText(hl.getDate_time());
        holder.name.setText(hl.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        CardView card;

        TextView name,date_time,comment;
        public ViewHolder(final View rView) {
            super(rView);

            name= (TextView) rView.findViewById(R.id.Comment_name);
            date_time= (TextView) rView.findViewById(R.id.comment_date_time);
            comment= (TextView) rView.findViewById(R.id.comment_header);



        }
    }
}
