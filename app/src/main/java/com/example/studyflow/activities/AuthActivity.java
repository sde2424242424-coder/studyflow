package com.example.studyflow.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.studyflow.R;
import com.example.studyflow.fragments.LoginFragment;
import com.example.studyflow.fragments.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            loadFragment(new LoginFragment(), false);
        }
    }

    public void openRegister() {
        loadFragment(new RegisterFragment(), true);
    }

    public void openLogin() {
        loadFragment(new LoginFragment(), true);
    }

    private void loadFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerAuth, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}