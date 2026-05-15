package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.responses.SessionResponseDto;
import com.example.studyflow.repository.SessionRepository;
import com.example.studyflow.storage.local.session.LocalStudySessionEntity;
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
    private final SessionRepository sessionRepository;

    private final MutableLiveData<List<SessionResponseDto>> sessionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final Map<Long, List<SessionResponseDto>> sessionsCache = new HashMap<>();
    private final HashSet<Long> loadingSubjectIds = new HashSet<>();

    private Long currentSubjectId = null;

    public SessionViewModel(@NonNull Application application) {
        super(application);
        apiService = ApiClient.getApiService(application);
        sessionRepository = new SessionRepository(application);
    }

    public LiveData<List<SessionResponseDto>> getSessionsLiveData() {
        return sessionsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadSessionsIfNeeded(Long subjectId) {
        if (subjectId == null) {
            errorMessage.postValue("Subject id is empty");
            return;
        }

        currentSubjectId = subjectId;

        if (loadingSubjectIds.contains(subjectId)) {
            return;
        }

        loadLocalSessionsThenServer(subjectId);
    }

    public void refreshSessions(Long subjectId) {
        if (subjectId == null) {
            errorMessage.postValue("Subject id is empty");
            return;
        }

        currentSubjectId = subjectId;
        sessionsCache.remove(subjectId);

        loadLocalSessionsThenServer(subjectId);
    }

    private void loadLocalSessionsThenServer(Long subjectId) {
        loadingSubjectIds.add(subjectId);

        sessionRepository.getLocalSessionsBySubjectId(subjectId, localSessions -> {
            List<SessionResponseDto> localDtos = convertLocalSessionsToDto(localSessions);

            sessionsCache.put(subjectId, new ArrayList<>(localDtos));

            if (subjectId.equals(currentSubjectId)) {
                sessionsLiveData.postValue(localDtos);
            }

            loadSessionsFromServer(subjectId);
        });
    }

    private void loadSessionsFromServer(Long subjectId) {
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

                    if (subjectId.equals(currentSubjectId)) {
                        sessionsLiveData.postValue(result);
                    }

                    return;
                }

                if (response.code() == 401 || response.code() == 403) {
                    AuthErrorHandler.handleUnauthorized(getApplication());
                    return;
                }

                errorMessage.postValue("Offline mode. Local session history is shown.");
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<SessionResponseDto>> call,
                    @NonNull Throwable t
            ) {
                loadingSubjectIds.remove(subjectId);
                errorMessage.postValue("Offline mode. Local session history is shown.");
            }
        });
    }

    private List<SessionResponseDto> convertLocalSessionsToDto(
            List<LocalStudySessionEntity> localSessions
    ) {
        List<SessionResponseDto> result = new ArrayList<>();

        if (localSessions == null) {
            return result;
        }

        for (LocalStudySessionEntity entity : localSessions) {
            SessionResponseDto dto = new SessionResponseDto();

            if (entity.serverId != null) {
                dto.setId(entity.serverId);
            } else {
                dto.setId(-entity.localId);
            }

            dto.setSubjectId(entity.subjectServerId);
            dto.setDurationSeconds(entity.durationSeconds);
            dto.setPlannedSeconds(entity.plannedSeconds);
            dto.setProductivity(entity.productivityLevel);
            dto.setFatigue(entity.fatigue);
            dto.setNotes(entity.notes);
            dto.setStudyPlace(entity.studyPlace);
            dto.setStudyEnvironment(entity.studyEnvironment);
            dto.setDifficulty(entity.difficultyLevel);
            dto.setNeedReview(entity.needReview);
            dto.setFatigueLevel(entity.fatigueLevel);
            dto.setUnderstanding(entity.understandingLevel);
            dto.setCreatedAtMillis(entity.createdAtMillis);

            result.add(dto);
        }

        return result;
    }

    public void syncPendingSessions() {
        sessionRepository.syncPendingSessions(() -> {
            if (currentSubjectId != null) {
                refreshSessions(currentSubjectId);
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

        if (subjectId.equals(currentSubjectId)) {
            sessionsLiveData.postValue(currentList);
        }
    }

    public void clearCache() {
        sessionsCache.clear();
        loadingSubjectIds.clear();
        sessionsLiveData.postValue(new ArrayList<>());
    }

    public void clearCacheForSubject(Long subjectId) {
        if (subjectId == null) {
            return;
        }

        sessionsCache.remove(subjectId);
        loadingSubjectIds.remove(subjectId);
    }
}