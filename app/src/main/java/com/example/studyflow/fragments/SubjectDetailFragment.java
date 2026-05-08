package com.example.studyflow.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;
import com.example.studyflow.adapters.SessionAdapter;
import com.example.studyflow.network.responses.SessionResponseDto;
import com.example.studyflow.viewmodel.SessionViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetailFragment extends Fragment {

    private TextView textSubjectTitle;
    private Button buttonStartStudy;

    private long subjectId = -1L;
    private String subjectName;

    private RecyclerView recyclerSessionHistory;
    private SessionAdapter sessionAdapter;
    private final List<SessionResponseDto> sessionList = new ArrayList<>();

    private SessionViewModel sessionViewModel;

    public SubjectDetailFragment() {
        super(R.layout.fragment_subject_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textSubjectTitle = view.findViewById(R.id.textSubjectTitle);
        buttonStartStudy = view.findViewById(R.id.buttonStartStudy);
        recyclerSessionHistory = view.findViewById(R.id.recyclerSessionHistory);

        readArguments();

        if (subjectId <= 0) {
            Toast.makeText(requireContext(), "Ошибка: subjectId не передан", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).returnToSubjects();
            return;
        }

        textSubjectTitle.setText(subjectName != null ? subjectName : "Subject");

        setupRecyclerView();

        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);

        observeViewModel();

        sessionViewModel.loadSessionsIfNeeded(subjectId);

        buttonStartStudy.setOnClickListener(v -> showHoursDialog());
    }

    private void readArguments() {
        Bundle args = getArguments();

        if (args != null) {
            subjectId = args.getLong("subjectId", -1L);
            subjectName = args.getString("subjectName");
        }
    }

    private void setupRecyclerView() {
        sessionAdapter = new SessionAdapter(sessionList);
        recyclerSessionHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerSessionHistory.setAdapter(sessionAdapter);
    }

    private void observeViewModel() {
        sessionViewModel.getSessionsLiveData().observe(getViewLifecycleOwner(), sessions -> {
            if (sessions != null) {
                sessionAdapter.setSessions(sessions);
            }
        });

        sessionViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.trim().isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showHoursDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_study_time, null);

        EditText inputHours = dialogView.findViewById(R.id.inputHours);
        EditText inputMinutes = dialogView.findViewById(R.id.inputMinutes);
        EditText inputSeconds = dialogView.findViewById(R.id.inputSeconds);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Сколько будешь учиться сегодня?")
                .setView(dialogView)
                .setPositiveButton("Начать", null)
                .setNegativeButton("Отмена", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            button.setOnClickListener(v -> {
                int hours = parseTimeValue(inputHours);
                int minutes = parseTimeValue(inputMinutes);
                int seconds = parseTimeValue(inputSeconds);

                if (minutes >= 60 || seconds >= 60) {
                    Toast.makeText(
                            requireContext(),
                            "Минуты и секунды должны быть меньше 60",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                long plannedSeconds = hours * 3600L + minutes * 60L + seconds;

                if (plannedSeconds <= 0) {
                    Toast.makeText(
                            requireContext(),
                            "Введите время",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                dialog.dismiss();
                openStudyFragment(plannedSeconds);
            });
        });

        dialog.show();
    }

    private int parseTimeValue(EditText editText) {
        String value = editText.getText().toString().trim();

        if (value.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void openStudyFragment(long plannedSeconds) {
        if (subjectId <= 0) {
            Toast.makeText(requireContext(), "Ошибка: subjectId = " + subjectId, Toast.LENGTH_SHORT).show();
            return;
        }

        ((MainActivity) requireActivity()).openStudyFragment(
                subjectId,
                subjectName,
                plannedSeconds
        );
    }
}