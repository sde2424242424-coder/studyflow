package com.example.studyflow.storage.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class SubjectEntity {

    @PrimaryKey(autoGenerate = true)
    public long localId;

    public Long serverId;

    public String title;
    public String description;

    public boolean synced;

    public long createdAtMillis;
    public long updatedAtMillis;

    public SubjectEntity(String title, String description) {
        this.title = title;
        this.description = description;
        this.synced = false;
        this.createdAtMillis = System.currentTimeMillis();
        this.updatedAtMillis = System.currentTimeMillis();
    }
}