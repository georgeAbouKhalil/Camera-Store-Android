package com.example.camerastore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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

        String priceS = Integer.toString(priceI);
        String quantityS = Integer.toString(quantityI);

        holder.name.setText("Product name:" + model.getName());
        holder.price.setText("Product price:" + priceS);
        holder.quantity.setText("Product quantity:" + quantityS);
        Glide.with(holder.img.getContext())
                .load(model.getImageUrl())
                .into(holder.img);
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

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.nametextrec);
            price = itemView.findViewById(R.id.pricetextrec);
            quantity = itemView.findViewById(R.id.qtytextrec);
        }
    }
}
