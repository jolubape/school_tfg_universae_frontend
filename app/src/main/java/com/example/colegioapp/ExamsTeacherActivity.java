package com.example.colegioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colegioapp.adapter.ExamPupilListAdapter;
import com.example.colegioapp.adapter.ExamTeacherListAdapter;
import com.example.colegioapp.dto.User;
import com.example.colegioapp.dto.UserExam;
import com.example.colegioapp.dto.Exam;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ExamsTeacherActivity extends AppCompatActivity {
    private User userResponse;
    private ArrayList<Exam> exams;

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_teacher);
        // Bloque codigo para boton back generico
        btnBack = (Button) findViewById(R.id.btnBackDetails);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("userResponse") && intent.hasExtra("exams")) {
            userResponse = (User) intent.getSerializableExtra("userResponse");
            exams = (ArrayList<Exam>) intent.getSerializableExtra("exams");

            ExamTeacherListAdapter adapter = new ExamTeacherListAdapter(exams, ExamsTeacherActivity.this);
            RecyclerView recyclerView = findViewById(R.id.recyclerViewTeacherExams);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

        }


    }
}