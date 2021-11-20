package com.example.warehouseinventory.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehouseinventory.R;
import com.example.warehouseinventory.model.CheckinAdapter;
import com.example.warehouseinventory.model.CheckinModel;
import com.example.warehouseinventory.vendor.MyRequestsActivity;
import com.example.warehouseinventory.vendor.UserInventoryActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryActivity extends AppCompatActivity {


    GridView simpleGrid;
    ArrayList<CheckinModel> names;
    FirebaseFirestore fs;
    CheckinAdapter customCheckinAdapter;
    public static ArrayList<CheckinModel> exampleListFull = new ArrayList<>();
    Button name, vendor, id;

    boolean bname = true, bid = false, bvendor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        SearchView searchView = findViewById(R.id.searchView);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        vendor = findViewById(R.id.vendor);


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bname){
                    name.setBackgroundColor(0xFFAA880F);
                    id.setBackgroundColor(0xFFface2f);
                    vendor.setBackgroundColor(0xFFface2f);
                    bname= true;
                    bid = false;
                    bvendor = false;
                }
            }
        });


        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bid){
                    id.setBackgroundColor(0xFFAA880F);
                    name.setBackgroundColor(0xFFface2f);
                    vendor.setBackgroundColor(0xFFface2f);
                    bid = true;
                    bname = false;
                    bvendor = false;
                }
            }
        });

        vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bvendor){
                    vendor.setBackgroundColor(0xFFAA880F);
                    name.setBackgroundColor(0xFFface2f);
                    id.setBackgroundColor(0xFFface2f);
                    bvendor = true;
                    bname = false;
                    bid = false;
                }
            }
        });

        fs = FirebaseFirestore.getInstance();

        simpleGrid = findViewById(R.id.simpleGridView);
        names = new ArrayList<>();
        customCheckinAdapter =new CheckinAdapter(InventoryActivity.this, names);
        fs.collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot q) {
                    for(DocumentSnapshot d : q){
                        FirebaseFirestore.getInstance().collection("Vendors").document(d.getString("Vendor")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot da) {
                                if(d.exists()){
                                    names.add(new CheckinModel(d.getString("Product Name"),d.getString("Brand"),d.getString("Quantity"),d.getString("Check ID"),d.getId(),da.getString("username")));
                                    exampleListFull.add(new CheckinModel(d.getString("Product Name"),d.getString("Brand"),d.getString("Quantity"),d.getString("Check ID"),d.getId(),da.getString("username")));
                                    customCheckinAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                        }
            }
        });
        simpleGrid.setAdapter(customCheckinAdapter);

        simpleGrid.setOnItemClickListener((parent, view, position, id) -> {


            final Dialog dialog = new Dialog(InventoryActivity.this);
            dialog.setContentView(R.layout.custom_dialog2);
            Button dialogButton =  dialog.findViewById(R.id.remove);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        fs.collection("Products").document(names.get(position).getPid()).delete();
                        Toast.makeText(getApplicationContext(),"Item Removed..",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                }
            });
            Button dialogButto =  dialog.findViewById(R.id.cancel);
            dialogButto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(bvendor){
                    customCheckinAdapter =new CheckinAdapter(InventoryActivity.this, names);
                    simpleGrid.setAdapter(customCheckinAdapter);
                    customCheckinAdapter.getSecondFilter().filter(newText);
                }else if(bid){
                    customCheckinAdapter =new CheckinAdapter(InventoryActivity.this, names);
                    simpleGrid.setAdapter(customCheckinAdapter);
                    customCheckinAdapter.getThirdFilter().filter(newText);
                }else {
                    customCheckinAdapter =new CheckinAdapter(InventoryActivity.this, names);
                    simpleGrid.setAdapter(customCheckinAdapter);
                    customCheckinAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}