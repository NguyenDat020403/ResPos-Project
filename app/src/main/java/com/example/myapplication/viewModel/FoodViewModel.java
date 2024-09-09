package com.example.myapplication.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Food;

import java.util.List;

public class FoodViewModel extends ViewModel {
    private final MutableLiveData<List<Food>> foodList = new MutableLiveData<>();

    public LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foods) {
        foodList.setValue(foods);
    }
}
