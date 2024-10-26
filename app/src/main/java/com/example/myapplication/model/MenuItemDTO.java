package com.example.myapplication.model;

public class MenuItemDTO {
    private int menuItemID;
    private String itemName;
    private int totalQuantity;
    private String image;

    public MenuItemDTO(int menuItemID, String itemName, int totalQuantity, String image) {
        this.menuItemID = menuItemID;
        this.itemName = itemName;
        this.totalQuantity = totalQuantity;
        this.image = image;
    }

    public int getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
