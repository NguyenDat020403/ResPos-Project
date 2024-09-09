package com.example.myapplication;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import static com.example.myapplication.R.drawable.border_menu_left;
import static com.example.myapplication.R.drawable.border_note_order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.adapter.FoodAdapter;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.adapter.ViewPagerAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderItem;
import com.example.myapplication.model.Table;
import com.example.myapplication.viewModel.FoodViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.type.DateTime;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static ActivityMainBinding binding;
    public static List<Food> listFood;
    public static List<OrderItem> listOrder;
    public static OrderAdapter orderAdapter;
    private Table table;
    public static TextView totalBill;
    public static ApiService apiService;
    public static boolean checkOrder = false;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        totalBill = findViewById(R.id.txtTotalBill);
        listFood = new ArrayList<>();
        listOrder = new ArrayList<>();
        binding.txtTotalBill.setText( "0 VNĐ");
        Intent i = getIntent();
        table = (Table) i.getSerializableExtra("table");
        binding.txtTableNumber.setText(String.valueOf(table.getTableNumber()));
        apiService = ApiClient.getClient().create(ApiService.class);

        // Improved error handling for order creation
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(new Date());
        Order newOrder = new Order(1, 1, formattedDate, new BigDecimal(150000.0), "Đang chuẩn bị");
        Call<Order> callOrder = apiService.insertNewOrder(newOrder);
        callOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    binding.txtOrderID.setText(String.valueOf(response.body().getOrderId()));
                    Log.d("API_RESPONSE", "Order inserted successfully: " + response.body().getStatus());
                    checkOrder = true;
                } else {
                    Log.d("API_RESPONSE", "Order insertion failed: " + response.code());
                    // Handle order insertion failure with appropriate error code
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("API_RESPONSE", "Order insertion failed: " + t.getMessage());
                // Handle network or other errors during order insertion
            }
        });




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcyOrderList.setLayoutManager(linearLayoutManager);


        Call<List<Food>> call = apiService.getListFood();
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    List<Food> foodList = response.body();
                    listFood.addAll(foodList);
                    Log.d("foodViewModel.getFoodList()", String.valueOf(listFood.size()));
                    FoodViewModel foodViewModel = new ViewModelProvider(MainActivity.this).get(FoodViewModel.class);
                    foodViewModel.setFoodList(listFood);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, foodViewModel);
                    binding.viewPager.setAdapter(viewPagerAdapter);
                    new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
                        switch (position) {
                            case 0:
                                tab.setText("Khai vị");
                                break;
                            case 1:
                                tab.setText("Món chính");
                                break;
                            case 2:
                                tab.setText("Tráng miệng");
                                break;
                        }
                    }).attach();                    // Notify the adapter here if needed
                } else {
                    Log.d("API_RESPONSE", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.e("API_RESPONSE", "Failure: " + t.getMessage());
            }
        });

        Log.d("foodViewModel.getFoodList()", String.valueOf(listFood.size()));




        FoodAdapter foodAdapter = new FoodAdapter(listFood, this, food -> {
            addToOrder(food);
        });
        orderAdapter = new OrderAdapter(listOrder);
        binding.rcyOrderList.setAdapter(orderAdapter);

        addEvents();
        binding.btnOrderNow.setOnClickListener(v -> {
            if(listOrder.isEmpty()){
                Toast.makeText(this, "Vui lòng chọn món ăn", Toast.LENGTH_LONG).show();
                binding.btnOrderNow.setClickable(false);
            }else{
                if(checkOrder){
                    for(OrderItem o: listOrder){
                        Call<OrderItem> callOrderItem = apiService.insertOrderItem(o);
                        callOrderItem.enqueue(new Callback<OrderItem>() {
                            @Override
                            public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        Log.d("API_RESPONSE", "Order item inserted successfully: " + response.body().getPrice().toString());
                                    } else {
                                        Log.d("API_RESPONSE", "Response body is null");
                                    }
                                } else {
                                    Log.d("API_RESPONSE", "Order item insertion failed: " + response.code() + " - " + response.message());
                                    // Handle order item insertion failure with appropriate error code
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderItem> call, Throwable t) {
                                Log.e("API_RESPONSE", "Order item insertion failed: " + t.getMessage());
                                // Handle network or other errors during order item insertion
                            }
                        });
                    }
                    binding.btnOrderNow.setBackgroundResource(border_note_order);
                    binding.btnOrderNow.setClickable(false);
                    listOrder.clear();
                    orderAdapter.notifyDataSetChanged();
                }else{
                    Log.d("Orders ", "Loading...");

                }
            }


        });

    }



    @SuppressLint("NotifyDataSetChanged")
    public static void addToOrder(Food food) {
        binding.btnOrderNow.setClickable(true);
        binding.btnOrderNow.setBackgroundColor(BLUE);

        int check = 1;
        Double total = (double) 0;
        if(listOrder.isEmpty()){
            OrderItem item = new OrderItem(Integer.parseInt(binding.txtOrderID.getText().toString()),food.getFoodID(),1,food.getFoodPrice());
            listOrder.add(item);
        }else{
            for(OrderItem o : listOrder){
                if(o.getMenuItemId() == food.getFoodID()) {
                    o.setQuantity(o.getQuantity() + 1);
                    check = 0;
                    break;
                }
                check = 1;
            }
            if(check == 1 ){
                OrderItem item = new OrderItem(Integer.parseInt(binding.txtOrderID.getText().toString()),food.getFoodID(),1,food.getFoodPrice());
                listOrder.add(item);
            }


        }
        for(OrderItem o : listOrder){
            total += Double.valueOf(o.getQuantity()) * o.getPrice();
        }
        totalBill.setText(String.valueOf(total));
        orderAdapter.notifyDataSetChanged();

    }



    private void addEvents() {
        binding.navItemHome.setSelected(true);
        binding.navItemHome.setOnClickListener(view -> {
            selectItem(binding.navItemHome);
            binding.home.setColorFilter(WHITE);
        });
        binding.navItemSearch.setOnClickListener(view -> {
            selectItem(binding.navItemSearch);
            binding.search.setColorFilter(WHITE);
        });
        binding.navItemCallStaff.setOnClickListener(view -> {
            selectItem(binding.navItemCallStaff);
            binding.callStaff.setColorFilter(WHITE);

        });
    }
    private void selectItem(LinearLayout selectedItem) {
        // Bỏ chọn tất cả các item trước đó
        binding.navItemHome.setSelected(false);
        binding.home.setColorFilter(RED);
        binding.navItemSearch.setSelected(false);
        binding.search.setColorFilter(RED);
        binding.navItemCallStaff.setSelected(false);
        binding.callStaff.setColorFilter(RED);

        // Thêm các item khác nếu cần

        // Đặt item được chọn
        selectedItem.setSelected(true);
    }
}