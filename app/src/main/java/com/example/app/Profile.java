package com.example.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {
    private Button ChangeUsernameButton;
    private Button ChangeEmailButton;
    private Button ChangeContactButton;
    private Button ChangePasswordButton;
    private Button DeleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ChangeUsernameButton = findViewById(R.id.changeUsernameButton);
        ChangeEmailButton = findViewById(R.id.changeEmailButton);
        ChangeContactButton = findViewById(R.id.changePhoneNumberButton);
        ChangePasswordButton = findViewById(R.id.changePasswordButton);
        DeleteAccountButton = findViewById(R.id.deleteAccountButton);

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (Profile.this, ChangePassword.class);
                startActivity(i);
            }
        });
    }
}
