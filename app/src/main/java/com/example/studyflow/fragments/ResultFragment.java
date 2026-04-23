package com.example.studyflow.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;

public class ResultFragment extends Fragment {

    private TextView textTimeValue;
    private TextView textProductivityValue;
    private TextView textFatigueValue;
    private SeekBar seekBarProductivity;
    private SeekBar seekBarFatigue;
    private EditText editNotes;
    private Button buttonSaveResult;

    public ResultFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textTimeValue = view.findViewById(R.id.textTimeValue);
        textProductivityValue = view.findViewById(R.id.textProductivityValue);
        textFatigueValue = view.findViewById(R.id.textFatigueValue);
        seekBarProductivity = view.findViewById(R.id.seekBarProductivity);
        seekBarFatigue = view.findViewById(R.id.seekBarFatigue);
        editNotes = view.findViewById(R.id.editNotes);
        buttonSaveResult = view.findViewById(R.id.buttonSaveResult);

        textTimeValue.setText("00:45:21");

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

    private void updateProductivityText(int progress) {
        int value = progress + 1;
        textProductivityValue.setText(value + " / 5");
    }

    private void updateFatigueText(int progress) {
        int value = progress + 1;
        textFatigueValue.setText(value + " / 5");
    }

    private void saveResult() {
        int productivity = seekBarProductivity.getProgress() + 1;
        int fatigue = seekBarFatigue.getProgress() + 1;
        String notes = editNotes.getText().toString().trim();

        String message = "Saved\nProductivity: " + productivity +
                "\nFatigue: " + fatigue +
                "\nNotes: " + (TextUtils.isEmpty(notes) ? "-" : notes);

        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();

        ((MainActivity) requireActivity()).returnToSubjects();
    }
}