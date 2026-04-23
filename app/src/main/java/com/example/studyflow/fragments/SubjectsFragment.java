package com.example.studyflow.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyflow.R;

public class SubjectsFragment extends Fragment {

    private RecyclerView recyclerSubjects;
    private LinearLayout layoutEmptyState;

    public SubjectsFragment() {
        super(R.layout.fragment_subjects);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerSubjects = view.findViewById(R.id.recyclerSubjects);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);

        recyclerSubjects.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Пока список пустой
        showEmptyState(true);
    }

    private void showEmptyState(boolean isEmpty) {
        if (isEmpty) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerSubjects.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerSubjects.setVisibility(View.VISIBLE);
        }
    }
}