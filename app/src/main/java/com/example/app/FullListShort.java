package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.User;
import recycleView_cardView.ItemAdapter;
import recycleView_cardView.ItemModel;

public class FullListShort extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ItemModel> itemsList;
    private ImageButton imageButton;
    User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list_2);

        imageButton = findViewById(R.id.profilePic);

        MyUser = new User();

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




    }

}
