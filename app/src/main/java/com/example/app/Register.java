package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import authentication.FirebaseHandler;

public class Register extends AppCompatActivity{
    private EditText username;
    private EditText email;
    private EditText password;
    private Button RegisterButton;
    private FirebaseHandler firebaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firebaseHandler = new FirebaseHandler();

        RegisterButton = findViewById(R.id.appIcon);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHandler.CreateUser(email, password, getApplicationContext());
            }
        });
    }
}
