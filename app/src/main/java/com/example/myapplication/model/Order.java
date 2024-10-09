package com.example.myapplication.model;

import android.util.Log;

import com.google.type.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Order {
    private int  orderId;
    private int tableId;
    private int customerId;
    private String orderTime;
    private BigDecimal totalAmount;
    private String status;

    public Order() {
    }

    public Order(int tableId, String status) {
        this.tableId = tableId;
        this.orderTime = getCurrentTime();
        this.status = status;
    }

    public Order(int orderId, int tableId, int customerId, String orderTime, BigDecimal totalAmount, String status) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.customerId = customerId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String  getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String  orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
