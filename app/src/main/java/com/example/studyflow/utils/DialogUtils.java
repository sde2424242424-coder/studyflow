package com.example.studyflow.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public interface PauseDialogListener {
        void onResume(int focusLevel, int fatigueLevel);
        void onKeepPaused(int focusLevel, int fatigueLevel);
    }

    public static void showPauseDialog(
            Context context,
            int currentFocus,
            int currentFatigue,
            PauseDialogListener listener
    ) {
        String[] focusOptions = {
                "1 - Очень низкая",
                "2 - Низкая",
                "3 - Средняя",
                "4 - Хорошая",
                "5 - Отличная"
        };

        final int[] focusLevel = {currentFocus};
        final int[] fatigueLevel = {currentFatigue};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Пауза");

        builder.setSingleChoiceItems(focusOptions, currentFocus - 1, (dialog, which) -> {
            focusLevel[0] = which + 1;
        });

        builder.setPositiveButton("Продолжить", (dialog, which) -> {
            listener.onResume(focusLevel[0], fatigueLevel[0]);
        });

        builder.setNegativeButton("Оставить паузу", (dialog, which) -> {
            listener.onKeepPaused(focusLevel[0], fatigueLevel[0]);
        });

        builder.setCancelable(false);
        builder.show();
    }
}
