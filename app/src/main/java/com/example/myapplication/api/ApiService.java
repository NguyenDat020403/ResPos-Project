package com.example.myapplication.api;

import com.example.myapplication.model.Food;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderItem;
import com.example.myapplication.model.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("Items/List")
    Call<List<Food>> getListFood();
    @GET("Tables/List")
    Call<List<Table>> getListTable();
    @POST("Orders/Insert")
    Call<Order> insertNewOrder(@Body Order order);
    @POST("OrderItems/Insert")
    Call<OrderItem> insertOrderItem(@Body OrderItem orderItem);

}
