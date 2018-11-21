package com.claudio.rojas.anandaapp.request;

import com.claudio.rojas.anandaapp.dto.AlarmaRequestDto;
import com.claudio.rojas.anandaapp.dto.AlarmaResponseDto;
import com.claudio.rojas.anandaapp.dto.AlarmaSearchRequestDto;

import java.util.List;

import retrofit2.Call;

public class AlarmaService {

    Consumer consumer;

    public AlarmaService() {
        this.consumer = new Consumer();
    }

    public Call<List<AlarmaResponseDto>> getAlarmas(AlarmaSearchRequestDto model) {
        System.out.println("Aqui");
        return consumer.getRetrofitCOnfigured().getAlarmas(
                model.getUsuario(),
                model.getFecha(),
                model.getHora()
        );
    }

    public Call<AlarmaResponseDto> postAlarma(final AlarmaRequestDto model) {
        System.out.println(model);
        return consumer.getRetrofitCOnfigured().apostALarma(model);
    }

    public Call<AlarmaResponseDto> putALarma(Long id, AlarmaRequestDto model) {
        return consumer.getRetrofitCOnfigured().putALarma(id, model);
    }

    public Call<Void> deleteAlarma(Long id) {
        return consumer.getRetrofitCOnfigured().deleteALarma(id);
    }
}
