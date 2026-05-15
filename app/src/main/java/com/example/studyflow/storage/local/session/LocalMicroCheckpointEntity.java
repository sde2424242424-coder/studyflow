package com.example.studyflow.storage.local.session;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "micro_checkpoints",
        foreignKeys = @ForeignKey(
                entity = LocalStudySessionEntity.class,
                parentColumns = "localId",
                childColumns = "sessionLocalId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("sessionLocalId")
        }
)
public class LocalMicroCheckpointEntity {

    @PrimaryKey(autoGenerate = true)
    public long localId;

    public long sessionLocalId;

    public String distractionCountRange;
    public String mood;
    public String breakReason;
    public Integer concentrationLevel;

    public long createdAtMillis;

    public LocalMicroCheckpointEntity() {
        this.createdAtMillis = System.currentTimeMillis();
    }

    public LocalMicroCheckpointEntity(
            long sessionLocalId,
            String distractionCountRange,
            String mood,
            String breakReason,
            Integer concentrationLevel
    ) {
        this.sessionLocalId = sessionLocalId;
        this.distractionCountRange = distractionCountRange;
        this.mood = mood;
        this.breakReason = breakReason;
        this.concentrationLevel = concentrationLevel;
        this.createdAtMillis = System.currentTimeMillis();
    }
}