package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.app.handlers.AuthHandler;

public class PasswordRecovery extends AppCompatActivity {
    private EditText email;
    private Button PasswordRecovery;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        email = findViewById(R.id.email);
        PasswordRecovery = findViewById(R.id.RecoverPassword);

        authHandler = new AuthHandler();

        PasswordRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_aux = email.getText().toString().trim();

                if (email_aux.isEmpty()){
                    email.setError("Please insert your email");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email_aux).matches()) {
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                    return;
                }

                authHandler.RecoverPassword(email_aux, getApplicationContext());
            }
        });
    }


}
