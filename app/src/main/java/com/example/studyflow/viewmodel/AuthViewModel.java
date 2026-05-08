package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.models.LoginResult;
import com.example.studyflow.network.responses.LoginResponseDto;
import com.example.studyflow.repository.AuthRepository;
import com.example.studyflow.storage.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final SessionManager sessionManager;

    public MutableLiveData<LoginResponseDto> authSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> authCheckSuccess = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        sessionManager = new SessionManager(application);
    }

    public MutableLiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        authRepository.login(email, password).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseDto body = response.body();

                    if (body.getToken() != null && !body.getToken().isEmpty()) {
                        sessionManager.saveAuthToken(response.body().getToken());

                        authSuccess.postValue(body);
                        loginResult.postValue(new LoginResult(true, "Успешный вход"));
                    } else {
                        loginResult.postValue(new LoginResult(false, "Сервер не вернул токен"));
                    }

                } else if (response.code() == 401 || response.code() == 403) {
                    loginResult.postValue(new LoginResult(false, "Неверный email или пароль"));
                } else {
                    loginResult.postValue(new LoginResult(false, "Ошибка сервера: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                loginResult.postValue(new LoginResult(false, "Ошибка сети: " + t.getMessage()));
            }
        });
    }

    public void register(String name, String email, String password) {
        authRepository.register(name, email, password).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseDto body = response.body();

                    if (body.getToken() != null && !body.getToken().isEmpty()) {
                        sessionManager.saveAuthToken(body.getToken());
                    }

                    authSuccess.postValue(body);

                } else {
                    errorMessage.postValue("Ошибка регистрации. Код: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                errorMessage.postValue("Ошибка сети: " + t.getMessage());
            }
        });
    }

    public void checkAuth() {
        authCheckSuccess.postValue(sessionManager.isLoggedIn());
    }
}