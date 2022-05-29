package com.example.camerastore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainStore extends AppCompatActivity {

    RecyclerView dataList;
    Adapter adapter;
    Button resendCode,profile_button,btnAll,cameraBtn, sendMailBtn,button;
    TextView welcome_user,verifyMsg;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    ImageView imageView4;
    StorageReference storageReference;
    FrameLayout framLayout;
    CircleImageView carts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove the action bar
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_store);

        imageView4 = findViewById(R.id.imageView4);
        dataList = findViewById(R.id.recyclearView);
        welcome_user = findViewById(R.id.welcome_user);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg2);
        profile_button = findViewById(R.id.profile_button);
        final FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        framLayout=findViewById(R.id.framLayout);
        btnAll = findViewById(R.id.btnAll);
        cameraBtn = findViewById(R.id.cameraBtn);
        sendMailBtn = findViewById(R.id.sendMailBtn);
        button = findViewById(R.id.btn);
        carts = findViewById(R.id.carts);


        //   loadFragmentFirst(new FirstFrag());
        loadfragmentButtonclick(btnAll, sendMailBtn,cameraBtn);


        if(mFirebaseUser != null){
            carts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCart();
                }
            });
        }

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });
        //check if user loggin
        if(mFirebaseUser != null){
            userId = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    welcome_user.setText("Hey, " + value.getString("user name"));
                }
            });
            StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imageView4);
                }
            });

            if(!mFirebaseUser.isEmailVerified()){
                verifyMsg.setVisibility(View.VISIBLE);
                resendCode.setVisibility(View.VISIBLE);

                resendCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mFirebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainStore.this, "Verificatoon Email Has been Sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("tag", "onFailure: Email not sent" + e.getMessage());
                                Toast.makeText(MainStore.this, "Email not sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("products"), model.class)
                        .build();
        dataList.setLayoutManager(gridLayoutManager);
        adapter= new Adapter(options);
        dataList.setAdapter(adapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void openProfile() {
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
    private void openCart() {
        Intent intentToCart = new Intent(this,cart.class);
        startActivity(intentToCart);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    private void loadfragmentButtonclick(Button btnAll, Button btn2, Button btn3) {
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,new FirstFrag()).commit();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,new secondfrag()).commit();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,new Thirdfrag()).commit();
            }
        });
    }
    private boolean loadFragmentFirst(FirstFrag firstFrag)
    {
        if(firstFrag==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,new FirstFrag()).commit();
            return true;
        }
        return false;
    }

    public void openGEmail(View view){

        Intent intent = new Intent(Intent.ACTION_VIEW ,Uri.parse("mailto:" + "CameraStoreCM@gmail.com"));
        startActivity(intent);
    }
    public void openCamera(View view){
        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(open_camera);
    }



}

