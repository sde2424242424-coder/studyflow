package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.responses.SessionResponseDto;
import com.example.studyflow.utils.AuthErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionViewModel extends AndroidViewModel {

    private final ApiService apiService;

    private final MutableLiveData<List<SessionResponseDto>> sessionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final Map<Long, List<SessionResponseDto>> sessionsCache = new HashMap<>();
    private final HashSet<Long> loadedSubjectIds = new HashSet<>();
    private final HashSet<Long> loadingSubjectIds = new HashSet<>();

    private Long currentSubjectId = null;

    public SessionViewModel(@NonNull Application application) {
        super(application);
        apiService = ApiClient.getApiService(application);
    }

    public LiveData<List<SessionResponseDto>> getSessionsLiveData() {
        return sessionsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadSessionsIfNeeded(Long subjectId) {
        if (subjectId == null) {
            errorMessage.setValue("Subject id is empty");
            return;
        }

        currentSubjectId = subjectId;

        if (sessionsCache.containsKey(subjectId)) {
            sessionsLiveData.setValue(sessionsCache.get(subjectId));
            return;
        }

        if (loadedSubjectIds.contains(subjectId)) {
            sessionsLiveData.setValue(new ArrayList<>());
            return;
        }

        if (loadingSubjectIds.contains(subjectId)) {
            return;
        }

        loadSessionsFromServer(subjectId);
    }

    public void refreshSessions(Long subjectId) {
        if (subjectId == null) {
            errorMessage.setValue("Subject id is empty");
            return;
        }

        currentSubjectId = subjectId;

        loadedSubjectIds.remove(subjectId);
        sessionsCache.remove(subjectId);

        loadSessionsFromServer(subjectId);
    }

    private void loadSessionsFromServer(Long subjectId) {
        loadingSubjectIds.add(subjectId);

        apiService.getSessionsBySubject(subjectId).enqueue(new Callback<List<SessionResponseDto>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<SessionResponseDto>> call,
                    @NonNull Response<List<SessionResponseDto>> response
            ) {
                loadingSubjectIds.remove(subjectId);

                if (response.isSuccessful() && response.body() != null) {
                    List<SessionResponseDto> result = response.body();

                    sessionsCache.put(subjectId, new ArrayList<>(result));
                    loadedSubjectIds.add(subjectId);

                    if (subjectId.equals(currentSubjectId)) {
                        sessionsLiveData.setValue(result);
                    }

                    return;
                }

                if (response.code() == 401 || response.code() == 403) {
                    AuthErrorHandler.handleUnauthorized(getApplication());
                    return;
                }

                errorMessage.setValue("Failed to load session history");
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<SessionResponseDto>> call,
                    @NonNull Throwable t
            ) {
                loadingSubjectIds.remove(subjectId);
                errorMessage.setValue(t.getMessage());
            }
        });
    }

    public void addSessionToCache(Long subjectId, SessionResponseDto session) {
        if (subjectId == null || session == null) {
            return;
        }

        List<SessionResponseDto> currentList = sessionsCache.get(subjectId);

        if (currentList == null) {
            currentList = new ArrayList<>();
        } else {
            currentList = new ArrayList<>(currentList);
        }

        currentList.add(0, session);

        sessionsCache.put(subjectId, currentList);
        loadedSubjectIds.add(subjectId);

        if (subjectId.equals(currentSubjectId)) {
            sessionsLiveData.setValue(currentList);
        }
    }

    public void clearCache() {
        sessionsCache.clear();
        loadedSubjectIds.clear();
        loadingSubjectIds.clear();
        sessionsLiveData.setValue(new ArrayList<>());
    }

    public void clearCacheForSubject(Long subjectId) {
        if (subjectId == null) {
            return;
        }

        sessionsCache.remove(subjectId);
        loadedSubjectIds.remove(subjectId);
        loadingSubjectIds.remove(subjectId);
    }
}