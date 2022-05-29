package com.example.camerastore;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class updateData extends AppCompatActivity {

    EditText productNumber,imgUrl,name,price,quantity;
    DatabaseReference rootdatabaseReference;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        btnUpdate = findViewById(R.id.updateBtn);
        productNumber = findViewById(R.id.numberProductData);
        imgUrl = findViewById(R.id.imgUrlData);
        name = findViewById(R.id.nameData);
        price = findViewById(R.id.priceData);
        quantity = findViewById(R.id.quantityData);
        rootdatabaseReference = FirebaseDatabase.getInstance().getReference().child("products");


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgUr2 = imgUrl.getText().toString();
                String name2 = name.getText().toString();
                int price2 = Integer.parseInt(price.getText().toString());
                int quantity2 = Integer.parseInt(quantity.getText().toString());

                HashMap hashMap = new HashMap();
                hashMap.put("imageUrl",imgUr2);
                hashMap.put("name",name2);
                hashMap.put("price",price2);
                hashMap.put("quantity",quantity2);

                rootdatabaseReference.child(productNumber.getText().toString()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(updateData.this, "Your Data is Successfuly Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}