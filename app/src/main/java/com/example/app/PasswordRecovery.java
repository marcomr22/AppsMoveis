package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import authentication.FirebaseHandler;

public class PasswordRecovery extends AppCompatActivity {
    private EditText email;
    private Button PasswordRecovery;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        email = findViewById(R.id.email);
        PasswordRecovery = findViewById(R.id.RecoverPassword);

        firebaseHandler = new FirebaseHandler();

        PasswordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHandler.RecoverPassword(email, getApplicationContext());
            }
        });
    }


}
