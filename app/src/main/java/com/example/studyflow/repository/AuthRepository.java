package com.example.studyflow.repository;

import android.content.Context;

import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.requests.LoginRequestDto;
import com.example.studyflow.network.requests.RegisterRequestDto;
import com.example.studyflow.network.responses.LoginResponseDto;

import retrofit2.Call;

public class AuthRepository {

    private final ApiService apiService;

    public AuthRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public Call<LoginResponseDto> login(String email, String password) {
        return apiService.login(new LoginRequestDto(email, password));
    }

    public Call<LoginResponseDto> register(String name, String email, String password) {
        return apiService.register(new RegisterRequestDto(name, email, password));
    }
}