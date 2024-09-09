package com.example.myapplication.model;

public class OrderItem {
    private int  orderItemId;
    private int  orderId;
    private int  menuItemId;
    private int quantity;
    private Double price;

    public OrderItem() {
    }

    public OrderItem(int orderId, int menuItemId, int quantity, Double price) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.price = price;
    }

//    public OrderItem(int orderItemId, int orderId, int menuItemId, int quantity, Double price) {
//        this.orderItemId = orderItemId;
//        this.orderId = orderId;
//        this.menuItemId = menuItemId;
//        this.quantity = quantity;
//        this.price = price;
//    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
