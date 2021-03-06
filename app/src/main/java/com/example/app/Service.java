package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.example.app.handlers.AuthHandler;
import com.example.app.handlers.FirestoreHandler;
import com.example.app.models.Advert;
import com.example.app.models.User;

public class Service extends AppCompatActivity {


    private ImageView profilePic5;
    private ImageView profilePic2;
    private RatingBar ratingBar;
    private TextView username;
    private TextView phone;
    private TextView email;
    private TextView price;
    private TextView description;
    private ImageButton servicePic;
    private ImageButton left;
    private ImageButton right;
    private CheckBox hourly;
    private Button hire;

    private User MyUser;
    private Advert advert;
    private  ArrayList<String> imageList;
    private int picNumber = -1;

    //Maybe missing Price and Hourly??
    //Remove Search Bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent oldIntent = this.getIntent();
        loadMyUser();
        advert = getIntent().getParcelableExtra("Advert");

        //profilePic5 = findViewById(R.id.profilePic5);
        profilePic2 = findViewById(R.id.profilePic2);
        ratingBar = findViewById(R.id.ratingBar);
        username = findViewById(R.id.username2);
        email = findViewById(R.id.email2);
        phone = findViewById(R.id.phoneNumber);
        description = findViewById(R.id.serviceDescription);
        servicePic = findViewById(R.id.servicePic);
        left = findViewById(R.id.leftArrow);
        right = findViewById(R.id.rightArrow);
        price = findViewById(R.id.price);
        hourly = findViewById(R.id.hour_rate2);
        hire = findViewById(R.id.comments2);

       // loadMyUser();
        loadOwnerUser();
        loadService();

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picNumber != -1){
                    if(picNumber == 0){
                        picNumber = imageList.size()-1;
                    }else {
                        picNumber--;
                    }
                    Glide.with(Service.this).asBitmap().load(imageList.get(picNumber)).into(servicePic);
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picNumber != -1){
                    if(picNumber == imageList.size()-1){
                        picNumber = 0;
                    }else {
                        picNumber++;
                    }
                    Glide.with(Service.this).asBitmap().load(imageList.get(picNumber)).into(servicePic);
                }
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyUser.getHiredServices() == null){
                    MyUser.setHiredServices(new ArrayList<String>());
                }
                if(!MyUser.getHiredServices().contains(advert.getId())) {
                    MyUser.addHiredService(advert.getId());
                    FirestoreHandler.saveUser(MyUser);
                    Intent i = new Intent(Service.this, FullListShort.class);
                    i.putExtra("MyUser", MyUser);
                    startActivity(i);
                }
            }
        });

    }

    private void loadMyUser(){
        FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                MyUser = user;
                if(!MyUser.getPhotoURL().equals("") && (MyUser.getPhotoURL() != null)){
                    Glide.with(Service.this).asBitmap().load(MyUser.getPhotoURL()).into(profilePic5);
                }
            }
        });
    }

    private void loadOwnerUser(){
        FirestoreHandler.getUser(advert.getOwnerID(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user.getPhotoURL() != null && !user.getPhotoURL().equals("")) {
                    Glide.with(Service.this).asBitmap().load(user.getPhotoURL()).into(profilePic2);
                }
                username.setText(user.getName());
                phone.setText(user.getNumber());
                email.setText(user.getEmail());
            }
        });
    }

    private void loadService(){
        description.setText(advert.getDescription());
        imageList = this.getIntent().getStringArrayListExtra("URLs");
        price.setText(String.valueOf(advert.getPrice()));
        hourly.setChecked(advert.isHourly());
        if(!imageList.isEmpty()){
            picNumber = 0;
            Glide.with(Service.this).asBitmap().load(imageList.get(0)).into(servicePic);
        }
        ratingBar.setRating((float)advert.getRating()/advert.getVoteCount());
    }

}
