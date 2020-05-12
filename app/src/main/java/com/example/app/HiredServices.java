package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import handlers.FirestoreHandler;
import models.Advert;
import models.User;
import recycleView_cardView.ItemAdapter;
import recycleView_cardView.ItemModel;

public class HiredServices extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Advert> itemsList;
    private User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hired_services);

        Intent oldIntent = this.getIntent();
        MyUser = oldIntent.getParcelableExtra("MyUser");

        recyclerView = findViewById(R.id.rv);
        itemsList = new ArrayList<>();

        //loadServices();

    }

    //Devia pedir os meus serviços mas pede os serviços de uma dada categoria
    private void loadMyServices(){
        FirestoreHandler firestoreHandler = new FirestoreHandler(HiredServices.this, Advert.Category.ALL, MyUser.getuID());
        firestoreHandler.getAdverts(new FirestoreHandler.QueryCallback() {
            @Override
            public void onCallback(List<Advert> list) {
                for (Advert advert : list) {
                    itemsList.add(advert);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager( HiredServices.this);
                RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                ItemAdapter adapter = new ItemAdapter(HiredServices.this, itemsList, RateService.class);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(rvLiLayoutManager);
            }
        });

    }
}
