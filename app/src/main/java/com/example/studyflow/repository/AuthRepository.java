package com.example.studyflow.repository;

import android.content.Context;

import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.requests.LoginRequestDto;
import com.example.studyflow.network.requests.RegisterRequestDto;
import com.example.studyflow.network.responses.LoginResponseDto;
import com.example.studyflow.network.responses.UserResponseDto;

import retrofit2.Call;

public class AuthRepository {

    private final ApiService apiService;

    public AuthRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    public Call<LoginResponseDto> login(LoginRequestDto requestDto) {
        return apiService.login(requestDto);
    }

    public Call<LoginResponseDto> register(RegisterRequestDto requestDto) {
        return apiService.register(requestDto);
    }

    public Call<UserResponseDto> getCurrentUser() {
        return apiService.getCurrentUser();
    }
}