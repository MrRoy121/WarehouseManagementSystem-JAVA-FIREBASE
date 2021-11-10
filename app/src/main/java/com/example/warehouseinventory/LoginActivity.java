package com.example.warehouseinventory;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.warehouseinventory.vendor.HomeActivity;
import com.example.warehouseinventory.vendor.SignUpActivity;
import com.example.warehouseinventory.warehouse.WarehouseMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth fauth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        EditText username = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button regbtn = findViewById(R.id.loginbtn);


        if (fauth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
        }

        findViewById(R.id.regi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        regbtn.setOnClickListener(view -> {
            String email1 = username.getText().toString();
            String password1 = password.getText().toString();
            if(username.getText().length()==0 || password.getText().length()==0 ){
                Toast.makeText(LoginActivity.this, "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
            }else{
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin") ){
                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, WarehouseMainActivity.class);
                    intent.putExtra("User", email1);
                    startActivity(intent);
                    finish();
                }else{
                    fauth.signInWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }
}