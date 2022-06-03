package com.example.camerastore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firestore.v1.MapValueOrBuilder;

import java.util.HashMap;
import java.util.Map;

public class AddAcitivity extends AppCompatActivity {

    EditText name,price,quantity,imgUrl;
    Button btnAdd,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove the action bar
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_acitivity);

        name = findViewById(R.id.txtName);
        price = findViewById(R.id.txtPrice);
        quantity = findViewById(R.id.txtQuantity);
        imgUrl = findViewById(R.id.txtimageUrl);

        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void insertData(){
        Map<String,Object> map = new HashMap<>();
        map.put("imageUrl",imgUrl.getText().toString());
        map.put("name",name.getText().toString());
        map.put("price",Integer.parseInt(price.getText().toString()));
        map.put("quantity",Integer.parseInt(quantity.getText().toString()));

        FirebaseDatabase.getInstance().getReference().child("products").child(name.getText().toString())
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddAcitivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddAcitivity.this, "Error while Insertion.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this,updateData.class);
        startActivity(intent3);
    }
}