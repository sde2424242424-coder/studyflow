package com.example.studyflow.network.requests;

public class MicroCheckpointRequestDto {

    private String distractionCountRange;
    private String mood;
    private String breakReason;
    private Integer concentrationLevel;
    private Long createdAtMillis;

    public MicroCheckpointRequestDto() {
    }

    public MicroCheckpointRequestDto(
            String distractionCountRange,
            String mood,
            String breakReason,
            Integer concentrationLevel,
            Long createdAtMillis
    ) {
        this.distractionCountRange = distractionCountRange;
        this.mood = mood;
        this.breakReason = breakReason;
        this.concentrationLevel = concentrationLevel;
        this.createdAtMillis = createdAtMillis;
    }

    public String getDistractionCountRange() {
        return distractionCountRange;
    }

    public void setDistractionCountRange(String distractionCountRange) {
        this.distractionCountRange = distractionCountRange;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getBreakReason() {
        return breakReason;
    }

    public void setBreakReason(String breakReason) {
        this.breakReason = breakReason;
    }

    public Integer getConcentrationLevel() {
        return concentrationLevel;
    }

    public void setConcentrationLevel(Integer concentrationLevel) {
        this.concentrationLevel = concentrationLevel;
    }

    public Long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public void setCreatedAtMillis(Long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }
}