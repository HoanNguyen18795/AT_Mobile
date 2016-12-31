package com.testing.hoan.at_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hoan on 12/29/2016.
 */

public class listViewAdapter extends BaseAdapter {

    private ArrayList<Comments> commentList;
    private LayoutInflater layoutInflater;
    private Context context;

    public listViewAdapter(Context context,ArrayList<Comments> commentList){
        this.commentList=commentList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.commentlv, null);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.textView);
            holder.comment = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comments com = this.commentList.get(position);
        holder.userName.setText(com.getUserName());
        holder.comment.setText(com.getComment());
        return convertView;
    }
    static class ViewHolder {
         TextView userName;
         TextView comment;
    }
}
