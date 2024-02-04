package com.example.colegioapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colegioapp.ExamDetailsActivity;
import com.example.colegioapp.ExamsTeacherActivity;
import com.example.colegioapp.HomeActivity;
import com.example.colegioapp.R;
import com.example.colegioapp.dto.Exam;
import com.example.colegioapp.dto.UserExam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExamTeacherListAdapter extends RecyclerView.Adapter<ExamTeacherListAdapter.ViewHolder> {

    private ArrayList<Exam> data;

    private Context context;

    public ExamTeacherListAdapter(ArrayList<Exam> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExamNameTeacher;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewExamNameTeacher = itemView.findViewById(R.id.textViewExamNameTeacher);

            textViewExamNameTeacher.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Exam exam = data.get(position);
                        try {
                            OkHttpClient client = new OkHttpClient();

                            String url = "http://10.0.2.2:8080/exam/userExam/" + exam.getId();
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
                                        ArrayList<UserExam> exams = gson.fromJson(responseBody, listType);
                                        System.out.println("Exams: " + exams);
                                        if (exams == null) {
                                            //alerta de usuario o contraseña incorrecto

                                        } else {
                                            Intent intent = new Intent(context, ExamDetailsActivity.class);
                                            intent.putExtra("exams", exams);

                                            // Start the second activity
                                            context.startActivity(intent);
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
                            AlertDialog msj = new AlertDialog.Builder(context).create();
                            msj.setTitle("Error");
                            msj.setMessage("El servicio no esta disponible");
                            msj.show();
                        }
                    }
                }
            });
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_exam_teacher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exam item = data.get(position);
        holder.textViewExamNameTeacher.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}