package com.example.studyflow.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;
import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.requests.FinishSessionRequestDto;
import com.example.studyflow.network.responses.SessionResponseDto;
import com.example.studyflow.utils.AuthErrorHandler;
import com.example.studyflow.viewmodel.SessionViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultFragment extends Fragment {

    private TextView textTimeValue;
    private TextView textProductivityValue;
    private TextView textFatigueValue;
    private SeekBar seekBarProductivity;
    private SeekBar seekBarFatigue;
    private EditText editNotes;
    private Button buttonSaveResult;

    private long subjectId = -1L;
    private String subjectName;
    private long studiedSeconds;
    private long plannedSeconds;

    private SessionViewModel sessionViewModel;

    public ResultFragment() {
        super(R.layout.fragment_result);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        textTimeValue = view.findViewById(R.id.textTimeValue);
        textProductivityValue = view.findViewById(R.id.textProductivityValue);
        textFatigueValue = view.findViewById(R.id.textFatigueValue);
        seekBarProductivity = view.findViewById(R.id.seekBarProductivity);
        seekBarFatigue = view.findViewById(R.id.seekBarFatigue);
        editNotes = view.findViewById(R.id.editNotes);
        buttonSaveResult = view.findViewById(R.id.buttonSaveResult);

        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);

        readArguments();

        if (subjectId <= 0) {
            Toast.makeText(requireContext(), "Ошибка: subjectId не передан", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).returnToSubjects();
            return;
        }

        textTimeValue.setText(formatTime(studiedSeconds));

        updateProductivityText(seekBarProductivity.getProgress());
        updateFatigueText(seekBarFatigue.getProgress());

        seekBarProductivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateProductivityText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarFatigue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateFatigueText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonSaveResult.setOnClickListener(v -> saveResult());
    }

    private void readArguments() {
        Bundle args = getArguments();

        if (args != null) {
            subjectId = args.getLong("subjectId", -1L);
            subjectName = args.getString("subjectName");
            studiedSeconds = args.getLong("studiedSeconds", 0L);
            plannedSeconds = args.getLong("plannedSeconds", 0L);
        }
    }

    private void updateProductivityText(int progress) {
        int value = progress + 1;
        textProductivityValue.setText(value + " / 5");
    }

    private void updateFatigueText(int progress) {
        int value = progress + 1;
        textFatigueValue.setText(value + " / 5");
    }

    private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format(
                java.util.Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
        );
    }

    private void saveResult() {
        int productivity = seekBarProductivity.getProgress() + 1;
        int fatigue = seekBarFatigue.getProgress() + 1;
        String notes = editNotes.getText().toString().trim();

        if (subjectId <= 0) {
            Toast.makeText(requireContext(), "Ошибка: subjectId = " + subjectId, Toast.LENGTH_SHORT).show();
            return;
        }

        if (studiedSeconds <= 0) {
            Toast.makeText(requireContext(), "Ошибка: время занятия равно 0", Toast.LENGTH_SHORT).show();
            return;
        }

        FinishSessionRequestDto request = new FinishSessionRequestDto(
                subjectId,
                studiedSeconds,
                productivity,
                fatigue,
                notes
        );

        buttonSaveResult.setEnabled(false);
        buttonSaveResult.setText("Сохранение...");

        ApiService apiService = ApiClient.getApiService(requireContext());

        apiService.finishSession(request).enqueue(new Callback<SessionResponseDto>() {
            @Override
            public void onResponse(
                    @NonNull Call<SessionResponseDto> call,
                    @NonNull Response<SessionResponseDto> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    SessionResponseDto savedSession = response.body();

                    sessionViewModel.addSessionToCache(subjectId, savedSession);

                    Toast.makeText(requireContext(), "Сессия сохранена", Toast.LENGTH_SHORT).show();

                    ((MainActivity) requireActivity()).returnToSubjectDetail(
                            subjectId,
                            subjectName
                    );
                    return;
                }

                buttonSaveResult.setEnabled(true);
                buttonSaveResult.setText("Save");

                if (response.code() == 401 || response.code() == 403) {
                    AuthErrorHandler.handleUnauthorized(requireContext());
                    return;
                }

                Toast.makeText(
                        requireContext(),
                        "Ошибка сохранения: " + response.code(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onFailure(
                    @NonNull Call<SessionResponseDto> call,
                    @NonNull Throwable t
            ) {
                buttonSaveResult.setEnabled(true);
                buttonSaveResult.setText("Save");

                Toast.makeText(
                        requireContext(),
                        "Ошибка сети: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}