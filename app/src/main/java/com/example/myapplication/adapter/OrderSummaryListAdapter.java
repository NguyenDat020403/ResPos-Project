package com.example.myapplication.adapter;

import static com.example.myapplication.MainActivity.listOrder;
import static com.example.myapplication.R.drawable.image5;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderSummaryListAdapter extends RecyclerView.Adapter<OrderSummaryListAdapter.OrderSummaryHolder> {
    private List<OrderItem> orderItemSummaryList;

    public OrderSummaryListAdapter(List<OrderItem> orderItemSummaryList) {
        this.orderItemSummaryList = orderItemSummaryList;
    }

    @NonNull
    @Override
    public OrderSummaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_order_list,parent,false);

        return new OrderSummaryListAdapter.OrderSummaryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryHolder holder, int position) {
        OrderItem order = orderItemSummaryList.get(position);
        if(order == null){
            return;
        }
        for (Food f : MainActivity.listFood) {
            if(f.getFoodID() == order.getMenuItemId()){
                holder.txtProductName.setText(f.getFoodName());
                holder.txtProductPrice.setText(String.valueOf(order.getPrice()));
                if(f.getFoodImage() != null){
                    Picasso.get()
                            .load("https://resmant11111-001-site1.anytempurl.com/uploads/" + f.getFoodImage())
                            .into(holder.imgProductPhoto);
                }else {
                    Picasso.get()
                            .load(image5)
                            .into(holder.imgProductPhoto);
                }
                holder.txtTotalPrice.setText(String.valueOf(order.getPrice() * order.getQuantity()));
                holder.txtItemQty.setText(String.valueOf(order.getQuantity()));
                holder.txtnoteItemOrder.setText(order.getNote());
            }
        }
    }

    @Override
    public int getItemCount() {
        if(orderItemSummaryList != null){
            return orderItemSummaryList.size();
        }
        return 0;
    }

    static class OrderSummaryHolder extends RecyclerView.ViewHolder{

        private TextView txtProductName, txtProductPrice, txtTotalPrice,txtnoteItemOrder;
        public TextView txtItemQty;
        private ImageView imgProductPhoto, imvRemoveItemOrder;
        private ImageView imvRemoveItem, imvAddItem;

        public OrderSummaryHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtItemSummaryName);
            txtProductPrice = itemView.findViewById(R.id.txtItemSummaryPrice);
            imgProductPhoto = itemView.findViewById(R.id.imvItemSummaryPhoto);
            txtTotalPrice = itemView.findViewById(R.id.txtItemSummaryTotal);
            txtItemQty = itemView.findViewById(R.id.txtItemSummaryQty);
            txtnoteItemOrder = itemView.findViewById(R.id.txtnoteItemSummaryOrder);
        }
    }
}
