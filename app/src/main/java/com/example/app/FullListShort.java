package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import CustomAdapter.CustomAdapter;
import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.Advert;
import models.User;
import recycleView_cardView.ItemAdapter;

public class FullListShort extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Advert> itemsList;
    private ImageButton imageButton;
    private Spinner spinner;
    private String[] CategoryNames = {"Carpentry","Mechanics","Technology","Cooking","Child Care","Pet Care","Event Planning","Health & Beauty","Other"};
    private int[] images = {R.drawable.icon_carpentry,R.drawable.icon_mecanics,R.drawable.icon_tecnology,R.drawable.icon_cooking,R.drawable.icon_child_care,
                            R.drawable.icon_pet_care,R.drawable.icon_event_planning,R.drawable.icon_health_beauty,R.drawable.icon_others};
    private CustomAdapter adapter;
    private User MyUser;
    private Advert.Category category;
    private  FirestoreHandler firestoreHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list_2);

        imageButton = findViewById(R.id.profilePic);
        itemsList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv);

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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemsList.clear();
                firestoreHandler = new FirestoreHandler(FullListShort.this, Advert.Category.convert(position));
                loadServices();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    loadServices();
                }
            }
        });
    }

    //Pede uma lista com serviços do tipo
    private void loadServices(){
        firestoreHandler.getAdverts(new FirestoreHandler.QueryCallback() {
            @Override
            public void onCallback(List<Advert> list) {
                for (Advert advert : list) {
                    itemsList.add(advert);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager( FullListShort.this);
                RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                ItemAdapter adapter = new ItemAdapter(FullListShort.this, itemsList, ServiceSettings.class);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(rvLiLayoutManager);
            }
        });
    }

}
