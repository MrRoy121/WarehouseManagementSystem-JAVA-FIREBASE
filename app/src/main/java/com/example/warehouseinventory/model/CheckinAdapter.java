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
import com.example.warehouseinventory.vendor.UserInventoryActivity;
import com.example.warehouseinventory.warehouse.InventoryActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckinAdapter extends BaseAdapter  implements Filterable {
    Context context;
    ArrayList<CheckinModel> name;
    LayoutInflater inflter;

    public CheckinAdapter(Context applicationContext, ArrayList<CheckinModel> name) {
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
        view = inflter.inflate(R.layout.warehouse_inventory, null);
        TextView cids =  view.findViewById(R.id.cid);
        TextView names =  view.findViewById(R.id.name);
        TextView brands =  view.findViewById(R.id.brand);
        TextView prices =  view.findViewById(R.id.price);
        TextView vname =  view.findViewById(R.id.vname);

        cids.setText(name.get(i).cid);
        names.setText(name.get(i).name);
        brands.setText(name.get(i).brand);
        prices.setText(name.get(i).quantity);

        vname.setText(name.get(i).vendor);


        return view;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    public Filter getSecondFilter() {return mSecondFilter;}
    public Filter getThirdFilter() {return mThirdFilter;}
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CheckinModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(InventoryActivity.exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CheckinModel item : InventoryActivity.exampleListFull) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
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

    private Filter mSecondFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CheckinModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(InventoryActivity.exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CheckinModel item : InventoryActivity.exampleListFull) {
                    if (item.vendor.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
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

    private Filter mThirdFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CheckinModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(InventoryActivity.exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CheckinModel item : InventoryActivity.exampleListFull) {
                    if (item.cid.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
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