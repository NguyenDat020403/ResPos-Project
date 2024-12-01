package com.example.myapplication.adapter;

import static com.example.myapplication.R.drawable.image5;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.MenuItemDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemTopAdapter extends RecyclerView.Adapter<ItemTopAdapter.MenuItemViewHolder>{
    private List<MenuItemDTO> menuItemList;

    public ItemTopAdapter(List<MenuItemDTO> menuItemList) {
        this.menuItemList = menuItemList;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<MenuItemDTO> newList) {
        this.menuItemList.clear();
        this.menuItemList.addAll(newList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_order, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {

        MenuItemDTO menuItem = menuItemList.get(position);
        holder.txtItemName.setText(menuItem.getItemName());
        holder.txtTotalQuantity.setText(String.valueOf(menuItem.getTotalQuantity()));
        if(menuItem.getImage() != null){
            Picasso.get()
                    .load("https://resmant11111-001-site1.anytempurl.com/uploads/" + menuItem.getImage())
                    .into(holder.imvItemImage);
        }else {
            Picasso.get()
                    .load(image5)
                    .into(holder.imvItemImage);
        }
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName, txtTotalQuantity;
        ImageView imvItemImage;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtTotalQuantity = itemView.findViewById(R.id.txtTotalQuantity);
            imvItemImage = itemView.findViewById(R.id.imvItemPhoto);
        }
    }
}
