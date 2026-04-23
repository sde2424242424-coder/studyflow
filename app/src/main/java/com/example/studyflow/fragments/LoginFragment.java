package com.example.studyflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;

public class LoginFragment extends Fragment {

    private Button btnLogin;
    private TextView textRegister;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogin = view.findViewById(R.id.buttonLogin);
        textRegister = view.findViewById(R.id.textRegister);

        textRegister.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(LoginFragment.this);
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}