package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragment.KhaiViFragment;
import com.example.myapplication.fragment.MonChinhFragment;
import com.example.myapplication.fragment.TrangMiengFragment;
import com.example.myapplication.viewModel.FoodViewModel;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final FoodViewModel foodViewModel;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, FoodViewModel foodViewModel) {
        super(fragmentActivity);
        this.foodViewModel = foodViewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return  MonChinhFragment.newInstance(foodViewModel);
            case 2:
                return TrangMiengFragment.newInstance(foodViewModel);
            default:
                return KhaiViFragment.newInstance(foodViewModel);

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
