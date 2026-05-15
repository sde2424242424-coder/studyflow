package com.example.studyflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studyflow.R;
import com.example.studyflow.activities.AuthActivity;
import com.example.studyflow.storage.AppSettingsManager;
import com.example.studyflow.storage.SessionManager;

public class SettingsFragment extends Fragment {

    private AppSettingsManager appSettingsManager;

    private RadioGroup languageRadioGroup;
    private RadioButton englishRadioButton;
    private RadioButton russianRadioButton;
    private RadioButton koreanRadioButton;
    private Button logoutButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        appSettingsManager = new AppSettingsManager(requireContext());

        languageRadioGroup = view.findViewById(R.id.languageRadioGroup);
        englishRadioButton = view.findViewById(R.id.englishRadioButton);
        russianRadioButton = view.findViewById(R.id.russianRadioButton);
        koreanRadioButton = view.findViewById(R.id.koreanRadioButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        setupCurrentLanguage();
        setupLanguageListener();
        setupLogoutButton();
    }

    private void setupCurrentLanguage() {
        String currentLanguage = appSettingsManager.getLanguage();

        switch (currentLanguage) {
            case AppSettingsManager.LANGUAGE_RUSSIAN:
                russianRadioButton.setChecked(true);
                break;

            case AppSettingsManager.LANGUAGE_KOREAN:
                koreanRadioButton.setChecked(true);
                break;

            case AppSettingsManager.LANGUAGE_ENGLISH:
            default:
                englishRadioButton.setChecked(true);
                break;
        }
    }

    private void setupLanguageListener() {
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedLanguage;

            if (checkedId == R.id.russianRadioButton) {
                selectedLanguage = AppSettingsManager.LANGUAGE_RUSSIAN;
            } else if (checkedId == R.id.koreanRadioButton) {
                selectedLanguage = AppSettingsManager.LANGUAGE_KOREAN;
            } else {
                selectedLanguage = AppSettingsManager.LANGUAGE_ENGLISH;
            }

            appSettingsManager.saveLanguage(selectedLanguage);

            requireActivity().recreate();
        });
    }

    private void setupLogoutButton() {
        logoutButton.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(requireContext());
            sessionManager.clearSession();

            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            requireActivity().finish();
        });
    }
}