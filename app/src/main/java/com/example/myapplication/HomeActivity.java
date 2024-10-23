package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.databinding.ActivityHomeBinding;
import com.example.myapplication.model.Table;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    ActivityHomeBinding binding;
    public static Table table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(35000);
        rotate.setRepeatCount(Animation.INFINITE);
        binding.imvBanner.startAnimation(rotate);
        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        binding.lnRightHome.startAnimation(slideInLeft);



        checkLogin();
//        table = new Table("3",4,"table3","table3");
        binding.btnCreateNewOrder.setOnClickListener(v ->{
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            i.putExtra("tableData", table); // 'table' là đối tượng Table
            startActivity(i);
        });

    }



    private void checkLogin() {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Welcome!!!", Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String tableJson = sharedPref.getString("tableInfo", null);

            if (tableJson != null) {
                // Chuyển đổi lại chuỗi JSON thành đối tượng Table
                table = gson.fromJson(tableJson, Table.class);
            }
//            Intent i = getIntent();
//            table = (Table) i.getSerializableExtra("table");
        }

    }
}