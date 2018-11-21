package com.claudio.rojas.anandaapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.auth0.android.jwt.JWT;
import com.claudio.rojas.anandaapp.dto.AlarmaResponseDto;
import com.claudio.rojas.anandaapp.dto.AlarmaSearchRequestDto;
import com.claudio.rojas.anandaapp.request.AlarmaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listado extends AppCompatActivity {

    final static ArrayList<Item> superListaPrincipal = new ArrayList<Item>();
    static ItemArrayAdapter itemArrayAdapter;
    RecyclerView recyclerView;
    public static final String PREFS_NAME = "MI_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);


        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String tokenData = settings.getString("silentMode", "0");
        JWT decode = new JWT(tokenData);

        // Initializing list view with the custom adapter
        itemArrayAdapter = new ItemArrayAdapter(R.layout.list_item, superListaPrincipal);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);

        Long userId = Long.parseLong(decode.getClaim("user_id").asString());
        AlarmaSearchRequestDto alarmaSearchRequestDto = new AlarmaSearchRequestDto();
        alarmaSearchRequestDto.setUsuario(userId);
        alarmaSearchRequestDto.setFecha("");
        alarmaSearchRequestDto.setHora("");

        AlarmaService alarmaService = new AlarmaService();
        alarmaService.getAlarmas(alarmaSearchRequestDto).enqueue(new Callback<List<AlarmaResponseDto>>() {

            @Override
            public void onResponse(Call<List<AlarmaResponseDto>> call, Response<List<AlarmaResponseDto>> response) {
                superListaPrincipal.clear();
                for (AlarmaResponseDto a: response.body()
                     ) {
                    superListaPrincipal.add(new Item(a.getId() + ") " + a.getTipo() + " - " + a.getFecha() + " - " + a.getHora() + " - " + a.getDescripcion(), a.getId()));
                }
                itemArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<AlarmaResponseDto>> call, Throwable t) {

            }
        });
    }

}
