package com.example.myapplication.fragment;


import static com.example.myapplication.MainActivity.addToOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FoodAdapter;
import com.example.myapplication.model.Food;
import com.example.myapplication.viewModel.FoodViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrangMiengFragment extends Fragment {

    private FoodViewModel foodViewModel;

    public static TrangMiengFragment newInstance(FoodViewModel foodViewModel) {
        TrangMiengFragment fragment = new TrangMiengFragment();
        fragment.foodViewModel = foodViewModel;
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_trangmieng,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.rcyListFood);
        FoodAdapter foodAdapter = new FoodAdapter(new ArrayList<>(), getContext(), food -> {
            addToOrder(food);
        });
        recyclerView.setAdapter(foodAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        foodViewModel.getFoodList().observe(getViewLifecycleOwner(), foodList -> {
            if (foodList != null) {
                List<Food> monchinhList = new ArrayList<>();
                for (Food food : foodList) {
                    if(food.getFoodID() == 2){
                        monchinhList.add(food);
                    }
                }
                foodAdapter.updateFoodList(monchinhList);
            }
        });

        return view;
    }
}
