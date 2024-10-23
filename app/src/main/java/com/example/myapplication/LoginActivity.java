package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.ViewPagerAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.model.Food;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.Table;
import com.example.myapplication.viewModel.FoodViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    public List<Table> tableList;
    public  ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tableList = new ArrayList<>();
        apiService = ApiClient.getClient().create(ApiService.class);



        Call<List<Table>> call = apiService.getListTable();
        call.enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                if (response.isSuccessful()) {;
                    tableList.addAll(response.body());
                    for (Table table : tableList) {
                        Log.d("TABLE_LIST", "ID: " + table.getTableId() +
                                ", Username: " + table.getUsername() +
                                ", Password: " + table.getPassword());
                    }
                    Log.d("API_RESPONSE", "Response  successful: " + response.code());
                } else {
                    Log.d("API_RESPONSE", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                Log.e("API_RESPONSE", "Failure: " + t.getMessage());
            }
        });
        binding.edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.txtErrorText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.txtErrorText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.btnLogin.setOnClickListener(v ->{
            if(binding.edtUsername.getText().toString().isEmpty() || binding.edtPassword.getText().toString().isEmpty()){
                binding.txtErrorText.setVisibility(View.VISIBLE);
                binding.txtErrorText.setText("Vui lòng nhập đầy đủ thông tin!");
            }else{
                if(!tableList.isEmpty()){
                    for (Table t : tableList){
                        if(Objects.equals(t.getUsername(), binding.edtUsername.getText().toString()) && Objects.equals(t.getPassword(), binding.edtPassword.getText().toString())){
                            SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            Gson gson = new Gson();
                            String tableJson = gson.toJson(t);
                            editor.putString("tableInfo", tableJson);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            binding.txtErrorText.setVisibility(View.VISIBLE);
                            binding.txtErrorText.setText("Đăng nhập thành công!");

                            new Handler().postDelayed(() -> {
                                Intent i = new Intent(this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }, 1000);
                            return;
                        }
                    }
                    binding.txtErrorText.setVisibility(View.VISIBLE);
                    binding.txtErrorText.setText("Sai tên đăng nhập hoặc mật khẩu!");
                }
            }

        });

    }
}