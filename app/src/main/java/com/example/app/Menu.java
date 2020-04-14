package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import handlers.AuthHandler;

public class Menu extends AppCompatActivity {
    private Button ProfileSettingsButton;
    private Button MyServicesButton;
    private Button HiredServicesButton;
    private Button LogOutButton;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ProfileSettingsButton = findViewById(R.id.ProfileSettingsButton);
        MyServicesButton = findViewById(R.id.MyServicesButton);
        HiredServicesButton = findViewById(R.id.HiredServicesButton);
        LogOutButton = findViewById(R.id.LogOutButton);

        authHandler = new AuthHandler();

        ProfileSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Profile.class);
                startActivity(i);
            }
        });

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.SignOut(getApplicationContext());
                Intent i = new Intent(Menu.this, Login.class);
                startActivity(i);
            }
        });
    }
}
