package com.example.studyflow.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;
import com.example.studyflow.viewmodel.SubjectViewModel;

public class CreateSubjectFragment extends Fragment {

    private EditText editSubjectName;
    private EditText editGoal;
    private Switch switchReminder;
    private Button buttonCreateSubject;

    private SubjectViewModel viewModel;

    public CreateSubjectFragment() {
        super(R.layout.fragment_create_subject);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editSubjectName = view.findViewById(R.id.editSubjectName);
        editGoal = view.findViewById(R.id.editGoal);
        switchReminder = view.findViewById(R.id.switchReminder);
        buttonCreateSubject = view.findViewById(R.id.buttonCreateSubject);

        viewModel = new ViewModelProvider(requireActivity()).get(SubjectViewModel.class);

        observeViewModel();

        buttonCreateSubject.setOnClickListener(v -> createSubject());
    }

    private void observeViewModel() {
        viewModel.getSubjectCreated().observe(getViewLifecycleOwner(), created -> {
            if (created != null && created) {
                Toast.makeText(requireContext(), "Subject created", Toast.LENGTH_SHORT).show();
                viewModel.resetSubjectCreated();
                ((MainActivity) requireActivity()).returnToSubjects();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createSubject() {
        String title = editSubjectName.getText().toString().trim();
        String description = editGoal.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editSubjectName.setError("Enter subject name");
            editSubjectName.requestFocus();
            return;
        }

        buttonCreateSubject.setEnabled(false);
        Toast.makeText(requireContext(), "Creating subject...", Toast.LENGTH_SHORT).show();

        viewModel.createSubject(title, description);
    }
}