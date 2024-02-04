package com.example.colegioapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colegioapp.R;
import com.example.colegioapp.dto.UserExam;

import java.util.ArrayList;

public class ExamPupilListAdapter extends RecyclerView.Adapter<ExamPupilListAdapter.ViewHolder> {

    private ArrayList<UserExam> data;

    public ExamPupilListAdapter(ArrayList<UserExam> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExamName;
        TextView textViewExamGrade;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewExamName = itemView.findViewById(R.id.textViewExamName);
            textViewExamGrade = itemView.findViewById(R.id.textViewExamGrade);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_exam_pupil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserExam item = data.get(position);
        holder.textViewExamName.setText(item.getExam().getName());

        String grade = "Sin evaluar";
        if(item.getGrade() != null) {
            grade = item.getGrade().toString();
        }
        holder.textViewExamGrade.setText(grade);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}