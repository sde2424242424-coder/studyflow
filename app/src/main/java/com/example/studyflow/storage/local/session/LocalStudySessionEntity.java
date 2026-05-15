package com.example.studyflow.storage.local.session;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_sessions")
public class LocalStudySessionEntity {

    @PrimaryKey(autoGenerate = true)
    public long localId;

    public Long serverId;

    public Long subjectServerId;
    public Long subjectLocalId;

    public String learningType;

    public long startedAtMillis;
    public long endedAtMillis;
    public long durationSeconds;
    public long plannedSeconds;
    public Integer fatigue;
    public String notes;
    public boolean hadBreak;
    public int breakCount;

    public String dayOfWeek;

    // Final survey
    public String studyPlace;
    public String studyEnvironment;
    public String helpfulFactors;
    public String disturbingFactors;
    public Integer productivityLevel;
    public String difficultyLevel;
    public Boolean needReview;
    public String fatigueLevel;
    public Integer understandingLevel;

    public boolean synced;
    public String syncStatus; // PENDING, SYNCED, FAILED

    public long createdAtMillis;
    public long updatedAtMillis;

    public LocalStudySessionEntity() {
        long now = System.currentTimeMillis();

        this.startedAtMillis = now;
        this.createdAtMillis = now;
        this.updatedAtMillis = now;

        this.synced = false;
        this.syncStatus = "PENDING";
        this.hadBreak = false;
        this.breakCount = 0;
    }
}