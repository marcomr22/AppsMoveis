package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import handlers.AuthHandler;
import models.User;

public class Menu extends AppCompatActivity {
    private Button ProfileSettingsButton;
    private Button MyServicesButton;
    private Button HiredServicesButton;
    private Button LogOutButton;
    private AuthHandler authHandler;
    private ImageButton imageButton;
    private User MyUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent oldIntent = this.getIntent();
        MyUser = oldIntent.getParcelableExtra("MyUser");


        imageButton = findViewById(R.id.profilePic4);
        ProfileSettingsButton = findViewById(R.id.ProfileSettingsButton);
        MyServicesButton = findViewById(R.id.MyServicesButton);
        HiredServicesButton = findViewById(R.id.HiredServicesButton);
        LogOutButton = findViewById(R.id.LogOutButton);

        authHandler = new AuthHandler();

        if(!MyUser.getPhotoURL().equals("") && (MyUser.getPhotoURL() != null)){
            Glide.with(Menu.this).asBitmap().load(MyUser.getPhotoURL()).into(imageButton);
        }

        ProfileSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Re_Authentication.class);
                i.putExtra("MyUser", MyUser);
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

        MyServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (Menu.this, MyServices.class);
                i.putExtra("MyUser", MyUser);
                startActivity(i);
            }
        });

    }
}
