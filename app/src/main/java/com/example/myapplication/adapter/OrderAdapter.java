package com.example.myapplication.adapter;

import static com.example.myapplication.MainActivity.binding;
import static com.example.myapplication.MainActivity.listOrder;
import static com.example.myapplication.R.drawable.image5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.squareup.picasso.Picasso;

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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderItem order = orderItemList.get(position);
        if(order == null){
            return;
        }
//        holder.edtnoteItemOrder.setText("");
//
//        holder.edtnoteItemOrder.setHint("Order Now...");
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
                        MainActivity.binding.txtTotalBill.setText(String.valueOf(total));
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
                        }
                        Double total = (double) 0;
                        for(OrderItem o : listOrder){
                            total += o.getQuantity() * o.getPrice();
                        }
                        MainActivity.binding.txtTotalBill.setText(String.valueOf(total));
                        MainActivity.orderAdapter.notifyDataSetChanged();
                    }
                });

                holder.imvRemoveItemOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeItemOderList(order);
                    }
                });
                holder.edtnoteItemOrder.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        orderItemList.get(position).setNote(holder.edtnoteItemOrder.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                break;

            }

        }
//
//        if (position == orderItemList.size() - 1 ) {
//            blinkAnimation(holder.itemView);
//        }

    }



    @SuppressLint("NotifyDataSetChanged")
    private void removeItemOderList(OrderItem order) {
        listOrder.remove(order);
        MainActivity.orderAdapter.notifyDataSetChanged();
        if(orderItemList.isEmpty()) MainActivity.binding.txtTotalBill.setText("0 VNĐ");
        else for(OrderItem o : orderItemList){
            MainActivity.binding.txtTotalBill.setText(String.valueOf(o.getQuantity() * o.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        if(orderItemList != null){
            return orderItemList.size();
        }
        return 0;
    }
//    private void blinkAnimation(View view) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
//        animator.setDuration(300);  // Thời gian nhấp nháy
//        animator.setRepeatCount(1); // Số lần lặp lại
//        animator.setRepeatMode(ValueAnimator.REVERSE); // Đảo chiều
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setAlpha(1f); // Giữ item ở trạng thái hiển thị (alpha = 1)
//            }
//        });
//        animator.start();
//    }


    static class OrderHolder extends RecyclerView.ViewHolder{

        private TextView txtProductName, txtProductPrice, txtTotalPrice;
        public TextView txtItemQty;
        private EditText edtnoteItemOrder;
        private ImageView imgProductPhoto, imvRemoveItemOrder;
        private ImageView imvRemoveItem, imvAddItem;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtItemName);
            txtProductPrice = itemView.findViewById(R.id.txtItemPrice);
            imgProductPhoto = itemView.findViewById(R.id.imvItemPhoto);
            txtTotalPrice = itemView.findViewById(R.id.txtItemTotal);
            txtItemQty = itemView.findViewById(R.id.txtItemQty);
            edtnoteItemOrder = itemView.findViewById(R.id.edtnoteItemOrder);
            imvRemoveItemOrder = itemView.findViewById(R.id.imvRemoveItemOrder);
            imvRemoveItem = itemView.findViewById(R.id.imvRemoveItem);
            imvAddItem = itemView.findViewById(R.id.imvAddItem);
        }
    }
}
