package com.claudio.rojas.anandaapp.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Consumer {
    final static String API_HOST = "https://abueloapp.herokuapp.com/";

    private Retrofit retrofit;
    public Consumer() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Consumer.API_HOST)
                .build();
    }

    private Retrofit getRetrofit() {
        return retrofit;
    }

    public ConsumerDefinition getRetrofitCOnfigured() {
        System.out.println("rotrofit configured");
        ConsumerDefinition consumerDefinition = this.getRetrofit().create(ConsumerDefinition.class);
        return consumerDefinition;
    }
}
