package com.example.studyflow.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.studyflow.R;
import com.example.studyflow.activities.MainActivity;
import com.example.studyflow.adapters.SubjectAdapter;
import com.example.studyflow.network.responses.SubjectResponseDto;
import com.example.studyflow.viewmodel.SubjectViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SubjectsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshSubjects;
    private RecyclerView recyclerSubjects;
    private LinearLayout layoutEmptyState;
    private FloatingActionButton buttonAddSubject;

    private SubjectAdapter adapter;
    private SubjectViewModel viewModel;

    public SubjectsFragment() {
        super(R.layout.fragment_subjects);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshSubjects = view.findViewById(R.id.swipeRefreshSubjects);
        recyclerSubjects = view.findViewById(R.id.recyclerSubjects);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        buttonAddSubject = view.findViewById(R.id.buttonAddSubject);

        swipeRefreshSubjects.setRefreshing(false);

        viewModel = new ViewModelProvider(requireActivity()).get(SubjectViewModel.class);

        setupRecyclerView();
        setupClickListeners();
        observeViewModel();

        viewModel.refreshSubjects();
    }

    private void setupRecyclerView() {
        adapter = new SubjectAdapter(subject -> {
            if (subject == null) {
                Toast.makeText(requireContext(), "Subject is null", Toast.LENGTH_SHORT).show();
                return;
            }

            if (subject.getId() == null || subject.getId() <= 0) {
                Toast.makeText(requireContext(), "Subject id is invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            ((MainActivity) requireActivity()).openSubjectDetail(
                    subject.getId(),
                    subject.getTitle()
            );
        });

        recyclerSubjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerSubjects.setAdapter(adapter);
    }

    private void setupClickListeners() {
        buttonAddSubject.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openCreateSubjectFragment();
        });

        swipeRefreshSubjects.setOnRefreshListener(() -> {
            viewModel.refreshSubjects();
        });
    }

    private void observeViewModel() {
        viewModel.getSubjectsLiveData().observe(getViewLifecycleOwner(), subjects -> {
            adapter.setSubjects(subjects);
            updateEmptyState(subjects);
            stopRefreshing();
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.trim().isEmpty()) {
                stopRefreshing();
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                swipeRefreshSubjects.setRefreshing(true);
            } else {
                stopRefreshing();
            }
        });
    }

    private void stopRefreshing() {
        if (swipeRefreshSubjects != null) {
            swipeRefreshSubjects.setRefreshing(false);

            swipeRefreshSubjects.post(() -> {
                if (swipeRefreshSubjects != null) {
                    swipeRefreshSubjects.setRefreshing(false);
                }
            });
        }
    }

    private void updateEmptyState(List<SubjectResponseDto> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            recyclerSubjects.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerSubjects.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
        }
    }
}