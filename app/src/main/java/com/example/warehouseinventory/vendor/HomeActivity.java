package com.example.warehouseinventory.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouseinventory.LoginActivity;
import com.example.warehouseinventory.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    TextView uid;
    Button inventory, myreq, chat, signOutBtn, cart, profile;
    FirebaseFirestore fstore;
    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        uid = findViewById(R.id.uid);
        profile = findViewById(R.id.profile);
        inventory = findViewById(R.id.inventory);
        myreq = findViewById(R.id.myrequests);
        chat = findViewById(R.id.chat);
        signOutBtn = findViewById(R.id.signOutBtn);
        cart = findViewById(R.id.cart);

        fstore = FirebaseFirestore.getInstance();

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,UserInventoryActivity.class ));
            }
        });
        myreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,MyRequestsActivity.class ));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,CartActivity.class ));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VendorProfile.class ));
            }
        });
        fstore.collection("Vendors").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    uid.setText(documentSnapshot.getString("username"));

                    uname = documentSnapshot.getString("username");
                }else{
                    finish();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ChatActivity.class);
                i.putExtra("username", uname);
                startActivity(i);
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));

            }
        });
    }

}