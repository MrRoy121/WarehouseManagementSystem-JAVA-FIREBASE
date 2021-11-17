package com.example.warehouseinventory.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.warehouseinventory.LoginActivity;
import com.example.warehouseinventory.R;
import com.example.warehouseinventory.vendor.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class WarehouseMainActivity extends AppCompatActivity {


    Button checkin, viewinvenrtory, signOutBtn, requests, chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_main);


       checkin= findViewById(R.id.checkin);
       viewinvenrtory = findViewById(R.id.viewinvenrtory);
        signOutBtn = findViewById(R.id.signOutBtn);
        requests = findViewById(R.id.requests);
        chat = findViewById(R.id.chat);

        viewinvenrtory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WarehouseMainActivity.this, InventoryActivity.class));
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WarehouseMainActivity.this, CheckinActivity.class));
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(WarehouseMainActivity.this, LoginActivity.class));

            }
        });


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WarehouseMainActivity.this, AllUserActivity.class));

            }
        });
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WarehouseMainActivity.this, WareHouseRequestActivity.class));

            }
        });
    }
}