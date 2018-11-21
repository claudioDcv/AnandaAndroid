package com.claudio.rojas.anandaapp.request;

import com.claudio.rojas.anandaapp.dto.AlarmaRequestDto;
import com.claudio.rojas.anandaapp.dto.AlarmaResponseDto;
import com.claudio.rojas.anandaapp.dto.CredentialRequestDto;
import com.claudio.rojas.anandaapp.dto.LoginResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConsumerDefinition {

    @POST("api-token-auth/")
    Call<LoginResponseDto> onLogin(@Body CredentialRequestDto loginRequestDto);

    //alarma/?usuario=1&fecha=2018-11-21&hora=
    @GET("alarma/")
    Call<List<AlarmaResponseDto>> getAlarmas(
            @Query("usuario") Long usuario,
            @Query("fecha") String fecha,
            @Query("hora") String hora
    );

    @POST("alarma/")
    Call<AlarmaResponseDto> apostALarma(@Body AlarmaRequestDto alarmaRequestDto);

    @PUT("alarma/{id}/")
    Call<AlarmaResponseDto> putALarma(@Path("id") Long id, @Body AlarmaRequestDto alarmaRequestDto);

    @DELETE("alarma/{id}/")
    Call<Void> deleteALarma(@Path("id") Long id);
}
