package com.example.colegioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.colegioapp.adapter.ExamPupilListAdapter;
import com.example.colegioapp.dto.User;
import com.example.colegioapp.dto.UserExam;

import java.util.ArrayList;

public class ExamsStudentActivity extends AppCompatActivity {

    private User userResponse;
    private ArrayList<UserExam> userExams;

    private Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_student);
        // Bloque codigo para boton back generico
        btnBack = (Button) findViewById(R.id.btnBackDetails);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("userResponse") && intent.hasExtra("userExams")) {
            userResponse = (User) intent.getSerializableExtra("userResponse");
            userExams = (ArrayList<UserExam>) intent.getSerializableExtra("userExams");

            ExamPupilListAdapter adapter = new ExamPupilListAdapter(userExams);
            RecyclerView recyclerView = findViewById(R.id.recyclerViewPupilExams);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

        }
    }
}