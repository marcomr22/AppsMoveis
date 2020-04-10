package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import authentication.FirebaseHandler;

public class Menu extends AppCompatActivity {
    private Button ProfileSettingsButton;
    private Button MyServicesButton;
    private Button HiredServicesButton;
    private Button LogOutButton;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ProfileSettingsButton = findViewById(R.id.button_profile_settings);
        MyServicesButton = findViewById(R.id.MyServicesButton);
        HiredServicesButton = findViewById(R.id.HiredServicesButton);
        LogOutButton = findViewById(R.id.LogOutButton);

        firebaseHandler = new FirebaseHandler();

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
                firebaseHandler.SignOut(getApplicationContext());
                Intent i = new Intent(Menu.this, Login.class);
                startActivity(i);
            }
        });
    }
}
