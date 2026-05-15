package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.repository.SessionRepository;
import com.example.studyflow.storage.local.session.LocalStudySessionEntity;

public class StudyViewModel extends AndroidViewModel {

    private final SessionRepository sessionRepository;

    private final MutableLiveData<Long> currentSessionLocalId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sessionStarted = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> checkpointSaved = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> sessionFinished = new MutableLiveData<>(false);

    public StudyViewModel(@NonNull Application application) {
        super(application);
        sessionRepository = new SessionRepository(application);
    }

    public LiveData<Long> getCurrentSessionLocalId() {
        return currentSessionLocalId;
    }

    public LiveData<Boolean> getSessionStarted() {
        return sessionStarted;
    }

    public LiveData<Boolean> getCheckpointSaved() {
        return checkpointSaved;
    }

    public LiveData<Boolean> getSessionFinished() {
        return sessionFinished;
    }

    public void startSession(Long subjectId, String learningType) {
        sessionStarted.postValue(false);

        sessionRepository.startLocalSession(
                subjectId,
                learningType,
                new SessionRepository.StartSessionCallback() {
                    @Override
                    public void onStarted(LocalStudySessionEntity session) {
                        currentSessionLocalId.postValue(session.localId);
                        sessionStarted.postValue(true);
                    }
                }
        );
    }

    public void saveMicroCheckpoint(
            long sessionLocalId,
            String distractionCountRange,
            String mood,
            String breakReason,
            Integer concentrationLevel
    ) {
        checkpointSaved.postValue(false);

        sessionRepository.saveMicroCheckpoint(
                sessionLocalId,
                distractionCountRange,
                mood,
                breakReason,
                concentrationLevel,
                () -> checkpointSaved.postValue(true)
        );
    }

    public void finishSession(
            long sessionLocalId,
            long endedAtMillis,
            long durationSeconds,
            long plannedSeconds,
            String studyPlace,
            String studyEnvironment,
            String helpfulFactors,
            String disturbingFactors,
            Integer productivityLevel,
            Integer fatigue,
            String notes,
            String difficultyLevel,
            Boolean needReview,
            String fatigueLevel,
            Integer understandingLevel
    ) {
        sessionFinished.postValue(false);

        sessionRepository.finishLocalSession(
                sessionLocalId,
                endedAtMillis,
                durationSeconds,
                plannedSeconds,
                studyPlace,
                studyEnvironment,
                helpfulFactors,
                disturbingFactors,
                productivityLevel,
                fatigue,
                notes,
                difficultyLevel,
                needReview,
                fatigueLevel,
                understandingLevel,
                () -> sessionFinished.postValue(true)
        );
    }
}
