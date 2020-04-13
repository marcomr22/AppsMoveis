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
    private Button ChangePasswordButton;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        OldPassword = findViewById(R.id.old_pass);
        NewPassword = findViewById(R.id.new_pass);
        NewPassword2 = findViewById(R.id.new_pass_confirm);
        ChangePasswordButton = findViewById(R.id.changePasswordButton);

        firebaseHandler = new FirebaseHandler();

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHandler.ChangePassword(getApplicationContext(), OldPassword, NewPassword, NewPassword2);
                Intent i = new Intent (ChangePassword.this, Profile.class);
                startActivity(i);
            }
        });
    }
}
