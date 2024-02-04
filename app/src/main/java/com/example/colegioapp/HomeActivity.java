package com.example.colegioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.colegioapp.dto.Exam;
import com.example.colegioapp.dto.User;
import com.example.colegioapp.dto.UserExam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    private User userResponse;
    private Button examBtn;
    private Button logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        examBtn = (Button) findViewById(R.id.btnExam);
        logOutBtn = (Button) findViewById(R.id.btnLogout);

        Intent intent = getIntent();
        if (intent.hasExtra("userResponse")) {
            userResponse = (User) intent.getSerializableExtra("userResponse");
            System.out.println(userResponse.getRole().getName());
        }

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        examBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTeacher = "PROFESOR".equals(userResponse.getRole().getName());
                if (isTeacher) {
                    // url = "http://10.0.2.2:8080/exam"
                    // Devuelve ArrayList<Exam>
                    try {
                        OkHttpClient client = new OkHttpClient();

                        String url = "http://10.0.2.2:8080/exam";
                        Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .build();

                        // Perform the request and handle the response
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                // Handle failure (e.g., network issues)
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // Handle the response
                                if (response.isSuccessful()) {
                                    Gson gson = new Gson();
                                    // Process the successful response
                                    String responseBody = response.body().string();
                                    // Update UI or perform further processing
                                    System.out.println("Response: " + responseBody);

                                    Type listType = new TypeToken<ArrayList<Exam>>() {
                                    }.getType();
                                    ArrayList<Exam> exams = gson.fromJson(responseBody, listType);
                                    System.out.println("Exams: " + exams);
                                    if (exams == null) {
                                        //alerta de usuario o contraseña incorrecto

                                    } else {
                                        Intent intent = new Intent(HomeActivity.this, ExamsTeacherActivity.class);

                                        // Put the ResponseObject as an extra in the Intent
                                        intent.putExtra("userResponse", userResponse);
                                        intent.putExtra("exams", exams);

                                        // Start the second activity
                                        startActivity(intent);
                                    }
                                } else {
                                    // Handle non-successful response
                                }
                                // Close the response
                                response.close();
                            }
                        });

                    } catch (Exception e) {
                        //Toast.makeText(mContext, exception.getMessage(),
                        //        Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        AlertDialog msj = new AlertDialog.Builder(HomeActivity.this).create();
                        msj.setTitle("Error");
                        msj.setMessage("El servicio no esta disponible");
                        msj.show();
                    }

                } else {
                    Long userId = userResponse.getId();
                    try {
                        OkHttpClient client = new OkHttpClient();

                        String url = "http://10.0.2.2:8080/exam/alumno/" + userId;
                        Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .build();

                        // Perform the request and handle the response
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                // Handle failure (e.g., network issues)
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // Handle the response
                                if (response.isSuccessful()) {
                                    Gson gson = new Gson();
                                    // Process the successful response
                                    String responseBody = response.body().string();
                                    // Update UI or perform further processing
                                    System.out.println("Response: " + responseBody);

                                    Type listType = new TypeToken<ArrayList<UserExam>>() {
                                    }.getType();
                                    ArrayList<UserExam> userExams = gson.fromJson(responseBody, listType);
                                    System.out.println("UserExams: " + userExams);
                                    if (userExams == null) {
                                        //alerta de usuario o contraseña incorrecto

                                    } else {
                                        Intent intent = new Intent(HomeActivity.this, ExamsStudentActivity.class);

                                        // Put the ResponseObject as an extra in the Intent
                                        intent.putExtra("userResponse", userResponse);
                                        intent.putExtra("userExams", userExams);

                                        // Start the second activity
                                        startActivity(intent);
                                    }
                                } else {
                                    // Handle non-successful response
                                }
                                // Close the response
                                response.close();
                            }
                        });

                    } catch (Exception e) {
                        //Toast.makeText(mContext, exception.getMessage(),
                        //        Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        AlertDialog msj = new AlertDialog.Builder(HomeActivity.this).create();
                        msj.setTitle("Error");
                        msj.setMessage("El servicio no esta disponible");
                        msj.show();
                    }
                }
            }
        });

    }
}