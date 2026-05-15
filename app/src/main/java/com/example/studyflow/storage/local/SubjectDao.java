package com.example.studyflow.storage.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubjectDao {

    @Insert
    long insert(SubjectEntity subject);

    @Update
    void update(SubjectEntity subject);

    @Query("SELECT * FROM subjects ORDER BY updatedAtMillis DESC")
    List<SubjectEntity> getAllSubjects();

    @Query("SELECT * FROM subjects WHERE synced = 0")
    List<SubjectEntity> getPendingSubjects();

    @Query("DELETE FROM subjects")
    void deleteAll();

    @Delete
    void delete(SubjectEntity subject);

    @Query("SELECT * FROM subjects WHERE serverId = :serverId LIMIT 1")
    SubjectEntity findByServerId(Long serverId);

    @Query("SELECT * FROM subjects WHERE localId = :localId LIMIT 1")
    SubjectEntity findByLocalId(long localId);

    @Query("DELETE FROM subjects WHERE synced = 1 AND serverId IS NOT NULL")
    void deleteAllSyncedServerSubjects();

    @Query("DELETE FROM subjects WHERE synced = 1 AND serverId IS NOT NULL AND serverId NOT IN (:serverIds)")
    void deleteSyncedSubjectsNotInServerIds(List<Long> serverIds);

    @Query("DELETE FROM subjects WHERE serverId = :serverId")
    void deleteByServerId(Long serverId);
}