package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public  class Food{
    @SerializedName("menuItemId")
    private int foodID;
    @SerializedName("itemName")
    private String foodName;
    @SerializedName("price")
    private Double foodPrice;
    @SerializedName("category")
    private String foodCategory;
    @SerializedName("description")
    private String foodDescription;
    @SerializedName("image")
    private String foodImage;


    public Food() {
    }

    public Food(int foodID, String foodName,  Double foodPrice, String foodCategory, String foodDescription,String foodImage) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
        this.foodCategory = foodCategory;
        this.foodImage = foodImage;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodImage() {
        return foodImage;
    }
    public byte[] getFoodImageBytes() {
        if (foodImage != null) {
            return Base64.decode(foodImage, Base64.DEFAULT);
        } else {
            return null; // Hoặc trả về một byte array mặc định (ví dụ: new byte[0])
        }
    }


    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
