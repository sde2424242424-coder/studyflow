package com.example.studyflow.network;

import com.example.studyflow.network.requests.LoginRequestDto;
import com.example.studyflow.network.requests.RegisterRequestDto;
import com.example.studyflow.network.responses.LoginResponseDto;
import com.example.studyflow.network.responses.UserResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/register")
    Call<LoginResponseDto> register(@Body RegisterRequestDto request);

    @POST("api/auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto request);

    @GET("api/auth/me")
    Call<UserResponseDto> getCurrentUser();
}