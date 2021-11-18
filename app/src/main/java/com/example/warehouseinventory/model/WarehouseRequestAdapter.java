package com.example.warehouseinventory.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.warehouseinventory.R;
import com.example.warehouseinventory.warehouse.WareHouseRequestActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WarehouseRequestAdapter extends BaseAdapter  implements Filterable {
    Context context;
    ArrayList<RequestModel> name;

    LayoutInflater inflter;

    public WarehouseRequestAdapter(Context applicationContext, ArrayList<RequestModel> name) {
        this.context = applicationContext;
        this.name = name;

        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return name.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_item3, null);
        TextView dates =  view.findViewById(R.id.date);
        TextView unames =  view.findViewById(R.id.uname);
        TextView names =  view.findViewById(R.id.name);
        TextView addresss =  view.findViewById(R.id.address);
        TextView statuss =  view.findViewById(R.id.status);
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String datess = simpleDateFormat.format(name.get(i).date);
        dates.setText(datess);
        String items ="";
        for (String key : name.get(i).products.keySet()) {
            String value = String.valueOf(name.get(i).products.get(key));
            items = items +" "+key + " " + value + " Items, ";
        }
        names.setText(items);
        unames.setText(name.get(i).username);
        addresss.setText(name.get(i).address);
        statuss.setText(name.get(i).status);
        return view;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RequestModel> filteredList = new ArrayList<>();
            String filterPattern = constraint.toString();
            if(filterPattern.equals("Pending")){
                for (RequestModel item : WareHouseRequestActivity.exampleListFull) {
                    if (item.status.contains("Pending")) {
                        filteredList.add(item);
                    }
                }
            }else if(filterPattern.equals("Shipped")){
                for (RequestModel item : WareHouseRequestActivity.exampleListFull) {
                    if (item.status.contains("Shipped")) {
                        filteredList.add(item);
                    }
                }
            }else if(filterPattern.equals("Delivered")){
                for (RequestModel item : WareHouseRequestActivity.exampleListFull) {
                    if (item.status.contains("Delivered")) {
                        filteredList.add(item);
                    }
                }
            }else{
                for (RequestModel item : WareHouseRequestActivity.exampleListFull) {
                    filteredList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            name.clear();
            name.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}