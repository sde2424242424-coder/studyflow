package com.example.studyflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;
import com.example.studyflow.viewmodel.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editPhone;

    private Button buttonCreateAccount;
    private TextView textLoginLink;

    private AuthViewModel authViewModel;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editPhone = view.findViewById(R.id.editPhone);

        buttonCreateAccount = view.findViewById(R.id.buttonCreateAccount);
        textLoginLink = view.findViewById(R.id.textLoginLink);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        buttonCreateAccount.setOnClickListener(v -> {
            String name = getText(editName);
            String email = getText(editEmail);
            String password = getText(editPassword);
            String phone = getText(editPhone);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Fill in name, email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // phone пока не отправляем, потому что backend его не принимает
            authViewModel.register(name, email, password);
        });

        textLoginLink.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_loginFragment)
        );

        authViewModel.authSuccess.observe(getViewLifecycleOwner(), response -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        authViewModel.errorMessage.observe(getViewLifecycleOwner(), error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        );
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}