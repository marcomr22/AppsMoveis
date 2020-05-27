package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.app.handlers.AuthHandler;

public class ChangePassword extends AppCompatActivity {
    private EditText OldPassword;
    private EditText NewPassword;
    private EditText NewPassword2;
    private Button ChangePasswordButton;
    private AuthHandler authHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);



        OldPassword = findViewById(R.id.old_pass);
        NewPassword = findViewById(R.id.new_pass);
        NewPassword2 = findViewById(R.id.new_pass_confirm);
        ChangePasswordButton = findViewById(R.id.ChangePasswordButton);

        authHandler = new AuthHandler();

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.ChangePassword(getApplicationContext(), OldPassword, NewPassword, NewPassword2);
            }
        });
    }
}
