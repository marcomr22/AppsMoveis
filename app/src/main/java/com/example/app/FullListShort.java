package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import CustomAdapter.CustomAdapter;
import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.Advert;
import models.User;
import recycleView_cardView.ItemAdapter;
import recycleView_cardView.ItemModel;

public class FullListShort extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ItemModel> itemsList;
    private ImageButton imageButton;
    private Spinner spinner;
    private String[] CategoryNames = {"Carpentry","Mechanics","Technology","Cooking","Child Care","Pet Care","Event Planning","Health & Beauty","Other"};
    private int[] images = {R.drawable.icon_carpentry,R.drawable.icon_mecanics,R.drawable.icon_tecnology,R.drawable.icon_cooking,R.drawable.icon_child_care,
                            R.drawable.icon_pet_care,R.drawable.icon_event_planning,R.drawable.icon_health_beauty,R.drawable.icon_others};
    private CustomAdapter adapter;
    private User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list_2);

        imageButton = findViewById(R.id.profilePic);

        FirestoreHandler firestoreHandler = new FirestoreHandler(this, Advert.Category.CARPENTRY, null);

        MyUser = new User();
        spinner = findViewById(R.id.spinner3);

        FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                MyUser = user;
                if(!MyUser.getPhotoURL().equals("") && (MyUser.getPhotoURL() != null)){
                   Glide.with(FullListShort.this).asBitmap().load(MyUser.getPhotoURL()).into(imageButton);
                }
            }
        });



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullListShort.this, Menu.class);
                intent.putExtra("MyUser", MyUser);
                startActivity(intent);
            }
        });

        adapter = new CustomAdapter(this, CategoryNames, images);
        spinner.setAdapter(adapter);


    }

}
