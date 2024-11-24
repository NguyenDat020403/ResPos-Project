package com.example.myapplication.adapter;

import static com.example.myapplication.R.drawable.image5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderHolder>{
    private List<OrderItem> myOrderList;

    public MyOrderAdapter(List<OrderItem> myOrderList) {
        this.myOrderList = myOrderList;
    }

    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_order_list,parent,false);

        return new MyOrderAdapter.MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int position) {
        OrderItem order = myOrderList.get(position);
        if(order == null){
            return;
        }
        for (Food f : MainActivity.listFood) {
            if(f.getFoodID() == order.getMenuItemId()){
                holder.txtProductName.setText(f.getFoodName());
                holder.txtProductPrice.setText(String.valueOf(order.getPrice()));
                if(f.getFoodImage() != null){
                    Picasso.get()
                            .load("https://resmant1111-001-site1.jtempurl.com/uploads/" + f.getFoodImage())
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
        if(myOrderList != null){
            return myOrderList.size();
        }
        return 0;
    }

    static class MyOrderHolder extends RecyclerView.ViewHolder {

        private TextView txtProductName, txtProductPrice, txtTotalPrice,txtnoteItemOrder;
        public TextView txtItemQty;
        private ImageView imgProductPhoto, imvRemoveItemOrder;
        private ImageView imvRemoveItem, imvAddItem;

        public MyOrderHolder(@NonNull View itemView) {
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
