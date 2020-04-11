package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import authentication.FirebaseHandler;

public class ChangePassword extends AppCompatActivity {
    private EditText OldPassword;
    private EditText NewPassword;
    private EditText NewPassword2;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        OldPassword = findViewById(R.id.OldPassword);
        NewPassword = findViewById(R.id.NewPassword);
        NewPassword2 = findViewById(R.id.NewPassword2);
        Button changePasswordButton = findViewById(R.id.ChangePassword);

        firebaseHandler = new FirebaseHandler();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHandler.ChangePassword(getApplicationContext(), OldPassword, NewPassword, NewPassword2);
            }
        });
    }
}
