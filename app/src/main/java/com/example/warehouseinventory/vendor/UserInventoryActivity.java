package com.example.warehouseinventory.vendor;

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
import com.example.warehouseinventory.model.userCheckinAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserInventoryActivity extends AppCompatActivity {


    GridView simpleGrid;
    ArrayList<CheckinModel> names;
    FirebaseFirestore fs;
    userCheckinAdapter customCheckinAdapter;
   public static ArrayList<CheckinModel> exampleListFull = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inventory);


        SearchView searchView = findViewById(R.id.searchView);


        fs = FirebaseFirestore.getInstance();

        simpleGrid = findViewById(R.id.simpleGridView);
        names = new ArrayList<>();
        customCheckinAdapter = new userCheckinAdapter(UserInventoryActivity.this, names);
        fs.collection("Products").whereEqualTo("Vendor", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot q) {
                if (!q.isEmpty()) {
                    for (DocumentSnapshot d : q) {
                        names.add(new CheckinModel(d.getString("Product Name"),d.getString("Brand"),d.getString("Quantity"),d.getString("Check ID"),d.getId(),""));
                        exampleListFull.add(new CheckinModel(d.getString("Product Name"),d.getString("Brand"),d.getString("Quantity"),d.getString("Check ID"),d.getId(),""));
                    }
                    customCheckinAdapter.notifyDataSetChanged();
                }
            }
        });
        simpleGrid.setAdapter(customCheckinAdapter);


        simpleGrid.setOnItemClickListener((parent, view, position, id) -> {

            final Dialog dialog = new Dialog(UserInventoryActivity.this);
            dialog.setContentView(R.layout.custom_dialog1);
            TextView tit = dialog.findViewById(R.id.title);
            EditText address = dialog.findViewById(R.id.address);
            tit.setText(names.get(position).getName());
            Map<String, Object> user = new HashMap<>();
            Button dialogButton = dialog.findViewById(R.id.send);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(address.getText().toString().length() == 0)) {
                        if (Integer.parseInt(address.getText().toString()) <= Integer.parseInt(names.get(position).getQuantity())) {
                            user.put("User Id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            user.put("Product ID", names.get(position).getPid());
                            user.put("Items", address.getText().toString());
                            fs.collection("Cart").document(names.get(position).getPid()).set(user);
                            Toast.makeText(getApplicationContext(),  names.get(position) + " Added to Cart.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else
                            Toast.makeText(getApplicationContext(), "Item has to be Less or Equal then the Quantity", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getApplicationContext(), "Add the Item Count", Toast.LENGTH_SHORT).show();
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
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customCheckinAdapter = new userCheckinAdapter(UserInventoryActivity.this, names);
                simpleGrid.setAdapter(customCheckinAdapter);
                customCheckinAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}