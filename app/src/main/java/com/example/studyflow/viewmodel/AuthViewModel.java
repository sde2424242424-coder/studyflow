package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.network.requests.LoginRequestDto;
import com.example.studyflow.network.requests.RegisterRequestDto;
import com.example.studyflow.network.responses.LoginResponseDto;
import com.example.studyflow.network.responses.UserResponseDto;
import com.example.studyflow.repository.AuthRepository;
import com.example.studyflow.storage.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final SessionManager sessionManager;

    public MutableLiveData<LoginResponseDto> authSuccess = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> authCheckSuccess = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application.getApplicationContext());
        sessionManager = new SessionManager(application.getApplicationContext());
    }

    public void login(String email, String password) {
        LoginRequestDto requestDto = new LoginRequestDto(email, password);

        authRepository.login(requestDto).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseDto> call,
                                   @NonNull Response<LoginResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseDto body = response.body();

                    if (body.getUser() != null) {
                        sessionManager.saveAuthData(
                                body.getToken(),
                                body.getUser().getEmail(),
                                body.getUser().getName()
                        );
                    }

                    authSuccess.postValue(body);
                } else {
                    errorMessage.postValue("Login failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseDto> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void register(String name, String email, String password) {
        RegisterRequestDto requestDto = new RegisterRequestDto(name, email, password);

        authRepository.register(requestDto).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseDto> call,
                                   @NonNull Response<LoginResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseDto body = response.body();

                    if (body.getUser() != null) {
                        sessionManager.saveAuthData(
                                body.getToken(),
                                body.getUser().getEmail(),
                                body.getUser().getName()
                        );
                    }

                    authSuccess.postValue(body);
                } else {
                    errorMessage.postValue("Registration failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseDto> call, @NonNull Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void checkAuth() {
        authRepository.getCurrentUser().enqueue(new Callback<UserResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call,
                                   @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful()) {
                    authCheckSuccess.postValue(true);
                } else {
                    sessionManager.clearSession();
                    authCheckSuccess.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                sessionManager.clearSession();
                authCheckSuccess.postValue(false);
            }
        });
    }
}