package com.example.studyflow.network.responses;

public class MicroCheckpointResponseDto {

    private Long id;
    private String distractionCountRange;
    private String mood;
    private String breakReason;
    private Integer concentrationLevel;
    private Long createdAtMillis;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public String getDistractionCountRange() {
        return distractionCountRange;
    }

    public String getMood() {
        return mood;
    }

    public String getBreakReason() {
        return breakReason;
    }

    public Integer getConcentrationLevel() {
        return concentrationLevel;
    }

    public Long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}