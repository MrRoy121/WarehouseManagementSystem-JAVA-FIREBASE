package com.example.warehouseinventory.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.warehouseinventory.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AllUserActivity extends AppCompatActivity {

    ListView l;
    ArrayList<String>user;
    ArrayList<String>id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        l = findViewById(R.id.list);
        user = new ArrayList<>();
        id = new ArrayList<>();
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user);
        FirebaseFirestore.getInstance().collection("Vendors").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(DocumentSnapshot d:queryDocumentSnapshots){
                        user.add(d.getString("username"));
                        id.add(d.getId());
                    }
                    arr.notifyDataSetChanged();
                }
            }
        });
        l.setAdapter(arr);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent is = new Intent(AllUserActivity.this, WarehousechatActivity.class);
                is.putExtra("Uid", id.get(i));
                is.putExtra("Username", user.get(i));
                startActivity(is);
            }
        });
    }
}