package com.example.myapplication.model;

import java.io.Serializable;

public class Table implements Serializable {
    private int tableId;
    private String tableNumber;
    private int capacity;
    private String username;
    private String password;
    private String Status;
    public Table() {
    }

    public Table(String tableNumber, int capacity, String username, String password) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.username = username;
        this.password = password;
    }

    public Table(int tableId, String tableNumber, int capacity, String username, String password, String status) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.username = username;
        this.password = password;
        this.Status = status;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
