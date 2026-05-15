package com.example.studyflow.network.responses;

import java.util.List;

public class SessionResponseDto {

    private Long id;
    private Long subjectId;

    private String learningType;
    private String startedAt;
    private String endedAt;

    private Long durationSeconds;
    private Long plannedSeconds;

    private String dayOfWeek;

    private Boolean hadBreak;
    private Integer breakCount;

    private Integer productivity;
    private Integer fatigue;
    private Integer studyStreakDays;

    private String notes;
    private String createdAt;
    private Long createdAtMillis;

    private String studyPlace;
    private String studyEnvironment;
    private String difficulty;
    private Boolean needReview;
    private String fatigueLevel;
    private Integer understanding;

    private List<MicroCheckpointResponseDto> microCheckpoints;

    public SessionResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getLearningType() {
        return learningType;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public Long getPlannedSeconds() {
        return plannedSeconds;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public Boolean getHadBreak() {
        return hadBreak;
    }

    public Integer getBreakCount() {
        return breakCount;
    }

    public Integer getProductivity() {
        return productivity;
    }

    public Integer getFatigue() {
        return fatigue;
    }

    public Integer getStudyStreakDays() {
        return studyStreakDays;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public String getStudyPlace() {
        return studyPlace;
    }

    public String getStudyEnvironment() {
        return studyEnvironment;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Boolean getNeedReview() {
        return needReview;
    }

    public String getFatigueLevel() {
        return fatigueLevel;
    }

    public Integer getUnderstanding() {
        return understanding;
    }

    public List<MicroCheckpointResponseDto> getMicroCheckpoints() {
        return microCheckpoints;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public void setLearningType(String learningType) {
        this.learningType = learningType;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setPlannedSeconds(Long plannedSeconds) {
        this.plannedSeconds = plannedSeconds;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setHadBreak(Boolean hadBreak) {
        this.hadBreak = hadBreak;
    }

    public void setBreakCount(Integer breakCount) {
        this.breakCount = breakCount;
    }

    public void setProductivity(Integer productivity) {
        this.productivity = productivity;
    }

    public void setFatigue(Integer fatigue) {
        this.fatigue = fatigue;
    }

    public void setStudyStreakDays(Integer studyStreakDays) {
        this.studyStreakDays = studyStreakDays;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAtMillis(Long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }

    public void setStudyPlace(String studyPlace) {
        this.studyPlace = studyPlace;
    }

    public void setStudyEnvironment(String studyEnvironment) {
        this.studyEnvironment = studyEnvironment;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setNeedReview(Boolean needReview) {
        this.needReview = needReview;
    }

    public void setFatigueLevel(String fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public void setUnderstanding(Integer understanding) {
        this.understanding = understanding;
    }

    public void setMicroCheckpoints(List<MicroCheckpointResponseDto> microCheckpoints) {
        this.microCheckpoints = microCheckpoints;
    }
}