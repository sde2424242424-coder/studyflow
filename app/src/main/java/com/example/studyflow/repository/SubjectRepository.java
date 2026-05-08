package com.example.studyflow.repository;

import android.content.Context;

import com.example.studyflow.network.ApiClient;
import com.example.studyflow.network.ApiService;
import com.example.studyflow.network.requests.CreateSubjectRequestDto;
import com.example.studyflow.network.responses.SubjectResponseDto;

import java.util.List;

import retrofit2.Call;

public class SubjectRepository {

    private final ApiService apiService;

    public SubjectRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public Call<List<SubjectResponseDto>> getSubjects() {
        return apiService.getSubjects();
    }

    public Call<SubjectResponseDto> createSubject(String title, String description) {
        return apiService.createSubject(new CreateSubjectRequestDto(title, description));
    }
}