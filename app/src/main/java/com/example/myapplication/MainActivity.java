package com.example.myapplication;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import static com.example.myapplication.R.drawable.border_note_order;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.LinearLayout;
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
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;


import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static ActivityMainBinding binding;
    public static List<Food> listFood;
    public static List<OrderItem> listOrder;
    private Table table;
    public static OrderAdapter orderAdapter;
    public static ApiService apiService;
    public static boolean checkOrder = false;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initM();
        createNewOrder(apiService,table);
        checkFoodUpdate();

        fetchMenu();
        insertOrderList();


        orderNow();
        addEvents();

    }

    private void orderNow() {
        binding.btnOrderNow.setOnClickListener(v -> {
            if(listOrder.isEmpty()){
                Toast.makeText(this, "Vui lòng chọn món ăn", Toast.LENGTH_LONG).show();
                binding.btnOrderNow.setClickable(false);
            }else{
                if(checkOrder){
                    insertOrderItem(listOrder,orderAdapter);
                }else{
                    Log.d("Orders ", "Loading...");
                }
            }


        });
    }

    private void insertOrderList() {
        FoodAdapter foodAdapter = new FoodAdapter(listFood, this, food -> {
            addToOrder(food);
        });
    }

    private void initM() {
        Intent intent = getIntent();
        table = (Table) intent.getSerializableExtra("tableData");
        binding.txtTableNumber.setText(table.getTableNumber());
        listFood = new ArrayList<>();
        listOrder = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcyOrderList.setLayoutManager(linearLayoutManager);
        orderAdapter = new OrderAdapter(listOrder);
        binding.rcyOrderList.setAdapter(orderAdapter);
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void insertOrderItem(List<OrderItem> listOrder, OrderAdapter orderAdapter) {
        for(OrderItem o: MainActivity.listOrder){
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
        binding.txtTotalBill.setText("0 VNĐ");
        listOrder.clear();
        orderAdapter.notifyDataSetChanged();
    }

    private void fetchMenu() {
        listFood.clear();
        Call<List<Food>> call = MainActivity.apiService.getListFood();
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful()) {
                    List<Food> foodList = response.body();
                    MainActivity.listFood.addAll(foodList);
                    Log.d("foodViewModel.getFoodList()", String.valueOf(MainActivity.listFood.size()));
                    FoodViewModel foodViewModel = new ViewModelProvider(MainActivity.this).get(FoodViewModel.class);
                    foodViewModel.setFoodList(MainActivity.listFood);
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
    }

    private void createNewOrder(ApiService apiService, Table table) {
        Order newOrder = new Order(table.getTableId() , new BigDecimal(10000), "Pending");


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
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("API_RESPONSE", "Order insertion failed: " + t.getMessage());
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
            OrderItem item = new OrderItem(Integer.parseInt(binding.txtOrderID.getText().toString()),food.getFoodID(),1,food.getFoodPrice(),"");
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
                OrderItem item = new OrderItem(Integer.parseInt(binding.txtOrderID.getText().toString()),food.getFoodID(),1,food.getFoodPrice(),"");
                listOrder.add(item);
            }


        }
        for(OrderItem o : listOrder){
            total += o.getQuantity() * o.getPrice();
        }
        binding.txtTotalBill.setText(String.valueOf(total));
        orderAdapter.notifyDataSetChanged();

    }

    private void checkFoodUpdate() {
        HubConnection hubConnection = HubConnectionBuilder.create("http://10.0.2.2:5134/menuItemHub") // Sử dụng IP của máy chủ hoặc localhost
                .build();

        hubConnection.on("ReceiveMenuUpdate", (data) -> {
            Log.d("SignalR", "Receive Menu Update: " + data);
            fetchMenu();
        }, String.class);

// Bắt đầu kết nối
        new Thread(() -> {
            try {
                hubConnection.start();
                Log.d("SignalR", "Connected to SignalR hub!");
            } catch (Exception e) {
                Log.e("SignalR", "Error connecting to hub", e);
            }
        }).start();
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