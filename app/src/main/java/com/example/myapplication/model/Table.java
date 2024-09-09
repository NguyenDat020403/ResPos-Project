package com.example.myapplication.model;

import java.io.Serializable;

public class Table implements Serializable {
    private int tableId;
    private int tableNumber;
    private int capacity;
    private String location;
    private String username;
    private String password;

    public Table() {
    }

    public Table(int tableNumber, int capacity, String location, String username, String password) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.location = location;
        this.username = username;
        this.password = password;
    }

    public Table(int tableId, int tableNumber, int capacity, String location, String username, String password) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.location = location;
        this.username = username;
        this.password = password;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
