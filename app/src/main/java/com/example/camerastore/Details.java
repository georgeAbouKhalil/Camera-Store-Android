package com.example.camerastore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class Details extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String name,ImageUrl;
    int price,quantity;


    String priceS = Integer.toString(price);
    String quantityS = Integer.toString(quantity);

    private String mParam1;
    private String mParam2;

    public Details() {

    }

    public Details(String name, String priceS, String quantityS, String ImageUrl){
        this.name = name;
        this.priceS = priceS;
        this.quantityS = quantityS;
        this.ImageUrl = ImageUrl;
    }



    public static Details newInstance(String param1, String param2){
        Details fragmet = new Details();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragmet.setArguments(args);
        return fragmet;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container ,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_details, container,false);

        ImageView imageHolder = view.findViewById(R.id.detailImage);
        TextView nameHolder = view.findViewById(R.id.detailTitle);
        TextView priceHolder = view.findViewById(R.id.detailPrice);
        TextView quantityHolder = view.findViewById(R.id.textView10);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = fAuth.getCurrentUser();

        Button button_buy = view.findViewById(R.id.button_buy);
        Button add_cart = view.findViewById(R.id.add_cart);
        nameHolder.setText(name);
        priceHolder.setText(priceS);

        if(mFirebaseUser != null){
            if(!mFirebaseUser.isEmailVerified()){
                Toast.makeText(getActivity(), "go Verified to access the payment", Toast.LENGTH_SHORT).show();
            }else{
                quantityHolder.setText("In Stock: " + quantityS);
                button_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // product details transfer to the visa
                        Intent transferItems = new Intent(getActivity(),visa.class);
                        transferItems.putExtra("IMAGE",ImageUrl);
                        transferItems.putExtra("NAME",name);
                        transferItems.putExtra("PRICE",priceS);
                        transferItems.putExtra("quantity",quantityS);
                        Log.d("IMGS2", ImageUrl);
                        startActivity(transferItems);
                    }
                });
                add_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent transferItemstoCart = new Intent(getActivity(),addToCarts.class);
                        transferItemstoCart.putExtra("IMAGE",ImageUrl);
                        transferItemstoCart.putExtra("NAME",name);
                        transferItemstoCart.putExtra("PRICE",priceS);
                        transferItemstoCart.putExtra("quantity",quantityS);

                        startActivity(transferItemstoCart);
                    }
                });

            }
            if(quantityS.startsWith("0")){
                quantityHolder.setText("it is not in stock");
                button_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "The product is not In stock", Toast.LENGTH_SHORT).show();
                    }
                });
                add_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "The product is not In stock", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                quantityHolder.setText("In Stock: " + quantityS);
            }
        }

        Glide.with(getContext()).load(ImageUrl).into(imageHolder);
        return view;
    }




}