package com.example.studyflow.network.requests;

public class FinishSessionRequestDto {

    private Long subjectId;
    private Long durationSeconds;
    private Integer productivity;
    private Integer fatigue;
    private String notes;

    public FinishSessionRequestDto(Long subjectId,
                                   Long durationSeconds,
                                   Integer productivity,
                                   Integer fatigue,
                                   String notes) {
        this.subjectId = subjectId;
        this.durationSeconds = durationSeconds;
        this.productivity = productivity;
        this.fatigue = fatigue;
        this.notes = notes;
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
}