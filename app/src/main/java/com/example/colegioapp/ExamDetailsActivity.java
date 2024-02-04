package com.example.colegioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colegioapp.adapter.ExamDetailsListAdapter;
import com.example.colegioapp.dto.UserExam;

import java.util.ArrayList;

public class ExamDetailsActivity extends AppCompatActivity {

    private ArrayList<UserExam> userExams;

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_details);

        // Bloque codigo para boton back generico
        btnBack = (Button) findViewById(R.id.btnBackDetails);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("exams")) {
            userExams = (ArrayList<UserExam>) intent.getSerializableExtra("exams");

            ExamDetailsListAdapter adapter = new ExamDetailsListAdapter(userExams, ExamDetailsActivity.this);
            RecyclerView recyclerView = findViewById(R.id.recyclerViewDetailsExams);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

        }


    }
}