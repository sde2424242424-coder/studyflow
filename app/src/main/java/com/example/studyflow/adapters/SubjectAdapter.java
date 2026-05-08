package com.example.studyflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyflow.R;
import com.example.studyflow.listeners.OnSubjectClickListener;
import com.example.studyflow.network.responses.SubjectResponseDto;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private final List<SubjectResponseDto> subjects = new ArrayList<>();
    private final OnSubjectClickListener listener;

    public SubjectAdapter(OnSubjectClickListener listener) {
        this.listener = listener;
    }

    public void setSubjects(List<SubjectResponseDto> subjects) {
        this.subjects.clear();

        if (subjects != null) {
            this.subjects.addAll(subjects);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.ViewHolder holder, int position) {
        SubjectResponseDto subject = subjects.get(position);

        holder.textName.setText(subject.getTitle() != null ? subject.getTitle() : "Untitled subject");
        holder.textGoal.setText(subject.getDescription() != null ? subject.getDescription() : "");
        holder.textProgress.setText("Progress: 0%");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSubjectClick(subject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textGoal;
        TextView textProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textSubjectName);
            textGoal = itemView.findViewById(R.id.textSubjectGoal);
            textProgress = itemView.findViewById(R.id.textSubjectProgress);
        }
    }
}