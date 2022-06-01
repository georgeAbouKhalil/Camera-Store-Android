package com.example.camerastore;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter extends FirebaseRecyclerAdapter<model,Adapter.myviewholder>
{

    public Adapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {

        int priceI = model.getPrice();
        int quantityI = model.getQuantity();

        String priceS = Integer.toString(priceI);
        String quantityS = Integer.toString(quantityI);

        holder.name.setText(model.getName());
        holder.price.setText(priceS);
        if(quantityI > 0){
            holder.quantity.setText(": In stock");
        }else{
            holder.quantity.setText("❌");
        }
        Glide.with(holder.img.getContext()).load(model.getImageUrl()).into(holder.img);

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new Details(model.getName(),priceS,quantityS,model.getImageUrl())).addToBackStack(null).commit();

                }
            });

    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid_layout,parent,false);
        return new myviewholder(view);

    }


    class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,price,quantity;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageView2);
            name = (TextView) itemView.findViewById(R.id.textView2);
            price = (TextView) itemView.findViewById(R.id.textPrice);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
        }


    }

}


