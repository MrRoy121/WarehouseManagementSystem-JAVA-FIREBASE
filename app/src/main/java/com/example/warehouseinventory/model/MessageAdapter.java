package com.example.warehouseinventory.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.warehouseinventory.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    ArrayList<MessageModel> listdata;

    public MessageAdapter(ArrayList<MessageModel> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.meesage_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MessageModel myListData = listdata.get(position);

        if(myListData.sender){
            holder.user.setVisibility(View.VISIBLE);
            holder.message1.setText(myListData.message);
            String pattern = "dd-MM hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String datess = simpleDateFormat.format(myListData.createdAt);
            holder.time1.setText(datess);
        }else{
            holder.other.setVisibility(View.VISIBLE);
            holder.message.setText(myListData.message);
            holder.username.setText(myListData.uid);
            String pattern = "dd-MM hh:mm a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String datess = simpleDateFormat.format(myListData.createdAt);
            holder.time.setText(datess);
        }



    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message, time, message1, time1, username;
        public LinearLayout user,other;
        public ViewHolder(View itemView) {
            super(itemView);
            this.message = (TextView) itemView.findViewById(R.id.message);
            this.time = (TextView) itemView.findViewById(R.id.time);
            this.message1 = (TextView) itemView.findViewById(R.id.message1);
            this.time1 = (TextView) itemView.findViewById(R.id.time1);
            this.username = (TextView) itemView.findViewById(R.id.username);
            this.user =  itemView.findViewById(R.id.user);
            this.other =  itemView.findViewById(R.id.other);
        }
    }
}