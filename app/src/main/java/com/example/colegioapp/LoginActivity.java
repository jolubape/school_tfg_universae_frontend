package com.example.colegioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.colegioapp.dto.LoginCredentials;
import com.example.colegioapp.dto.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.inputEmail);
        password = (EditText) findViewById(R.id.inputPassword);
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginCredentials loginCredentials = new LoginCredentials();
                loginCredentials.setEmail(email.getText().toString());
                loginCredentials.setPassword(password.getText().toString());

                try {
                    Gson gson = new Gson();
                    String jsonInputString = gson.toJson(loginCredentials);
                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonInputString);
                    String url = "http://10.0.2.2:8080/login";
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
                                User userResponse = gson.fromJson(responseBody, User.class);
                                System.out.println("UserResponse: " + userResponse);
                                if (userResponse == null){
                                    //alerta de usuario o contraseña incorrecto

                                } else {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                    // Put the ResponseObject as an extra in the Intent
                                    intent.putExtra("userResponse", userResponse);

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
                    AlertDialog msj = new AlertDialog.Builder(LoginActivity.this).create();
                    msj.setTitle("Error");
                    msj.setMessage("El servicio no esta disponible");
                    msj.show();
                }
            }
        });
    }
}