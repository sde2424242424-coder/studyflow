package com.example.studyflow.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;

import java.util.Locale;

public class StudyFragment extends Fragment {

    private TextView textTimer;
    private Button buttonPause;
    private Button buttonFinish;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private int seconds = 0;
    private boolean isRunning = true;

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                seconds++;
                updateTimerText();
            }
            handler.postDelayed(this, 1000);
        }
    };

    public StudyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textTimer = view.findViewById(R.id.textTimer);
        buttonPause = view.findViewById(R.id.buttonPause);
        buttonFinish = view.findViewById(R.id.buttonFinish);

        updateTimerText();
        handler.postDelayed(timerRunnable, 1000);

        buttonPause.setOnClickListener(v -> {
            isRunning = !isRunning;

            if (isRunning) {
                buttonPause.setText("Pause");
            } else {
                buttonPause.setText("Resume");
            }
        });

        buttonFinish.setOnClickListener(v -> {
            handler.removeCallbacks(timerRunnable);
            ((MainActivity) requireActivity()).openResultFragment();
        });
    }

    private void updateTimerText() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
        textTimer.setText(time);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(timerRunnable);
    }
}