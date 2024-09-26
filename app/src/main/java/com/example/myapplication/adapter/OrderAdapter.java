package com.example.myapplication.adapter;

import static com.example.myapplication.MainActivity.listOrder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder>{

    private List<OrderItem> orderItemList;

    public OrderAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_food,parent,false);

        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderItem order = orderItemList.get(position);
        if(order == null){
            return;
        }
        for (Food f : MainActivity.listFood) {
            if(f.getFoodID() == order.getMenuItemId()){
                holder.txtProductName.setText(f.getFoodName());
                holder.txtProductPrice.setText(String.valueOf(order.getPrice()));
//                if(f.getFoodImage() == "1" ){
//                    holder.imgProductPhoto.setImageResource(R.drawable.image1);
//                }if(f.getFoodImage() == "2" ){
//                    holder.imgProductPhoto.setImageResource(R.drawable.image3);
//                }if(f.getFoodImage() == "3" ){
                    holder.imgProductPhoto.setImageResource(R.drawable.image5);
//                }
                holder.txtTotalPrice.setText(String.valueOf(order.getPrice() * order.getQuantity()));
                holder.txtItemQty.setText(String.valueOf(order.getQuantity()));
                holder.imvAddItem.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View view) {
                        order.setQuantity(order.getQuantity() +1);
                        orderItemList.get(position).setQuantity(order.getQuantity());
                        holder.txtItemQty.setText(String.valueOf(order.getQuantity()));
                        Double total = (double) 0;
                        for(OrderItem o : listOrder){
                            total += o.getQuantity() * o.getPrice();
                        }
                        MainActivity.totalBill.setText(String.valueOf(total));
                        MainActivity.orderAdapter.notifyDataSetChanged();
                    }
                });
                holder.imvRemoveItem.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View view) {
                        order.setQuantity(order.getQuantity() -1);
                        if (order.getQuantity() == 0){
                            removeItemOderList(order);
                        }else{
                            orderItemList.get(position).setQuantity(order.getQuantity());
                            holder.txtItemQty.setText(String.valueOf(order.getQuantity()));
                            MainActivity.orderAdapter.notifyDataSetChanged();
                        }

                    }
                });

                holder.imvRemoveItemOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeItemOderList(order);
                    }
                });
                break;

            }

        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private void removeItemOderList(OrderItem order) {
        listOrder.remove(order);
        MainActivity.orderAdapter.notifyDataSetChanged();
        if(orderItemList.isEmpty()) MainActivity.totalBill.setText("0 VNĐ");
        else for(OrderItem o : orderItemList){
            MainActivity.totalBill.setText(String.valueOf(o.getQuantity() * o.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        if(orderItemList != null){
            return orderItemList.size();
        }
        return 0;
    }

    static class OrderHolder extends RecyclerView.ViewHolder{

        private TextView txtProductName, txtProductPrice, txtTotalPrice;
        public TextView txtItemQty;
        private ImageView imgProductPhoto, imvRemoveItemOrder;
        private ImageView imvRemoveItem, imvAddItem;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtItemName);
            txtProductPrice = itemView.findViewById(R.id.txtItemPrice);
            imgProductPhoto = itemView.findViewById(R.id.imvItemPhoto);
            txtTotalPrice = itemView.findViewById(R.id.txtItemTotal);
            txtItemQty = itemView.findViewById(R.id.txtItemQty);
            imvRemoveItemOrder = itemView.findViewById(R.id.imvRemoveItemOrder);
            imvRemoveItem = itemView.findViewById(R.id.imvRemoveItem);
            imvAddItem = itemView.findViewById(R.id.imvAddItem);
        }
    }
}
