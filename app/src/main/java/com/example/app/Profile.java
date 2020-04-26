package com.example.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.User;

public class Profile extends AppCompatActivity {
    private static final String TAG = "Profile";
    private Button ChangeUsernameButton;
    private Button ChangeEmailButton;
    private Button ChangeContactButton;
    private Button ChangePasswordButton;
    private Button DeleteAccountButton;
    private TextView PhoneNumber;
    private TextView Email;
    private TextView Username;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ChangeUsernameButton = findViewById(R.id.changeUsernameButton);
        ChangeEmailButton = findViewById(R.id.changeEmailButton);
        ChangeContactButton = findViewById(R.id.changePhoneNumberButton);
        ChangePasswordButton = findViewById(R.id.changePasswordButton);
        DeleteAccountButton = findViewById(R.id.deleteAccountButton);
        PhoneNumber = findViewById(R.id.phoneNumber);
        Email = findViewById(R.id.email);
        Username = findViewById(R.id.username);
        authHandler = new AuthHandler();

        PhoneNumber.setText("worked1st");

        if (FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().equals("google.com")) {
            ChangeEmailButton.setVisibility(View.GONE);
            ChangePasswordButton.setVisibility(View.GONE);
        }

        FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                Username.setText(user.getName());
                PhoneNumber.setText("worked2nd");
                Email.setText(user.getEmail());
            }
        });

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (Profile.this, ChangePassword.class);
                startActivity(i);
            }
        });

        ChangeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authHandler.ChangeEmail(EditText) Email, getApplicationContext());
            }
        });

        DeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.deleteUser(getApplicationContext());
            }
        });
    }
}