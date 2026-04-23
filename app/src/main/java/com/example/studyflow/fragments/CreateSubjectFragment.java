package com.example.studyflow.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studyflow.R;

public class CreateSubjectFragment extends Fragment {

    private EditText editSubjectName;
    private EditText editGoal;
    private EditText editFrequency;
    private EditText editDuration;
    private Switch switchReminder;
    private Button buttonCreateSubject;

    public CreateSubjectFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_subject, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editSubjectName = view.findViewById(R.id.editSubjectName);
        editGoal = view.findViewById(R.id.editGoal);
        editFrequency = view.findViewById(R.id.editFrequency);
        editDuration = view.findViewById(R.id.editDuration);
        switchReminder = view.findViewById(R.id.switchReminder);
        buttonCreateSubject = view.findViewById(R.id.buttonCreateSubject);

        buttonCreateSubject.setOnClickListener(v -> createSubject());
    }

    private void createSubject() {
        String subjectName = editSubjectName.getText().toString().trim();
        String goal = editGoal.getText().toString().trim();
        String frequency = editFrequency.getText().toString().trim();
        String duration = editDuration.getText().toString().trim();
        boolean reminderEnabled = switchReminder.isChecked();

        if (TextUtils.isEmpty(subjectName)) {
            editSubjectName.setError("Enter subject name");
            editSubjectName.requestFocus();
            return;
        }

        String message =
                "Created:\n" +
                        "Name: " + subjectName +
                        "\nGoal: " + goal +
                        "\nFrequency: " + frequency +
                        "\nDuration: " + duration +
                        "\nReminder: " + reminderEnabled;

        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();

        requireActivity().getSupportFragmentManager().popBackStack();
    }
}