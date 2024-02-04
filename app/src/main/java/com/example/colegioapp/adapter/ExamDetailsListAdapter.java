package com.example.colegioapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colegioapp.HomeActivity;
import com.example.colegioapp.LoginActivity;
import com.example.colegioapp.R;
import com.example.colegioapp.dto.User;
import com.example.colegioapp.dto.UserExam;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExamDetailsListAdapter extends RecyclerView.Adapter<ExamDetailsListAdapter.ViewHolder> {

    private ArrayList<UserExam> data;

    private Context context;

    public ExamDetailsListAdapter(ArrayList<UserExam> data, Context context) {

        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExamDetailsName;
        TextView textViewExamDetailsEmail;
        TextView textViewExamDetailsPhone;
        TextView textViewExamDetailsDNI;
        EditText editTextGrade;
        Button saveGradeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewExamDetailsName = itemView.findViewById(R.id.textViewExamDetailsName);
            textViewExamDetailsEmail = itemView.findViewById(R.id.textViewExamDetailsEmail);
            textViewExamDetailsPhone = itemView.findViewById(R.id.textViewExamDetailsPhone);
            textViewExamDetailsDNI = itemView.findViewById(R.id.textViewExamDetailsDNI);
            editTextGrade = itemView.findViewById(R.id.inputExamDetailsGrade);
            saveGradeButton = itemView.findViewById(R.id.btnBackDetails);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_exam_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserExam item = data.get(position);
        User user = item.getUser();
        holder.textViewExamDetailsName.setText(user.getName());
        holder.textViewExamDetailsEmail.setText(user.getEmail());
        holder.textViewExamDetailsPhone.setText(user.getPhone());
        holder.textViewExamDetailsDNI.setText(user.getDni());

        if(item.getGrade() != null) {
            holder.editTextGrade.setText(item.getGrade().toString());
        }
        //HAcer que el boton guarde the things
        holder.saveGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputGrade = holder.editTextGrade.getText().toString();
                if(!"".equals(inputGrade) && inputGrade != null) {
                    item.setGrade(Double.valueOf(inputGrade));
                    try {
                        Gson gson = new Gson();
                        String jsonInputString = gson.toJson(item);
                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonInputString);
                        String url = "http://10.0.2.2:8080/exam/userExam/save";
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
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
                                    // Process the successful response
                                    String responseBody = response.body().string();
                                    // Update UI or perform further processing
                                    System.out.println("Response: " + responseBody);
                                    UserExam userResponse = gson.fromJson(responseBody, UserExam.class);
                                    System.out.println("UserResponse: " + userResponse);
                                    if (userResponse == null){
                                        //alerta de usuario o contraseña incorrecto

                                    } else {
                                        Activity activity = (Activity) context;
                                        activity.runOnUiThread((new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog msj = new AlertDialog.Builder(context).create();
                                                msj.setTitle("Guardado");
                                                msj.setMessage("La nota se guardó correctamente");
                                                msj.show();
                                            }
                                        }));
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

    @Override
    public int getItemCount() {
        return data.size();
    }
}