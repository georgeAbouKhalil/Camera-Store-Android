package com.example.camerastore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class cart extends AppCompatActivity {

    RecyclerView recyclerCart;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userId;
    MainAdapter mainAdapter;
    Button removeAll,placeOrder;
    int random = new Random().nextInt((180000 - 18000) + 1) + 18000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove the action bar
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cart);


        recyclerCart = findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        removeAll = findViewById(R.id.removeAll);
        placeOrder = findViewById(R.id.placeOrder);

        //get currect user
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        final FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if(mFirebaseUser != null){
            userId = fAuth.getCurrentUser().getUid();
        }


        FirebaseRecyclerOptions<PCarts> options =
                new FirebaseRecyclerOptions.Builder<PCarts>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("cart").child(userId), PCarts.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerCart.setAdapter(mainAdapter);


        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemCarts(userId);
            }
        });
            placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerCart.getAdapter().getItemCount() >0) {
                        showToast("Order placed");
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(cart.this, "My Notification");
                        builder.setContentTitle("Reception");
                        builder.setContentText("Thanks for buying a camera The price you bought the camera is Reception number is " + random + "If there is any problem or delay you are welcome to contact us we will be happy to help");
                        builder.setSmallIcon(R.drawable.logo);
                        builder.setAutoCancel(true);

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(cart.this);
                        managerCompat.notify(1, builder.build());
                        deleteItemCarts(userId);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), TanksForBaying.class);
                                startActivity(i);
                            }
                        }, 1500);
                    }else{
                        Toast.makeText(cart.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });



    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void deleteItemCarts(String userId) {
        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
        Task<Void> mTask = DbRef.removeValue();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showToast("Items Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Error deleting Item Carts");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this,MainStore.class);
        startActivity(intent3);
    }
}