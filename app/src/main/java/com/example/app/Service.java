package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.User;

public class Service extends AppCompatActivity {

    private SearchView searchView;
    private ImageView profilePic5;
    private ImageView profilePic2;
    private RatingBar ratingBar;
    private TextView username;
    private TextView phone;
    private TextView email;
    private TextView description;
    private ImageView servicePic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        searchView = findViewById(R.id.Search);
        profilePic5 = findViewById(R.id.profilePic5);
        profilePic2 = findViewById(R.id.profilePic2);
        ratingBar = findViewById(R.id.ratingBar);
        username = findViewById(R.id.username2);
        email = findViewById(R.id.email2);
        phone = findViewById(R.id.phoneNumber);
        description = findViewById(R.id.serviceDescription);
        servicePic = findViewById(R.id.servicePic);


        //Alterar para o UID correto
        FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user.getPhotoURL() != null && user.getPhotoURL() != "") {

                    Glide.with(Service.this).asBitmap().load(user.getPhotoURL()).into(profilePic5);

                }
                username.setText(user.getName());
                phone.setText(user.getNumber());
                email.setText(user.getEmail());
            }
        });

    }
}
