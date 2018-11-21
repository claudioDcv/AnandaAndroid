package com.claudio.rojas.anandaapp.request;

import com.claudio.rojas.anandaapp.dto.CredentialRequestDto;
import com.claudio.rojas.anandaapp.dto.LoginResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;

public class LoginService {

    Consumer consumer;

    public LoginService() {
        this.consumer = new Consumer();
    }

    public Call<LoginResponseDto> onLogin(CredentialRequestDto loginRequestDto) {
        System.out.println(loginRequestDto.getPassword() + ":" + loginRequestDto.getUsername());
        return consumer.getRetrofitCOnfigured().onLogin(loginRequestDto);
    }
}
