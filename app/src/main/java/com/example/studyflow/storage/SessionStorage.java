package com.example.studyflow.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.studyflow.models.StudySession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionStorage {

    private static final String PREF_NAME = "study_sessions";
    private static final String KEY_PREFIX = "subject_sessions_";

    public static void saveSessions(Context context, long subjectId, List<StudySession> sessions) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String json = new Gson().toJson(sessions);

        prefs.edit()
                .putString(KEY_PREFIX + subjectId, json)
                .apply();
    }

    public static List<StudySession> loadSessions(Context context, long subjectId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String json = prefs.getString(KEY_PREFIX + subjectId, null);

        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<ArrayList<StudySession>>() {}.getType();

        return new Gson().fromJson(json, type);
    }
}