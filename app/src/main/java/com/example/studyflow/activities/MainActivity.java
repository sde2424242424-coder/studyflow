package com.example.studyflow.activities;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.studyflow.R;
import com.example.studyflow.fragments.CreateSubjectFragment;
import com.example.studyflow.fragments.ResultFragment;
import com.example.studyflow.fragments.StatisticsFragment;
import com.example.studyflow.fragments.StudyFragment;
import com.example.studyflow.fragments.SubjectDetailFragment;
import com.example.studyflow.fragments.SubjectsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fabAdd = findViewById(R.id.fabAdd);

        if (savedInstanceState == null) {
            loadFragment(new SubjectsFragment(), false);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_subjects) {
                loadFragment(new SubjectsFragment(), false);
                return true;
            } else if (id == R.id.nav_statistics) {
                loadFragment(new StatisticsFragment(), false);
                return true;
            }

            return false;
        });

        fabAdd.setOnClickListener(v -> {
            loadFragment(new SubjectDetailFragment(), true);
        });
    }

    public void openSubjectDetail() {
        loadFragment(new SubjectDetailFragment(), true);
    }

    public void openStudyFragment() {
        loadFragment(new StudyFragment(), true);
    }

    public void openCreateSubjectFragment() {
        loadFragment(new CreateSubjectFragment(), true);
    }

    public void openResultFragment() {
        loadFragment(new ResultFragment(), true);
    }

    public void returnToSubjects() {
        loadFragment(new SubjectsFragment(), false);
    }

    private void loadFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}