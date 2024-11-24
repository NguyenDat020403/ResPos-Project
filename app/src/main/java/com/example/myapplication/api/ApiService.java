package com.example.myapplication.api;

import com.example.myapplication.model.Food;
import com.example.myapplication.model.MenuItemDTO;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderItem;
import com.example.myapplication.model.Table;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("Menu/List")
    Call<List<Food>> getListFood();
    @GET("Table/List")
    Call<List<Table>> getListTable();
    @POST("Order/Insert")
    Call<Order> insertNewOrder(@Body Order order);
    @POST("OrderItem/Insert")
    Call<OrderItem> insertOrderItem(@Body OrderItem orderItem);

    @PUT("Order/Update")
    Call<Order> updateOrder(@Body Order order);

    @PUT("Table/UpdateStatus")
    Call<Table> updateTableStatus(@Query("tableID") int tableID, @Query("status") String status);

    @GET("Menu/GetTop5OrderByQuantity")
    Call<List<MenuItemDTO>> getTop5OrderByQuantity();

    @GET("OrderItem/ListByOrderId/{orderId}")
    Call<List<OrderItem>> getOrderItemByOrderId(@Path("orderId") int orderId);
}
