package com.example.studyflow.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettingsManager {

    private static final String PREF_NAME = "studyflow_app_settings";

    private static final String KEY_LANGUAGE = "language";

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_RUSSIAN = "ru";
    public static final String LANGUAGE_KOREAN = "ko";

    private final SharedPreferences sharedPreferences;

    public AppSettingsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void saveLanguage(String languageCode) {
        sharedPreferences.edit()
                .putString(KEY_LANGUAGE, languageCode)
                .apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(
                KEY_LANGUAGE,
                LANGUAGE_ENGLISH
        );
    }
}