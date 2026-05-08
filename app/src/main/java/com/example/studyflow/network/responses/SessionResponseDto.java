package com.example.studyflow.network.responses;

public class SessionResponseDto {

    private Long id;
    private Long subjectId;
    private Long durationSeconds;
    private Integer productivity;
    private Integer fatigue;
    private String notes;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public Integer getProductivity() {
        return productivity;
    }

    public Integer getFatigue() {
        return fatigue;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}