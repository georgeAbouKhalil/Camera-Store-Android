package com.example.camerastore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<PCarts,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<PCarts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull PCarts model) {
        int priceI = model.getPrice();
        int quantityI = model.getQuantity();
        DatabaseReference rootdatabaseReference,rootCartdatabaseReference;

        rootdatabaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        rootCartdatabaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        String priceS = Integer.toString(priceI);
        String quantityS = Integer.toString(quantityI);



        holder.name.setText("Product name:" + model.getName());
        holder.price.setText("Product price:" + priceS);
        holder.quantity.setText("Product quantity:" + quantityS);
        Glide.with(holder.img.getContext())
                .load(model.getImageUrl())
                .into(holder.img);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be undo");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("quantity",model.getTotalQuantity());
                        rootdatabaseReference.child(model.getName()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                rootCartdatabaseReference.child(holder.userId).child(model.getName()).removeValue();
                                Toast.makeText(holder.name.getContext(), "delete Item Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name,price,quantity;
        Button deleteBtn;
        FirebaseFirestore fStore;
        FirebaseAuth fAuth;
        String userId;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.nametextrec);
            price = itemView.findViewById(R.id.pricetextrec);
            quantity = itemView.findViewById(R.id.qtytextrec);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

            //get currect user
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            final FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
            if(mFirebaseUser != null){
                userId = fAuth.getCurrentUser().getUid();
            }
        }
    }
}
