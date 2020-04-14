package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import handlers.AuthHandler;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private AuthHandler AuthHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        AuthHandler = new AuthHandler();

        Button LoginButton = findViewById(R.id.Login);
        Button RegisterButton = findViewById(R.id.Register);
        Button PassRecoveryButton = findViewById(R.id.pass_recovery);
        ImageButton LoginGoogle = findViewById(R.id.LoginGoogle);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHandler.SignInUser(email, password, getApplicationContext());
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        LoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHandler.SignInGoogle();
            }
        });

        PassRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, PasswordRecovery.class);
                startActivity(i);
            }
        });

        if (AuthHandler.getUser() != null){
            Intent i = new Intent(Login.this, Menu.class);
            startActivity(i);
        }
    }
}
