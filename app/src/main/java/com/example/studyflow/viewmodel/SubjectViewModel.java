package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.network.responses.SubjectResponseDto;
import com.example.studyflow.repository.SubjectRepository;
import com.example.studyflow.utils.AuthErrorHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectViewModel extends AndroidViewModel {

    private final SubjectRepository subjectRepository;

    private final MutableLiveData<List<SubjectResponseDto>> subjects = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> subjectCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private boolean isLoaded = false;
    private boolean isLoadingNow = false;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        subjectRepository = new SubjectRepository(application);
    }

    public LiveData<List<SubjectResponseDto>> getSubjectsLiveData() {
        return subjects;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getSubjectCreated() {
        return subjectCreated;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void resetSubjectCreated() {
        subjectCreated.setValue(false);
    }

    public void loadSubjectsIfNeeded() {
        if (isLoaded || isLoadingNow) {
            return;
        }

        loadSubjectsFromServer();
    }

    public void refreshSubjects() {
        if (isLoadingNow) {
            return;
        }

        isLoaded = false;
        loadSubjectsFromServer();
    }

    private void loadSubjectsFromServer() {
        isLoadingNow = true;
        loading.setValue(true);

        subjectRepository.getSubjects().enqueue(new Callback<List<SubjectResponseDto>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<SubjectResponseDto>> call,
                    @NonNull Response<List<SubjectResponseDto>> response
            ) {
                isLoadingNow = false;
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    subjects.setValue(response.body());
                    isLoaded = true;
                    return;
                }

                isLoaded = false;

                if (response.code() == 401 || response.code() == 403) {
                    AuthErrorHandler.handleUnauthorized(getApplication());
                    return;
                }

                errorMessage.setValue("Failed to load subjects: " + response.code());
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<SubjectResponseDto>> call,
                    @NonNull Throwable t
            ) {
                isLoadingNow = false;
                loading.setValue(false);
                isLoaded = false;

                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void createSubject(String title, String description) {
        subjectCreated.setValue(false);
        loading.setValue(true);

        subjectRepository.createSubject(title, description).enqueue(new Callback<SubjectResponseDto>() {
            @Override
            public void onResponse(
                    @NonNull Call<SubjectResponseDto> call,
                    @NonNull Response<SubjectResponseDto> response
            ) {
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    subjectCreated.setValue(true);
                    refreshSubjects();
                    return;
                }

                if (response.code() == 401 || response.code() == 403) {
                    AuthErrorHandler.handleUnauthorized(getApplication());
                    return;
                }

                errorMessage.setValue("Failed to create subject: " + response.code());
            }

            @Override
            public void onFailure(
                    @NonNull Call<SubjectResponseDto> call,
                    @NonNull Throwable t
            ) {
                loading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }
}