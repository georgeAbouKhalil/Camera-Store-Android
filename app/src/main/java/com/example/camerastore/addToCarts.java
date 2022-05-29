package com.example.camerastore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class addToCarts extends AppCompatActivity {


    TextView quantityTv,originalPrice,nameTV,pQuantityTv;
    ImageView productIv;
    Button decrementBtn,incrementBtn;
    int quantityTvC,originalPriceC = 0,quantityFromDB;
    String quantityTv2,originalPrice2,imgDB,quantitDB,nameDB,priceDB,firstPrice;
    Button addToCartBtn;
    DatabaseReference rootdatabaseReference,rootCartdatabaseReference;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove the action bar
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_to_carts);

        quantityTv = findViewById(R.id.quantityTv);
        decrementBtn = findViewById(R.id.decrementBtn);
        incrementBtn = findViewById(R.id.incrementBtn);
        originalPrice = findViewById(R.id.originalPrice);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        productIv = findViewById(R.id.productIv);
        nameTV = findViewById(R.id.nameTV);
        pQuantityTv = findViewById(R.id.pQuantityTv);
        quantityTvC = Integer.parseInt(quantityTv.getText().toString());
        rootdatabaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        rootCartdatabaseReference = FirebaseDatabase.getInstance().getReference().child("cart");

        //get currect user
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        final FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if(mFirebaseUser != null){
            userId = fAuth.getCurrentUser().getUid();
            Log.d("userr", userId);
        }



        Intent recItems = getIntent();
        imgDB = recItems.getStringExtra("IMAGE");
        nameDB = recItems.getStringExtra("NAME");
        priceDB = recItems.getStringExtra("PRICE");
        quantitDB = recItems.getStringExtra("quantity");

        originalPrice.setText(priceDB);
        nameTV.setText(nameDB);
        pQuantityTv.setText(quantitDB);

        originalPriceC = Integer.parseInt(priceDB);
        Picasso.get().load(imgDB).into(productIv);
        firstPrice = priceDB;
        quantityFromDB = Integer.parseInt(quantitDB);

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantityTvC < 5) {
                    quantityTvC++;
                    originalPriceC *= quantityTvC;
                }
                originalPrice2 = String.valueOf(originalPriceC);
                originalPrice.setText(originalPriceC+"");

                quantityTv2 = String.valueOf(quantityTvC);
                quantityTv.setText(quantityTv2);
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantityTvC > 1) {
                    quantityTvC--;
                    originalPriceC /= quantityTvC;
                }else if (quantityTvC <= 1){
                    originalPrice.setText(firstPrice);
                }
                originalPrice2 = String.valueOf(originalPriceC);
                originalPrice.setText(originalPrice2+"");
                quantityTv2 = String.valueOf(quantityTvC);
                quantityTv.setText(quantityTv2);
            }
        });
//        add to cart
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int desquantitDB = quantityFromDB - quantityTvC;
                insertProductToCartData();
            }
        });
    }

    private void insertProductToCartData() {
        PCarts productToCart = new PCarts(imgDB,nameDB,originalPriceC,quantityTvC);
        rootCartdatabaseReference.child(userId).push().setValue(productToCart);
        Toast.makeText(this, "Product added to cart !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this,MainStore.class);
        startActivity(intent3);
    }
}