package com.example.studyflow.network.requests;

import java.util.List;

public class FinishSessionRequestDto {

    private Long subjectId;
    private Long durationSeconds;
    private Long plannedSeconds;

    private Integer productivity;
    private Integer fatigue;
    private String notes;

    private String studyPlace;
    private String studyEnvironment;
    private List<String> helpfulFactors;
    private List<String> disturbingFactors;
    private String difficulty;
    private Boolean needReview;
    private String fatigueLevel;
    private Integer understanding;

    private List<MicroCheckpointRequestDto> microCheckpoints;

    public FinishSessionRequestDto() {
    }

    public FinishSessionRequestDto(
            Long subjectId,
            Long durationSeconds,
            Long plannedSeconds,
            Integer productivity,
            Integer fatigue,
            String notes,
            String studyPlace,
            String studyEnvironment,
            List<String> helpfulFactors,
            List<String> disturbingFactors,
            String difficulty,
            Boolean needReview,
            String fatigueLevel,
            Integer understanding,
            List<MicroCheckpointRequestDto> microCheckpoints
    ) {
        this.subjectId = subjectId;
        this.durationSeconds = durationSeconds;
        this.plannedSeconds = plannedSeconds;
        this.productivity = productivity;
        this.fatigue = fatigue;
        this.notes = notes;
        this.studyPlace = studyPlace;
        this.studyEnvironment = studyEnvironment;
        this.helpfulFactors = helpfulFactors;
        this.disturbingFactors = disturbingFactors;
        this.difficulty = difficulty;
        this.needReview = needReview;
        this.fatigueLevel = fatigueLevel;
        this.understanding = understanding;
        this.microCheckpoints = microCheckpoints;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Long getPlannedSeconds() {
        return plannedSeconds;
    }

    public void setPlannedSeconds(Long plannedSeconds) {
        this.plannedSeconds = plannedSeconds;
    }

    public Integer getProductivity() {
        return productivity;
    }

    public void setProductivity(Integer productivity) {
        this.productivity = productivity;
    }

    public Integer getFatigue() {
        return fatigue;
    }

    public void setFatigue(Integer fatigue) {
        this.fatigue = fatigue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStudyPlace() {
        return studyPlace;
    }

    public void setStudyPlace(String studyPlace) {
        this.studyPlace = studyPlace;
    }

    public String getStudyEnvironment() {
        return studyEnvironment;
    }

    public void setStudyEnvironment(String studyEnvironment) {
        this.studyEnvironment = studyEnvironment;
    }

    public List<String> getHelpfulFactors() {
        return helpfulFactors;
    }

    public void setHelpfulFactors(List<String> helpfulFactors) {
        this.helpfulFactors = helpfulFactors;
    }

    public List<String> getDisturbingFactors() {
        return disturbingFactors;
    }

    public void setDisturbingFactors(List<String> disturbingFactors) {
        this.disturbingFactors = disturbingFactors;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getNeedReview() {
        return needReview;
    }

    public void setNeedReview(Boolean needReview) {
        this.needReview = needReview;
    }

    public String getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(String fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public Integer getUnderstanding() {
        return understanding;
    }

    public void setUnderstanding(Integer understanding) {
        this.understanding = understanding;
    }

    public List<MicroCheckpointRequestDto> getMicroCheckpoints() {
        return microCheckpoints;
    }

    public void setMicroCheckpoints(List<MicroCheckpointRequestDto> microCheckpoints) {
        this.microCheckpoints = microCheckpoints;
    }
}