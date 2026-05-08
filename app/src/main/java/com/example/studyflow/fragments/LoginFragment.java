package com.example.studyflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;
import com.example.studyflow.utils.AuthErrorHandler;
import com.example.studyflow.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {

    private EditText editEmail;
    private EditText editPassword;
    private Button buttonLogin;

    private AuthViewModel authViewModel;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        buttonLogin.setOnClickListener(v -> login());

        authViewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {

            if (result == null) {
                return;
            }

            Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_LONG).show();

            if (result.isSuccess()) {
                AuthErrorHandler.reset();

                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                buttonLogin.setEnabled(true);
                buttonLogin.setText("Login");
            }
        });

        TextView textRegister = view.findViewById(R.id.textRegister);

        textRegister.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void login() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Введите email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Введите пароль");
            return;
        }

        buttonLogin.setEnabled(false);
        buttonLogin.setText("Вход...");

        authViewModel.login(email, password);
    }
}