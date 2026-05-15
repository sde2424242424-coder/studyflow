package com.example.studyflow.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.studyflow.R;
import com.example.studyflow.fragments.CreateSubjectFragment;
import com.example.studyflow.fragments.ResultFragment;
import com.example.studyflow.fragments.SettingsFragment;
import com.example.studyflow.fragments.StatisticsFragment;
import com.example.studyflow.fragments.StudyFragment;
import com.example.studyflow.fragments.SubjectDetailFragment;
import com.example.studyflow.fragments.SubjectsFragment;
import com.example.studyflow.models.MicroCheckpoint;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setupTopAppBar();
        setupBottomNavigation();

        if (savedInstanceState == null) {
            loadFragment(new SubjectsFragment(), false);
        }
    }

    private void setupTopAppBar() {
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settingsFragment) {
                loadFragment(new SettingsFragment(), true);
                return true;
            }

            return false;
        });
    }

    private void setupBottomNavigation() {
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
    }

    public void openSubjectDetail(Long subjectId, String subjectName) {
        if (subjectId == null || subjectId <= 0) {
            return;
        }

        SubjectDetailFragment fragment = new SubjectDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("subjectId", subjectId);
        bundle.putString("subjectName", subjectName);

        fragment.setArguments(bundle);

        loadFragment(fragment, true);
    }

    public void openStudyFragment(Long subjectId, String subjectName, long plannedSeconds) {
        if (subjectId == null || subjectId <= 0) {
            return;
        }

        StudyFragment fragment = new StudyFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("subjectId", subjectId);
        bundle.putString("subjectName", subjectName);
        bundle.putLong("plannedSeconds", plannedSeconds);

        fragment.setArguments(bundle);

        loadFragment(fragment, true);
    }

    public void openCreateSubjectFragment() {
        loadFragment(new CreateSubjectFragment(), true);
    }

    public void openResultFragment(Long subjectId,
                                   String subjectName,
                                   long studiedSeconds,
                                   long plannedSeconds,
                                   ArrayList<MicroCheckpoint> microCheckpoints,
                                   long sessionLocalId) {
        if (subjectId == null || subjectId <= 0) {
            return;
        }

        ResultFragment fragment = new ResultFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("subjectId", subjectId);
        bundle.putString("subjectName", subjectName);
        bundle.putLong("studiedSeconds", studiedSeconds);
        bundle.putLong("plannedSeconds", plannedSeconds);
        bundle.putSerializable("microCheckpoints", microCheckpoints);
        bundle.putLong("sessionLocalId", sessionLocalId);

        fragment.setArguments(bundle);

        loadFragment(fragment, true);
    }

    public void returnToSubjects() {
        loadFragment(new SubjectsFragment(), false);
        bottomNavigationView.setSelectedItemId(R.id.nav_subjects);
    }

    public void returnToSubjectDetail(Long subjectId, String subjectName) {
        if (subjectId == null || subjectId <= 0) {
            returnToSubjects();
            return;
        }

        SubjectDetailFragment fragment = new SubjectDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("subjectId", subjectId);
        bundle.putString("subjectName", subjectName);

        fragment.setArguments(bundle);

        loadFragment(fragment, false);
    }

    private void loadFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}