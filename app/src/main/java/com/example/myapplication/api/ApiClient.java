package com.example.myapplication.api;


import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://resmant1111-001-site1.jtempurl.com/";
    private static Retrofit retrofit = null;

    private static final String USERNAME = "11197666";
    private static final String PASSWORD = "60-dayfreetrial";
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .authenticator((route, response) -> {
                        String credential = Credentials.basic(USERNAME, PASSWORD);
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    })
                    .build();

            // Xây dựng Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)  // Thêm OkHttpClient vào Retrofit
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
