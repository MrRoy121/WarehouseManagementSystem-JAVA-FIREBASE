package com.example.warehouseinventory.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.warehouseinventory.R;
import com.example.warehouseinventory.vendor.CartActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private ArrayList<CartModel> listdata;

    Context ctx;
    public CartAdapter(ArrayList<CartModel> listdata,  Context ctx) {
        this.listdata = listdata;
        this.ctx = ctx;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_item4, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CartModel myListData = listdata.get(position);
        holder.name.setText(String.valueOf(listdata.get(position).name));
        holder.item.setText(String.valueOf(listdata.get(position).item));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Cart").document(myListData.itemid).delete();
                listdata.remove(myListData);
                notifyDataSetChanged();
                Toast.makeText(ctx, "Item Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item, name;
        public ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);
            this.delete = itemView.findViewById(R.id.delete);
            this.item =  itemView.findViewById(R.id.item);
            this.name =  itemView.findViewById(R.id.name);
        }
    }
}