package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.app.handlers.AuthHandler;

public class Register extends AppCompatActivity{
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText phoneNumber;
    private Button RegisterButton;
    private AuthHandler authHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        authHandler = new AuthHandler();

        RegisterButton = findViewById(R.id.registerButton);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.CreateUser(username, email, phoneNumber, password, getApplicationContext());
            }
        });


    }
}
