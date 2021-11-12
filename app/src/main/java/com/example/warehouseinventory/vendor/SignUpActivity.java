package com.example.warehouseinventory.vendor;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.warehouseinventory.LoginActivity;
import com.example.warehouseinventory.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    EditText username, email, mobile, password, conpassword;
    Button register;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        conpassword = findViewById(R.id.confirmpassword);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        register = findViewById(R.id.signupbtn);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().length()==0 && password.getText().length()==0 && email.getText().length()==0 && mobile.getText().length()==0 && conpassword.getText().length()==0){
                    Toast.makeText(SignUpActivity.this, "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
                }else if(!(password.getText().toString().equals(conpassword.getText().toString()))){
                    Toast.makeText(SignUpActivity.this, "Password & Confirm Password Must be Same. " +password.getText().toString()+"\n"+conpassword.getText().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    fauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String userid = fauth.getCurrentUser().getUid();
                            DocumentReference refer = fstore.collection("Vendors").document(userid);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Email", email.getText().toString());
                            user.put("username", username.getText().toString());
                            user.put("Mobile", mobile.getText().toString());
                            user.put("Password", password.getText().toString());
                            refer.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Register Successful",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Register unsuccessful \n " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}