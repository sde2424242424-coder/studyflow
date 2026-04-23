package com.example.studyflow.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyflow.storage.SessionManager;
import com.example.studyflow.viewmodel.AuthViewModel;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if (!sessionManager.isLoggedIn()) {
            openAuth();
            return;
        }

        authViewModel.checkAuth();

        authViewModel.authCheckSuccess.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                openMain();
            } else {
                openAuth();
            }
        });
    }

    private void openAuth() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}