package com.example.myapplication;

import static android.graphics.Color.WHITE;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.adapter.ItemTopAdapter;
import com.example.myapplication.adapter.MyOrderAdapter;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.adapter.OrderSummaryListAdapter;
import com.example.myapplication.adapter.ViewPagerAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.MenuItemDTO;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderItem;
import com.example.myapplication.model.Table;
import com.example.myapplication.viewModel.FoodViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static ActivityMainBinding binding;
    public static List<Food> listFood;
    public static List<OrderItem> listOrder;
    public static List<MenuItemDTO> listTopOrder;
    public static List<OrderItem> myOrderList;
    private Table table;
    public static OrderAdapter orderAdapter;
    public static ItemTopAdapter itemTopAdapter;
    public static OrderSummaryListAdapter orderSummaryListAdapter;
    public static MyOrderAdapter myOrderAdapter;
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

        orderNow();
        addEvents();

    }

    private void orderNow() {
        binding.lnOrderNow.setOnClickListener(v -> {
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
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
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
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.e("API_RESPONSE", "Order  insertion failed: " + t.getMessage());
            }
        });
    }


    private void initM() {
        Intent intent = getIntent();
        table = (Table) intent.getSerializableExtra("tableData");
        binding.txtTableNumber.setText(Objects.requireNonNull(table).getTableNumber());
        myOrderList = new ArrayList<>();
        listTopOrder = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcyTop5Order.setLayoutManager(linearLayoutManager);
        //TopItemList
        itemTopAdapter = new ItemTopAdapter(listTopOrder);
        binding.rcyTop5Order.setAdapter(itemTopAdapter);
        //OrderListSummary
        LinearLayoutManager orderSummaryLayoutManager = new LinearLayoutManager(this);
        binding.rcyOrderSummaryList.setLayoutManager(orderSummaryLayoutManager);
        //MyOrderList
        LinearLayoutManager myOrderLayoutManager = new LinearLayoutManager(this);
        binding.rcyMyOrdersDetail.setLayoutManager(myOrderLayoutManager);
        myOrderAdapter = new MyOrderAdapter(myOrderList);
        binding.rcyMyOrdersDetail.setAdapter(myOrderAdapter);
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

        binding.btnContinue.setOnClickListener(v-> showOrderSummaryList() );
        binding.hideOrderList.setOnClickListener(v-> showTop5OrderTheMost());
        binding.myOrder.setOnClickListener(v->{
            showMyOrders();
        });
        binding.lnEditOrder.setOnClickListener(v-> {
            binding.overlay.setVisibility(View.GONE);
            showOrderListAdding();
        });
        binding.imvOrderSummaryBack.setOnClickListener(v->{
            binding.overlay.setVisibility(View.GONE);
            showOrderListAdding();
        });
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        binding.imvMyOrderBack.setOnClickListener(v->{
            binding.myOrderContainer.startAnimation(slideOut);
            binding.myOrderContainer.setVisibility(View.GONE);
            binding.overlay.setVisibility(View.GONE);
            getTop5Order();
            binding.rightContainer.startAnimation(slideIn);
            binding.rightContainer.setVisibility(View.VISIBLE);

        });
        binding.lnContinueOrder.setOnClickListener(v->{

            binding.myOrderContainer.startAnimation(slideOut);
            binding.myOrderContainer.setVisibility(View.GONE);
            binding.overlay.setVisibility(View.GONE);
            binding.orderContain.startAnimation(slideIn);
            binding.orderContain.setVisibility(View.VISIBLE);

        });
    }
    private void showMyOrders(){
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.rightContainer.setVisibility(View.GONE);
                binding.rightContainerOrderDetail.setVisibility(View.GONE);
                binding.orderContain.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                getOrderItemByOrderId();
                binding.overlay.setVisibility(View.VISIBLE);
                binding.myOrderContainer.setVisibility(View.VISIBLE);
                binding.txtMyOrderID.setText(binding.txtOrderID.getText());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.rightContainer.startAnimation(slideOut);
        binding.myOrderContainer.startAnimation(slideIn);
    }
    //Show Right
    private void showOrderListAdding() {
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.myOrderContainer.setVisibility(View.GONE);
                    binding.rightContainer.setVisibility(View.GONE);
                    binding.rightContainerOrderDetail.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.orderContain.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        binding.rightContainerOrderDetail.startAnimation(slideOut);
        binding.orderContain.startAnimation(slideIn);
    }

    private void showOrderSummaryList() {
        Animation slideOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right);
        Animation slideIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.myOrderContainer.setVisibility(View.GONE);
                    binding.rightContainer.setVisibility(View.GONE);
                    binding.orderContain.setVisibility(View.GONE);
                }

                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.rightContainerOrderDetail.setVisibility(View.VISIBLE);
                    binding.txtOrderConfirmationID.setText("Orders: #"+binding.txtOrderID.getText());
                    binding.rcyOrderSummaryList.setAdapter(orderSummaryListAdapter);
                    binding.overlay.setVisibility(View.VISIBLE);
                    orderSummaryListAdapter = new OrderSummaryListAdapter(listOrder);
                    orderSummaryListAdapter.notifyDataSetChanged();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            binding.orderContain.startAnimation(slideOut);
            binding.rightContainerOrderDetail.startAnimation(slideIn);

    }

    private void showTop5OrderTheMost() {

        Animation slideOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right);
        Animation slideIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);
            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.orderContain.setVisibility(View.GONE);
                    binding.myOrderContainer.setVisibility(View.GONE);
                    binding.rightContainerOrderDetail.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.rightContainer.setVisibility(View.VISIBLE);
                    getTop5Order();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            binding.orderContain.startAnimation(slideOut);
            binding.rightContainer.startAnimation(slideIn);
    }
    private void getOrderItemByOrderId(){
        Call<List<OrderItem>> call = MainActivity.apiService.getOrderItemByOrderId(Integer.parseInt(binding.txtOrderID.getText().toString()));
        call.enqueue(new Callback<List<OrderItem>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if(response.isSuccessful() && response.body() != null){
                    myOrderList.clear();
                    myOrderList.addAll(response.body());
                    myOrderAdapter.notifyDataSetChanged();
                    double totalBill = (double) 0;
                    for(int i = 0; i < response.body().size();i++){
                        totalBill += response.body().get(i).getQuantity() * response.body().get(i).getPrice();
                    }
                    binding.txtMyOrdersTotalBill.setText(String.valueOf(totalBill) );
                }else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {

            }
        });
    }

    private void getTop5Order() {
        Call<List<MenuItemDTO>> call = MainActivity.apiService.getTop5OrderByQuantity();
        call.enqueue(new Callback<List<MenuItemDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<MenuItemDTO>> call, @NonNull Response<List<MenuItemDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Clear the existing list and add the new data
                    listTopOrder.clear();
                    listTopOrder.addAll(response.body());
                    itemTopAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MenuItemDTO>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initOrderList() {
        listOrder = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcyOrderList.setLayoutManager(linearLayoutManager);
        orderAdapter = new OrderAdapter(listOrder);
        binding.rcyOrderList.setAdapter(orderAdapter);
        LinearLayoutManager orderSummaryLayoutManager = new LinearLayoutManager(this);
        binding.rcyOrderSummaryList.setLayoutManager(orderSummaryLayoutManager);
        orderSummaryListAdapter = new OrderSummaryListAdapter(listOrder);
        orderSummaryListAdapter.notifyDataSetChanged();
    }


    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void insertOrderItem() {
        for(OrderItem o: MainActivity.listOrder){

            Call<OrderItem> callOrderItem = apiService.insertOrderItem(o);
            callOrderItem.enqueue(new Callback<OrderItem>() {
                @Override
                public void onResponse(@NonNull Call<OrderItem> call, @NonNull Response<OrderItem> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Order Success!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Order Fail!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderItem> call, @NonNull Throwable t) {
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
            public void onResponse(@NonNull Call<List<Food>> call, @NonNull Response<List<Food>> response) {
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
                                tab.setText("Appetizers");
                                break;
                            case 1:
                                tab.setText("Main Courses");
                                break;
                            case 2:
                                tab.setText("Desserts");
                                break;
                        }
                    }).attach();
                } else {
                    Log.d("API_RESPONSE", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Food>> call, @NonNull Throwable t) {
                Log.e("API_RESPONSE", "Failure: " + t.getMessage());
            }
        });
    }

    private void createNewOrder(ApiService apiService, Table table) {
        newOrder = new Order(table.getTableId() , "Pending");
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("E, MMM d, yyyy").format(Calendar.getInstance().getTime());
        binding.txtDateNow.setText(timeStamp);
        Call<Order> callOrder = apiService.insertNewOrder(newOrder);
        callOrder.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    newOrder = response.body();
                    assert response.body() != null;
                    binding.txtOrderID.setText(String.valueOf(response.body().getOrderId()));
                    updateTableToUnavailable(table.getTableId());
                    Log.d("API_RESPONSE", "Order inserted successfully: " + response.body().getStatus());
                    checkOrder = true;
                } else {
                    Log.d("API_RESPONSE", "Order insertion failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.e("API_RESPONSE", "Order insertion failed: " + t.getMessage());
            }
        });
    }

    private void updateTableToUnavailable(int tableId) {

        Call<Table> updateTableStatus = apiService.updateTableStatus(tableId,"Unavailable");
        updateTableStatus.enqueue(new Callback<Table>() {
            @Override
            public void onResponse(@NonNull Call<Table> call, @NonNull Response<Table> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Table successfully");
                    checkOrder = true;
                } else {
                    Log.d("API_RESPONSE", "Table update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Table> call, @NonNull Throwable t) {
                Log.e("API_RESPONSE", "Table u failed: " + t.getMessage());
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    public static   void addToOrder(Food food) {
        binding.orderContain.setVisibility(View.VISIBLE);
        binding.rightContainerOrderDetail.setVisibility(View.GONE);
        binding.rightContainer.setVisibility(View.GONE);
        int check = 1;
        Double total = (double) 0;
        if(listOrder.isEmpty()){
            binding.txtIsEmptyList.setVisibility(View.GONE);
            binding.rcyOrderList.setVisibility(View.VISIBLE);
            OrderItem item = new OrderItem(Integer.parseInt(binding.txtOrderID.getText().toString()),food.getFoodID(),1,food.getFoodPrice(),"","Ordered");
            listOrder.add(item);
        }else{
            for(OrderItem o : listOrder){
                if(o.getMenuItemId() == food.getFoodID()) {
                    o.setQuantity(o.getQuantity() + 1);
                    check = 0;
                    break;
                }
            }
            if(check == 1 ){
                OrderItem item = new OrderItem(Integer.parseInt(binding.txtOrderID.getText().toString()),food.getFoodID(),1,food.getFoodPrice(),"","Ordered");
                listOrder.add(item);
            }


        }
        for(OrderItem o : listOrder){
            total += o.getQuantity() * o.getPrice();
        }
        binding.txtTotalBill.setText(String.valueOf(total));
        orderAdapter.notifyDataSetChanged();
        orderSummaryListAdapter.notifyDataSetChanged();
    }

    private void checkFoodUpdate() {
        HubConnection hubConnection = HubConnectionBuilder.create("https://resmant11111-001-site1.anytempurl.com/menuHub")
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
        });
        binding.navItemNewOrder.setOnClickListener(view ->{
            selectItem(binding.navItemNewOrder);
            binding.newOrder.setColorFilter(WHITE);
            startActivity(new Intent(MainActivity.this, MainActivity.class));
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