package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.User;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FirestoreHandler.saveUser(new User("uid0", "nome0", "email@email.com", "url"));

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        authHandler = new AuthHandler();

        Button LoginButton = (Button) findViewById(R.id.Login);
        Button RegisterButton = (Button) findViewById(R.id.Register);
        ImageButton LoginGoogle = (ImageButton) findViewById(R.id.LoginGoogle);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.SignInUser(email, password, getApplicationContext());
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });


        FirestoreHandler.getUser("uid0", new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("teste", user.toString());
            }
        });
    }
}
