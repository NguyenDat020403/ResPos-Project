package com.example.myapplication.fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.FoodAdapter;
import com.example.myapplication.model.Food;
import com.example.myapplication.viewModel.FoodViewModel;

import java.util.ArrayList;
import java.util.List;

public class MonChinhFragment extends Fragment {
    private FoodViewModel foodViewModel;

    public static MonChinhFragment newInstance(FoodViewModel foodViewModel) {
        MonChinhFragment fragment = new MonChinhFragment();
        fragment.foodViewModel = foodViewModel;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_monchinh,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.rcyListFood);
        FoodAdapter foodAdapter = new FoodAdapter(new ArrayList<>(), getContext(), MainActivity::addToOrder);
        recyclerView.setAdapter(foodAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        foodViewModel.getFoodList().observe(getViewLifecycleOwner(), foodList -> {
            if (foodList != null) {
                Log.d("MonChinhFragment", "Food list size: " + foodList.size());
                List<Food> monchinhList = new ArrayList<>();
                for (Food food : foodList) {
                    if(Integer.parseInt(food.getFoodCategory()) == 2){
                        monchinhList.add(food);
                    }
                }
                foodAdapter.updateFoodList(monchinhList);
            }
        });

        return view;
    }
}
