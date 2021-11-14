package com.example.warehouseinventory.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.warehouseinventory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPassordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText=(EditText) findViewById(R.id.email);
        resetPassordButton=(Button) findViewById(R.id.resetPassword);
            auth=FirebaseAuth.getInstance();

            resetPassordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetpassword();
                }
            });
    }
    private void resetpassword(){
        String email=emailEditText.getText().toString().trim();
        if(email.isEmpty()){
            emailEditText.setError("email is required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("please provide valid email");
            emailEditText.requestFocus();
            return;
        }

auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            Toast.makeText(ForgotPassword.this, "check your email for password reset Link", Toast.LENGTH_SHORT).show();
        }
       else{
            Toast.makeText(ForgotPassword.this, "Try Again!! something wrong happened.", Toast.LENGTH_SHORT).show();
        }
    }
});
    }
}