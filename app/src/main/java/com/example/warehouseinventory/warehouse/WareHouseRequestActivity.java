package com.example.warehouseinventory.warehouse;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.warehouseinventory.R;
import com.example.warehouseinventory.model.CartModel;
import com.example.warehouseinventory.model.RequestAdapter;
import com.example.warehouseinventory.model.RequestModel;
import com.example.warehouseinventory.model.WarehouseRequestAdapter;
import com.example.warehouseinventory.vendor.MyRequestsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WareHouseRequestActivity extends AppCompatActivity {
    public static ArrayList<RequestModel> exampleListFull;
    GridView simpleGrid;
    ArrayList<RequestModel> names;
    FirebaseFirestore fs;
    WarehouseRequestAdapter requestAdapter;
    Button allb, shippedb, deliveredb, pendingb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_request);

        fs = FirebaseFirestore.getInstance();

        simpleGrid = findViewById(R.id.simpleGridView);
        allb = findViewById(R.id.all);
        shippedb = findViewById(R.id.shipped);
        deliveredb = findViewById(R.id.delivered);
        pendingb = findViewById(R.id.pending);
        names = new ArrayList<RequestModel>();
        exampleListFull = new ArrayList<RequestModel>();
        requestAdapter = new WarehouseRequestAdapter(WareHouseRequestActivity.this, names);
        fs.collection("Request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot q) {
                if (!q.isEmpty()) {
                    for (DocumentSnapshot d : q) {
                        fs.collection("Vendors").document(d.getString("User Id")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                names.add(new RequestModel(d.getString("Status"), d.getString("Ship Address"), d.getDate("Date"),documentSnapshot.getString("username"),d.getString("User Id"), d.getId(), (Map<String, Object>) d.get("Cart"),d.getString("Image")));
                                exampleListFull.add(new RequestModel(d.getString("Status"), d.getString("Ship Address"), d.getDate("Date"),documentSnapshot.getString("username"),d.getString("User Id"), d.getId(), (Map<String, Object>) d.get("Cart"),d.getString("Image")));
                                requestAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    requestAdapter.notifyDataSetChanged();
                }
            }
        });
        simpleGrid.setAdapter(requestAdapter);

        allb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdapter.getFilter().filter("All");
            }
        });

        pendingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdapter.getFilter().filter("Pending");
            }
        });
        shippedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdapter.getFilter().filter("Shipped");
            }
        });
        deliveredb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdapter.getFilter().filter("Delivered");
            }
        });
        simpleGrid.setOnItemClickListener((parent, view, position, id) -> {

            if (names.get(position).getStatus().equals("Pending")) {

                final Dialog dialog = new Dialog(WareHouseRequestActivity.this);
                dialog.setContentView(R.layout.custom_dialog2);
                TextView t = dialog.findViewById(R.id.tit);
                t.setText("Confirm Shipping");
                ImageView img = dialog.findViewById(R.id.img);
                ImageButton save = dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File fs = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "/"+names.get(position).getUsername() + ".jpeg");
                        DownloadManager downloadmanager = (DownloadManager) WareHouseRequestActivity.this.
                                getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(names.get(position).getImage());
                        DownloadManager.Request request = new DownloadManager.Request(uri)
                                .setTitle("Downloading")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                .setDestinationUri( Uri.fromFile(fs));
                        downloadmanager.enqueue(request);

                    }
                });
                Glide.with(WareHouseRequestActivity.this)
                        .load(names.get(position).getImage())
                        .into(img);
                Button dialogButton = dialog.findViewById(R.id.remove);
                dialogButton.setText("Confirm");
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        for (String key : names.get(position).getProducts().keySet()) {
                            String value = String.valueOf(names.get(position).getProducts().get(key));
                            fs.collection("Products").whereEqualTo("Product Name", key).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot q) {
                                    if(!q.isEmpty()){
                                        for(DocumentSnapshot d: q){
                                            Map<String, Object> crt = new HashMap<>();
                                            crt.put("Brand",d.getString("Brand"));
                                            crt.put("Product Name",d.getString("Product Name"));
                                            crt.put("Check ID",d.getString("Check ID"));
                                            crt.put("Vendor",d.getString("Vendor"));
                                            crt.put("Quantity", String.valueOf(Integer.parseInt(d.getString("Quantity"))- Integer.parseInt(value)));
                                            fs.collection("Products").document(d.getId()).set(crt);
                                        }
                                    }
                                }
                            });
                        }
                        Map<String, Object> sss = new HashMap<>();
                        sss.put("User Id", names.get(position).getUid());
                        sss.put("Cart", names.get(position).getProducts());
                        sss.put("Ship Address", names.get(position).getAddress());
                        sss.put("Date", names.get(position).getDate());
                        sss.put("Image", names.get(position).getImage());
                        sss.put("Status", "Shipped");


                        fs.collection("Request").document(names.get(position).getRid()).set(sss);
                        Toast.makeText(getApplicationContext(),"Item Shipping Confirmed To User "+ names.get(position).getUsername() ,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                });
                Button dialogButto = dialog.findViewById(R.id.cancel);
                dialogButto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else if (names.get(position).getStatus().equals("Shipped")) {


                final Dialog dialog = new Dialog(WareHouseRequestActivity.this);
                dialog.setContentView(R.layout.custom_dialog2);
                TextView t = dialog.findViewById(R.id.tit);
                t.setText("Confirm Delivery of ");ImageView img = dialog.findViewById(R.id.img);
                ImageButton save = dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File fs = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "/"+names.get(position).getUsername() + ".jpeg");
                        DownloadManager downloadmanager = (DownloadManager) WareHouseRequestActivity.this.
                                getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(names.get(position).getImage());
                        DownloadManager.Request request = new DownloadManager.Request(uri)
                                .setTitle("Downloading")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                .setDestinationUri( Uri.fromFile(fs));
                        downloadmanager.enqueue(request);

                    }
                });
                Glide.with(WareHouseRequestActivity.this)
                        .load(names.get(position).getImage() )
                        .into(img);
                Button dialogButton = dialog.findViewById(R.id.remove);
                dialogButton.setText("Confirm");
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> sss = new HashMap<>();
                        sss.put("User Id", names.get(position).getUid());
                        sss.put("Cart", names.get(position).getProducts());
                        sss.put("Ship Address", names.get(position).getAddress());
                        sss.put("Date", names.get(position).getDate());
                        sss.put("Image", names.get(position).getImage());
                        sss.put("Status", "Delivered");
                        fs.collection("Request").document(names.get(position).getRid()).set(sss);
                        Toast.makeText(getApplicationContext(),"Item Delivery Confirmed To User "+ names.get(position).getUsername() ,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());

                    }
                });
                Button dialogButto = dialog.findViewById(R.id.cancel);
                dialogButto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }else{
                Toast.makeText(getApplicationContext(), "Product Is Delivered", Toast.LENGTH_SHORT).show();
            }
        });


    }
}