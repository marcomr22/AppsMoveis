package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import handlers.AuthHandler;
import models.User;

public class Re_authentication_Delete extends AppCompatActivity {

    private static final String TAG = "Re_Authentication_Delete";
    private EditText password;
    private Button confirm;
    private Button forgotPassword;
    private AuthHandler authHandler;
    private User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_authentication_delete);

        Intent oldIntent = this.getIntent();
        MyUser = oldIntent.getParcelableExtra("MyUser");

        password = findViewById(R.id.password2);
        confirm = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.pass_recovery2);

        authHandler = new AuthHandler();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.deleteUser(password, getApplicationContext());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                authHandler.RecoverPassword(user.getEmail(), getApplicationContext());
            }
        }); }
}
