package com.example.studyflow.repository;

import android.content.Context;

import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.requests.CreateSubjectRequestDto;
import com.example.studyflow.network.responses.SubjectResponseDto;
import com.example.studyflow.storage.local.StudyflowDatabase;
import com.example.studyflow.storage.local.SubjectDao;
import com.example.studyflow.storage.local.SubjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectRepository {

    private final ApiService apiService;
    private final SubjectDao subjectDao;
    private final ExecutorService executorService;

    public SubjectRepository(Context context) {
        apiService = ApiClient.getApiService(context);
        subjectDao = StudyflowDatabase.getInstance(context).subjectDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Старый метод пока оставляем для совместимости
    public Call<List<SubjectResponseDto>> getSubjects() {
        return apiService.getSubjects();
    }

    // Новый метод: получить предметы с телефона
    public void getLocalSubjects(LocalSubjectsCallback callback) {
        executorService.execute(() -> {
            List<SubjectEntity> subjects = subjectDao.getAllSubjects();
            callback.onResult(subjects);
        });
    }

    // Новый метод: создать предмет offline-first
    public void createSubjectOfflineFirst(
            String title,
            String description,
            CreateSubjectCallback callback
    ) {
        executorService.execute(() -> {
            SubjectEntity localSubject = new SubjectEntity(title, description);

            long localId = subjectDao.insert(localSubject);
            localSubject.localId = localId;

            callback.onLocalSaved(localSubject);

            tryCreateSubjectOnServer(localSubject, callback);
        });
    }

    private void tryCreateSubjectOnServer(
            SubjectEntity localSubject,
            CreateSubjectCallback callback
    ) {
        CreateSubjectRequestDto requestDto = new CreateSubjectRequestDto(
                localSubject.title,
                localSubject.description
        );

        apiService.createSubject(requestDto).enqueue(new Callback<SubjectResponseDto>() {
            @Override
            public void onResponse(
                    Call<SubjectResponseDto> call,
                    Response<SubjectResponseDto> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    SubjectResponseDto serverSubject = response.body();

                    executorService.execute(() -> {
                        localSubject.serverId = serverSubject.getId();
                        localSubject.synced = true;
                        localSubject.updatedAtMillis = System.currentTimeMillis();

                        subjectDao.update(localSubject);

                        callback.onSynced(localSubject);
                    });
                } else {
                    callback.onSyncFailed(localSubject);
                }
            }

            @Override
            public void onFailure(Call<SubjectResponseDto> call, Throwable t) {
                callback.onSyncFailed(localSubject);
            }
        });
    }

    // Старый метод пока оставляем
    public Call<SubjectResponseDto> createSubject(String title, String description) {
        return apiService.createSubject(new CreateSubjectRequestDto(title, description));
    }

    public interface LocalSubjectsCallback {
        void onResult(List<SubjectEntity> subjects);
    }

    public interface CreateSubjectCallback {
        void onLocalSaved(SubjectEntity subject);

        void onSynced(SubjectEntity subject);

        void onSyncFailed(SubjectEntity subject);
    }

    public void saveServerSubjectsToLocal(List<SubjectResponseDto> serverSubjects, Runnable callback) {
        executorService.execute(() -> {
            if (serverSubjects == null) {
                if (callback != null) callback.run();
                return;
            }

            if (serverSubjects.isEmpty()) {
                subjectDao.deleteAllSyncedServerSubjects();

                if (callback != null) {
                    callback.run();
                }

                return;
            }

            List<Long> serverIds = new ArrayList<>();

            for (SubjectResponseDto dto : serverSubjects) {
                serverIds.add(dto.getId());

                SubjectEntity existing = subjectDao.findByServerId(dto.getId());

                if (existing == null) {
                    SubjectEntity entity = new SubjectEntity(
                            dto.getTitle(),
                            dto.getDescription()
                    );

                    entity.serverId = dto.getId();
                    entity.synced = true;
                    entity.updatedAtMillis = System.currentTimeMillis();

                    subjectDao.insert(entity);
                } else {
                    existing.title = dto.getTitle();
                    existing.description = dto.getDescription();
                    existing.synced = true;
                    existing.updatedAtMillis = System.currentTimeMillis();

                    subjectDao.update(existing);
                }
            }

            subjectDao.deleteSyncedSubjectsNotInServerIds(serverIds);

            if (callback != null) {
                callback.run();
            }
        });
    }

    public void syncPendingSubjects(SyncCallback callback) {
        executorService.execute(() -> {
            List<SubjectEntity> pendingSubjects = subjectDao.getPendingSubjects();

            if (pendingSubjects == null || pendingSubjects.isEmpty()) {
                if (callback != null) {
                    callback.onFinished();
                }
                return;
            }

            syncPendingSubjectAtIndex(pendingSubjects, 0, callback);
        });
    }

    private void syncPendingSubjectAtIndex(
            List<SubjectEntity> pendingSubjects,
            int index,
            SyncCallback callback
    ) {
        if (index >= pendingSubjects.size()) {
            if (callback != null) {
                callback.onFinished();
            }
            return;
        }

        SubjectEntity subject = pendingSubjects.get(index);

        CreateSubjectRequestDto requestDto = new CreateSubjectRequestDto(
                subject.title,
                subject.description
        );

        apiService.createSubject(requestDto).enqueue(new Callback<SubjectResponseDto>() {
            @Override
            public void onResponse(
                    Call<SubjectResponseDto> call,
                    Response<SubjectResponseDto> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    SubjectResponseDto serverSubject = response.body();

                    executorService.execute(() -> {
                        subject.serverId = serverSubject.getId();
                        subject.synced = true;
                        subject.updatedAtMillis = System.currentTimeMillis();

                        subjectDao.update(subject);

                        syncPendingSubjectAtIndex(pendingSubjects, index + 1, callback);
                    });
                } else {
                    syncPendingSubjectAtIndex(pendingSubjects, index + 1, callback);
                }
            }

            @Override
            public void onFailure(Call<SubjectResponseDto> call, Throwable t) {
                syncPendingSubjectAtIndex(pendingSubjects, index + 1, callback);
            }
        });
    }

    public void deleteSubject(Long subjectId, DeleteSubjectCallback callback) {
        apiService.deleteSubject(subjectId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executorService.execute(() -> {
                        subjectDao.deleteByServerId(subjectId);
                        callback.onSuccess();
                    });
                } else {
                    callback.onError("Не удалось удалить урок");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteSubjectOfflineFirst(
            SubjectEntity subject,
            DeleteSubjectCallback callback
    ) {
        executorService.execute(() -> {
            subjectDao.delete(subject);

            if (subject.serverId == null) {
                callback.onSuccess();
                return;
            }

            apiService.deleteSubject(subject.serverId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError("Deleted locally, but failed to delete on server");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    callback.onError("Deleted locally, but server sync failed: " + t.getMessage());
                }
            });
        });
    }

    public interface DeleteSubjectCallback {
        void onSuccess();

        void onError(String message);
    }

    public interface SyncCallback {
        void onFinished();
    }

    public void getLocalSubjectById(long localId, LocalSubjectCallback callback) {
        executorService.execute(() -> {
            SubjectEntity subject = subjectDao.findByLocalId(localId);
            callback.onResult(subject);
        });
    }

    public interface LocalSubjectCallback {
        void onResult(SubjectEntity subject);
    }
}