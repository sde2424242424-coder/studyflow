package com.example.studyflow.network.requests;

public class CreateSubjectRequestDto {

    private String title;
    private String description;

    public CreateSubjectRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}