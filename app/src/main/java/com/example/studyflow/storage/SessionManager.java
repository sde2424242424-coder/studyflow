package com.example.studyflow.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.studyflow.utils.Constants;

public class SessionManager {

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveAuthData(String token, String email, String name) {
        preferences.edit()
                .putString(Constants.KEY_TOKEN, token)
                .putString(Constants.KEY_USER_EMAIL, email)
                .putString(Constants.KEY_USER_NAME, name)
                .apply();
    }

    public String getToken() {
        return preferences.getString(Constants.KEY_TOKEN, null);
    }

    public String getUserEmail() {
        return preferences.getString(Constants.KEY_USER_EMAIL, null);
    }

    public String getUserName() {
        return preferences.getString(Constants.KEY_USER_NAME, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }

    public void clearSession() {
        preferences.edit().clear().apply();
    }
}