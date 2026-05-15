package com.example.studyflow.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyflow.models.MicroCheckpoint;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class DialogUtils {

    public interface OnPauseSurveySubmitListener {
        void onSubmit(MicroCheckpoint checkpoint);
    }

    public static void showPauseSurveyDialog(
            Context context,
            OnPauseSurveySubmitListener listener
    ) {
        final String[] selectedDistraction = {null};
        final String[] selectedMood = {null};
        final String[] selectedBreakReason = {null};

        AlertDialog dialog = new AlertDialog.Builder(context).create();

        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(false);

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(context, 24), dp(context, 24), dp(context, 24), dp(context, 16));
        container.setBackgroundColor(Color.WHITE);

        TextView title = createTitle(context, "Pause Check");
        container.addView(title);

        TextView subtitle = createSubtitle(
                context,
                "Короткий опрос поможет понять, что происходит во время учебной сессии."
        );
        container.addView(subtitle);

        addSpace(context, container, 18);

        TextView distractionTitle = createSectionTitle(
                context,
                "Сколько раз отвлекался с начала сессии?"
        );
        container.addView(distractionTitle);

        ChipGroup distractionGroup = new ChipGroup(context);
        distractionGroup.setSingleSelection(true);
        distractionGroup.setChipSpacingHorizontal(dp(context, 8));
        distractionGroup.setChipSpacingVertical(dp(context, 8));

        String[] distractions = {
                "0 раз",
                "1-2 раза",
                "3-5 раз",
                "Больше 5 раз"
        };

        for (String item : distractions) {
            Chip chip = createChoiceChip(context, item);
            chip.setOnClickListener(v -> selectedDistraction[0] = item);
            distractionGroup.addView(chip);
        }

        container.addView(distractionGroup);

        addSpace(context, container, 20);

        TextView moodTitle = createSectionTitle(context, "Настроение");
        container.addView(moodTitle);

        GridLayout moodGrid = new GridLayout(context);
        moodGrid.setColumnCount(2);

        String[] moods = {
                "😌 Спокойный",
                "😊 Счастливый",
                "😟 Нервный",
                "😴 Уставший",
                "😠 Раздражённый",
                "🔥 Мотивированный",
                "😐 Скучно"
        };

        for (String item : moods) {
            Button button = createEmojiButton(context, item);
            button.setOnClickListener(v -> {
                selectedMood[0] = item;
                markSelectedButton(moodGrid, button);
            });
            moodGrid.addView(button);
        }

        container.addView(moodGrid);

        addSpace(context, container, 20);

        TextView reasonTitle = createSectionTitle(context, "Причина перерыва");
        container.addView(reasonTitle);

        GridLayout reasonGrid = new GridLayout(context);
        reasonGrid.setColumnCount(2);

        String[] reasons = {
                "😪 Устал",
                "👀 Отвлёкся",
                "📱 Телефон",
                "🍙 Еда",
                "🚻 Туалет",
                "🧩 Стало сложно",
                "🌀 Потерял концентрацию"
        };

        for (String item : reasons) {
            Button button = createEmojiButton(context, item);
            button.setOnClickListener(v -> {
                selectedBreakReason[0] = item;
                markSelectedButton(reasonGrid, button);
            });
            reasonGrid.addView(button);
        }

        container.addView(reasonGrid);

        addSpace(context, container, 20);

        TextView concentrationTitle = createSectionTitle(
                context,
                "Концентрация сейчас"
        );
        container.addView(concentrationTitle);

        TextView concentrationHint = createSubtitle(
                context,
                "1 — почти нет концентрации, 5 — очень хорошая концентрация"
        );
        container.addView(concentrationHint);

        RatingBar ratingBar = new RatingBar(context);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1f);
        ratingBar.setRating(3f);
        ratingBar.setIsIndicator(false);

        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingParams.gravity = Gravity.CENTER_HORIZONTAL;
        ratingParams.topMargin = dp(context, 8);
        ratingBar.setLayoutParams(ratingParams);

        container.addView(ratingBar);

        addSpace(context, container, 24);

        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.END);

        Button cancelButton = new Button(context);
        cancelButton.setText("Cancel");

        Button saveButton = new Button(context);
        saveButton.setText("Save");

        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cancelParams.rightMargin = dp(context, 8);

        buttonLayout.addView(cancelButton, cancelParams);
        buttonLayout.addView(saveButton);

        container.addView(buttonLayout);

        scrollView.addView(container);

        dialog.setView(scrollView);
        dialog.setCanceledOnTouchOutside(false);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        saveButton.setOnClickListener(v -> {
            if (selectedDistraction[0] == null) {
                Toast.makeText(context, "Выбери количество отвлечений", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedMood[0] == null) {
                Toast.makeText(context, "Выбери настроение", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedBreakReason[0] == null) {
                Toast.makeText(context, "Выбери причину перерыва", Toast.LENGTH_SHORT).show();
                return;
            }

            int concentrationLevel = Math.round(ratingBar.getRating());

            if (concentrationLevel < 1) {
                Toast.makeText(context, "Выбери уровень концентрации", Toast.LENGTH_SHORT).show();
                return;
            }

            MicroCheckpoint checkpoint = new MicroCheckpoint(
                    selectedDistraction[0],
                    selectedMood[0],
                    selectedBreakReason[0],
                    concentrationLevel,
                    System.currentTimeMillis()
            );

            if (listener != null) {
                listener.onSubmit(checkpoint);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private static TextView createTitle(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(24);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        return textView;
    }

    private static TextView createSubtitle(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(14);
        textView.setTextColor(Color.DKGRAY);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setPadding(0, dp(context, 6), 0, 0);
        return textView;
    }

    private static TextView createSectionTitle(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(17);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        textView.setPadding(0, 0, 0, dp(context, 8));
        return textView;
    }

    private static Chip createChoiceChip(Context context, String text) {
        Chip chip = new Chip(context);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setTextSize(14);
        return chip;
    }

    private static Button createEmojiButton(Context context, String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setTextSize(14);
        button.setAllCaps(false);
        button.setBackgroundColor(Color.rgb(245, 242, 255));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(dp(context, 4), dp(context, 4), dp(context, 4), dp(context, 4));
        button.setLayoutParams(params);

        return button;
    }

    private static void markSelectedButton(GridLayout gridLayout, Button selectedButton) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);

            if (child instanceof Button) {
                child.setBackgroundColor(Color.rgb(245, 242, 255));
            }
        }

        selectedButton.setBackgroundColor(Color.rgb(220, 208, 255));
    }

    private static void addSpace(Context context, LinearLayout container, int heightDp) {
        View space = new View(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(context, heightDp)
        ));
        container.addView(space);
    }

    private static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density);
    }
}