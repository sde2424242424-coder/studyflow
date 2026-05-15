package com.example.studyflow.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studyflow.network.responses.SubjectResponseDto;
import com.example.studyflow.repository.SubjectRepository;
import com.example.studyflow.storage.local.SubjectEntity;
import com.example.studyflow.utils.AuthErrorHandler;

import java.util.ArrayList;
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
    private boolean isCreatingSubject = false;
    private boolean isSyncingSubjects = false;

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
        subjectCreated.postValue(false);
    }

    public void loadSubjectsIfNeeded() {
        if (isLoaded || isLoadingNow) {
            return;
        }

        refreshSubjects();
    }

    public void refreshSubjects() {
        if (isLoadingNow) {
            return;
        }

        isLoadingNow = true;
        loading.postValue(true);

        loadLocalSubjectsThenServer();
    }

    private void loadLocalSubjectsThenServer() {
        subjectRepository.getLocalSubjects(localSubjects -> {
            List<SubjectResponseDto> localDtos = convertLocalSubjectsToDto(localSubjects);

            subjects.postValue(localDtos);
            isLoaded = true;

            loadSubjectsFromServer();
        });
    }

    private void loadSubjectsFromServer() {
        subjectRepository.getSubjects().enqueue(new Callback<List<SubjectResponseDto>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<SubjectResponseDto>> call,
                    @NonNull Response<List<SubjectResponseDto>> response
            ) {
                isLoadingNow = false;
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<SubjectResponseDto> serverSubjects = response.body();

                    subjectRepository.saveServerSubjectsToLocal(serverSubjects, () -> {
                        subjectRepository.getLocalSubjects(localSubjects -> {
                            List<SubjectResponseDto> localDtos = convertLocalSubjectsToDto(localSubjects);
                            subjects.postValue(localDtos);
                            isLoaded = true;
                        });
                    });
                    return;
                }

                if (response.code() == 401 || response.code() == 403) {
                    AuthErrorHandler.handleUnauthorized(getApplication());
                    return;
                }

                errorMessage.setValue("Server unavailable. Local subjects are shown.");
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<SubjectResponseDto>> call,
                    @NonNull Throwable t
            ) {
                isLoadingNow = false;
                loading.setValue(false);

                errorMessage.setValue("Offline mode. Local subjects are shown.");
            }
        });
    }

    public void createSubject(String title, String description) {
        if (isCreatingSubject) {
            return;
        }

        isCreatingSubject = true;

        subjectCreated.postValue(false);
        loading.postValue(true);

        subjectRepository.createSubjectOfflineFirst(
                title,
                description,
                new SubjectRepository.CreateSubjectCallback() {
                    @Override
                    public void onLocalSaved(SubjectEntity subject) {
                        subjectCreated.postValue(true);

                        subjectRepository.getLocalSubjects(localSubjects -> {
                            List<SubjectResponseDto> localDtos = convertLocalSubjectsToDto(localSubjects);
                            subjects.postValue(localDtos);
                            isLoaded = true;
                        });
                    }

                    @Override
                    public void onSynced(SubjectEntity subject) {
                        isCreatingSubject = false;
                        loading.postValue(false);

                        subjectRepository.getLocalSubjects(localSubjects -> {
                            List<SubjectResponseDto> localDtos = convertLocalSubjectsToDto(localSubjects);
                            subjects.postValue(localDtos);
                            isLoaded = true;
                        });
                    }

                    @Override
                    public void onSyncFailed(SubjectEntity subject) {
                        isCreatingSubject = false;
                        loading.postValue(false);
                        errorMessage.postValue("Subject saved locally. It will sync later.");
                    }
                }
        );
    }

    private List<SubjectResponseDto> convertLocalSubjectsToDto(List<SubjectEntity> localSubjects) {
        List<SubjectResponseDto> result = new ArrayList<>();

        if (localSubjects == null) {
            return result;
        }

        for (SubjectEntity entity : localSubjects) {
            SubjectResponseDto dto = new SubjectResponseDto();

            if (entity.serverId != null) {
                dto.setId(entity.serverId);
            } else {
                dto.setId(-entity.localId);
            }

            dto.setTitle(entity.title);
            dto.setDescription(entity.description);

            result.add(dto);
        }

        return result;
    }

    public void syncSubjects() {
        if (isSyncingSubjects) {
            return;
        }

        isSyncingSubjects = true;
        loading.postValue(true);

        subjectRepository.syncPendingSubjects(() -> {
            isSyncingSubjects = false;
            loading.postValue(false);
            refreshSubjects();
        });
    }
}