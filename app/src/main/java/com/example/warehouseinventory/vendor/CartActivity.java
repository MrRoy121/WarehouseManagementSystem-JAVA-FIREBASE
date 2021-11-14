package com.example.warehouseinventory.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warehouseinventory.R;
import com.example.warehouseinventory.model.CartAdapter;
import com.example.warehouseinventory.model.CartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {
    ArrayList<CartModel> listdata = new ArrayList<>();
    CartAdapter adapters;
    Button checkout, cancel;
    ImageView img;


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FirebaseFirestore.getInstance().collection("Cart").whereEqualTo("User Id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot q1) {
                if (!q1.isEmpty()) {
                    for (DocumentSnapshot d1 : q1) {
                        FirebaseFirestore.getInstance().collection("Products").document(d1.getString("Product ID")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                listdata.add(new CartModel(documentSnapshot.getString("Product Name"), d1.getId(), Integer.parseInt(d1.getString("Items"))));

                                adapters.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        });

        adapters = new CartAdapter(listdata, CartActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapters);

        checkout = findViewById(R.id.checkout);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listdata.size()!=0){

                    final Dialog dialog = new Dialog(CartActivity.this);
                    dialog.setContentView(R.layout.custom_dialog3);
                    EditText address = dialog.findViewById(R.id.address);
                    Button dialogButton = dialog.findViewById(R.id.send);
                    Button upload = dialog.findViewById(R.id.upload);
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SelectImage();
                        }
                    });
                    img = dialog.findViewById(R.id.imgView);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!(address.getText().toString().length() == 0)) {
                                uploadImage(address.getText().toString());

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
                }else{
                    Toast.makeText(getApplicationContext(), "No Item in Cart", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(String Address) {
        if (filePath != null) {

            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref
                    = storageReference
                    .child(
                            "CartId/"
                                    + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Map<String, Object> cart = new HashMap<>();
                                                    for (CartModel item : listdata) {
                                                        cart.put(item.getName(), item.getItem());
                                                        FirebaseFirestore.getInstance().collection("Cart").document(item.getItemid()).delete();
                                                    }
                                                    Map<String, Object> sss = new HashMap<>();
                                                    sss.put("User Id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    sss.put("Cart", cart);
                                                    sss.put("Ship Address", Address);
                                                    sss.put("Date", new Date());
                                                    sss.put("Image", uri.toString());
                                                    sss.put("Status", "Pending");
                                                    FirebaseFirestore.getInstance().collection("Request").add(sss).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            progressDialog.dismiss();
                                                            Toast
                                                                    .makeText(CartActivity.this,
                                                                            "Request Added!!",
                                                                            Toast.LENGTH_SHORT)
                                                                    .show();
                                                            finish();
                                                        }
                                                    });

                                                }
                                            });


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast
                                    .makeText(CartActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
}