package com.testing.hoan.at_mobile;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoan on 11/3/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String AdapterTAG="Adapter";
    private String dummyUrl="http://i.imgur.com/DvpvklR.png";
    //ImageLoader to load image
    private ImageLoader imageLoader;
    private Context context;
    private ArrayList<Events> eventList;
    public RecyclerViewAdapter(ArrayList<Events> eventList, Context context){
        super();
        //getting all the events
        this.eventList=eventList;
        this.context=context;
    }
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view,parent,false);
        ViewHolder viewHolder =new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        //getting the particular item on the list
        Events event=eventList.get(position);
        //Loading image from URL
        imageLoader =ApplicationControl.getInstance(context).getmImageLoader();
        imageLoader.get(event.getImageUrl(),ImageLoader.getImageListener(holder.imageView, R.drawable.ic_drawer, android.R.drawable.ic_dialog_alert));
        holder.getImageView().setImageUrl(event.getImageUrl(),imageLoader);
        holder.getTextView().setText(event.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(holder.getTextView().getTransitionName()==null) {
                holder.getTextView().setTransitionName("title" + position);
            }
            if(holder.getImageView().getTransitionName()==null){
                holder.getImageView().setTransitionName("image"+position);
            }
            Log.i(AdapterTAG, holder.getTextView().getTransitionName());
            Log.i(AdapterTAG, holder.getImageView().getTransitionName());
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //test view
        private TextView tv;
        //Views
        private NetworkImageView imageView;
        // initializing views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(NetworkImageView) itemView.findViewById(R.id.imageItem);
            tv=(TextView) itemView.findViewById(R.id.testTv);

        }
        public NetworkImageView getImageView(){
            return imageView;
        }
        public TextView getTextView(){
            return tv;
        }
    }
}
