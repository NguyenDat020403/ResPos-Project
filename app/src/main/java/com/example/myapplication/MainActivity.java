package com.example.myapplication;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

import static com.example.myapplication.R.drawable.border_note_order;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
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
import com.google.android.material.tabs.TabLayout;
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
    public Order newOrder;
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
            String totalBill = binding.txtTotalBill.getText().toString();
            if(listOrder.isEmpty()){
                Toast.makeText(this, "Vui lòng chọn món ăn", Toast.LENGTH_LONG).show();
            }else{
                if(checkOrder){
                    updateOrderTotalAmount(totalBill);
                    insertOrderItem();
                }else{
                    Log.d("Orders ", "Loading...");
                }
            }
        });
    }

    private void updateOrderTotalAmount(String totalBill) {
        if(newOrder.getTotalAmount() != null){
            BigDecimal total = newOrder.getTotalAmount().add(BigDecimal.valueOf(Double.parseDouble(totalBill)));
            newOrder.setTotalAmount(total );
        }else {
            newOrder.setTotalAmount(BigDecimal.valueOf(Double.parseDouble(totalBill)));
        }
        Call<Order> callOrder= apiService.updateOrder(newOrder);
        callOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("API_RESPONSE 1111111", "Order  inserted successfully: " + response.body().getTotalAmount());

                    } else {
                        Log.d("API_RESPONSE", "Response body is null");
                    }
                } else {
                    Log.d("API_RESPONSE", "Order  insertion failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("API_RESPONSE", "Order  insertion failed: " + t.getMessage());
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
        initOrderList();
        apiService = ApiClient.getClient().create(ApiService.class);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabLayout.setTabTextColors(WHITE, Color.parseColor("#FEA116"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Animation slideOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right);
        Animation slideIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);

        binding.hideOrderList.setOnClickListener(v->{
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.orderContain.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.rightContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            binding.orderContain.startAnimation(slideOut);
            binding.rightContainer.startAnimation(slideIn);
        });
        binding.showOrderList.setOnClickListener(v->{
            slideIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.rightContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.rightContainerOrderDetail.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            binding.rightContainer.startAnimation(slideOut);
            binding.orderContain.startAnimation(slideIn);


        });
    }

    private void initOrderList() {
        listOrder = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcyOrderList.setLayoutManager(linearLayoutManager);
        orderAdapter = new OrderAdapter(listOrder);
        binding.rcyOrderList.setAdapter(orderAdapter);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void insertOrderItem() {
        for(OrderItem o: MainActivity.listOrder){

            Call<OrderItem> callOrderItem = apiService.insertOrderItem(o);
            callOrderItem.enqueue(new Callback<OrderItem>() {
                @Override
                public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Order Success!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Order Fail!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderItem> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        binding.txtTotalBill.setText("0 VNĐ");
        listOrder.clear();
        initOrderList();
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
                    }).attach();
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
        newOrder = new Order(table.getTableId() , "Pending");
        Call<Order> callOrder = apiService.insertNewOrder(newOrder);
        callOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    newOrder = response.body();
                    binding.txtOrderID.setText(String.valueOf(response.body().getOrderId()));
                    updateTableToUnavailable(table.getTableId());
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

    private void updateTableToUnavailable(int tableId) {

        Call<Table> updateTableStatus = apiService.updateTableStatus(tableId,"Unavailable");
        updateTableStatus.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(Call<Table> call, Response<Table> response) {
                if (response.isSuccessful()) {
                    Table t = response.body();
                    Log.d("API_RESPONSE", "Table successfully");
                    checkOrder = true;
                } else {
                    Log.d("API_RESPONSE", "Table update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Table> call, Throwable t) {
                Log.e("API_RESPONSE", "Table u failed: " + t.getMessage());
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    public static void addToOrder(Food food) {
        binding.orderContain.setVisibility(View.VISIBLE);
        binding.rightContainerOrderDetail.setVisibility(View.GONE);
        binding.rightContainer.setVisibility(View.GONE);
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
        HubConnection hubConnection = HubConnectionBuilder.create("https://resmant1111-001-site1.jtempurl.com/menuHub") // Sử dụng IP của máy chủ hoặc localhost
                .build();

        hubConnection.on("ReceiveMenuUpdate", (data) -> {
            Log.d("SignalR", "Receive Menu Update: " + data);
            fetchMenu();
        }, String.class);

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
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        });
    }
    private void selectItem(LinearLayout selectedItem) {
        binding.navItemHome.setSelected(false);
        binding.home.setColorFilter(Color.parseColor("#FEA116"));
        binding.navItemSearch.setSelected(false);
        binding.search.setColorFilter(Color.parseColor("#FEA116"));
        binding.navItemCallStaff.setSelected(false);
        binding.callStaff.setColorFilter(Color.parseColor("#FEA116"));

        selectedItem.setSelected(true);
    }
}