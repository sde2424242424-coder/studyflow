package com.example.studyflow.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.studyflow.activities.AuthActivity;
import com.example.studyflow.storage.SessionManager;

public class AuthErrorHandler {

    private static boolean isHandlingAuthError = false;

    private AuthErrorHandler() {
    }

    public static void handleUnauthorized(Context context) {
        if (context == null) {
            return;
        }

        if (isHandlingAuthError) {
            return;
        }

        isHandlingAuthError = true;

        SessionManager sessionManager = new SessionManager(context);
        sessionManager.clearSession();

        Toast.makeText(
                context,
                "Session expired. Please log in again.",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void reset() {
        isHandlingAuthError = false;
    }
}