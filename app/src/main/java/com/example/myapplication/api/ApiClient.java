package com.example.myapplication.api;


import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://resmant11111-001-site1.anytempurl.com/";
    private static Retrofit retrofit = null;

    private static final String USERNAME = "11206824";
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

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
