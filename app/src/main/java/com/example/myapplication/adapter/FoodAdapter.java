package com.example.myapplication.adapter;

import static com.example.myapplication.R.drawable.ic_launcher_foreground;
import static com.example.myapplication.R.drawable.image1;
import static com.example.myapplication.R.drawable.image3;
import static com.example.myapplication.R.drawable.image5;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder>{


    private List<Food> listFoods;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Food food);
    }

    public FoodAdapter(List<Food> listFoods, Context context, OnItemClickListener onItemClickListener) {
        this.listFoods = listFoods;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_food,parent,false);

        return new FoodHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Food food = listFoods.get(position);
        if(food == null){
            return;
        }
        holder.txtProductName.setText(food.getFoodName());
        holder.txtProductPrice.setText(String.valueOf(food.getFoodPrice()));
        if(food.getFoodImageBytes() != null){
            byte[] imageBytes = food.getFoodImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            holder.imgProductPhoto.setImageBitmap(bitmap);
        }else {
            Picasso.get()
                    .load(image5)
                    .into(holder.imgProductPhoto);
        }
//        if(food.getFoodImage().equals("1") ){
//            Picasso.get()
//                    .load(image1)
//                    .into(holder.imgProductPhoto);
//        }else
//            Picasso.get()
//                    .load(image5)
//                    .into(holder.imgProductPhoto);
//

//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if(isLongClick)
//                    Toast.makeText(context, "Long Click: "+listFoods.get(position), Toast.LENGTH_SHORT).show();
//                else
//                    onItemClickListener.onItemClick(food);
//            }
//        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick)
                    Toast.makeText(context, "Long Click: "+listFoods.get(position), Toast.LENGTH_SHORT).show();
                else
                    showFoodDetailsDialog(food);
            }
        });
    }

    private void showFoodDetailsDialog(Food food) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_details_item);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView imgFood = dialog.findViewById(R.id.imgFood);
        TextView txtFoodName = dialog.findViewById(R.id.txtFoodName);
        TextView txtFoodPrice = dialog.findViewById(R.id.txtFoodPrice);
        TextView txtFoodDesc = dialog.findViewById(R.id.txtFoodDesc);
        Button btnAddToOrder = dialog.findViewById(R.id.btnAddToOrder);

//        if(food.getFoodImage().equals("1") ){
            Picasso.get()
                    .load(image1)
                    .into(imgFood);
//        }else
//            Picasso.get()
//                    .load(image5)
//                    .into(imgFood);
        txtFoodName.setText(food.getFoodName());
        txtFoodPrice.setText(String.valueOf(food.getFoodPrice()) + " VNĐ");
        txtFoodDesc.setText(food.getFoodDescription());
        btnAddToOrder.setOnClickListener(v -> {
            MainActivity.addToOrder(food);
            dialog.dismiss();
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(dialog.getWindow().getAttributes());

        params.width =  WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(params);

        dialog.show();
    }

    public void updateFoodList(List<Food> newFoodList) {
        listFoods = newFoodList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(listFoods != null){
            return listFoods.size();
        }
        return 0;
    }


    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }

    class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView txtProductName, txtProductPrice;
        private ImageView imgProductPhoto;
        private ItemClickListener itemClickListener;



        public FoodHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductPrice = itemView.findViewById(R.id.product_price);
            imgProductPhoto = itemView.findViewById(R.id.product_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return true;
        }
    }
}
