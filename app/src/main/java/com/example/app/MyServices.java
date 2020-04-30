package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import models.Advert;
import recycleView_cardView.ItemAdapter;
import recycleView_cardView.ItemModel;

public class MyServices  extends AppCompatActivity {

    RecyclerView recyclerView;
    private Button addNew;
    ArrayList<Advert> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        recyclerView = findViewById(R.id.rv);

        itemsList = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("https://gd-lisboa.sfo2.cdn.digitaloceanspaces.com/2016/08/pontadapiedadeemlagos.jpg");
        itemsList.add(new Advert("123","123425", Advert.Category.OTHER,"Descrição do produto 1",10,false,strings,0,12));
        itemsList.add(new Advert("2","1234q312325", Advert.Category.OTHER,"Descrição do produto 2",20,false,strings,10,3));
        itemsList.add(new Advert("33","1111123425", Advert.Category.OTHER,"Descrição do produto 3",30,false,strings,2,90));


        addNew = findViewById(R.id.button8);

        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;


        ItemAdapter adapter = new ItemAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(rvLiLayoutManager);

        addNew = findViewById(R.id.button8);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyServices.this, ServiceSettings.class);
                i.putExtra("New Service",1);
                startActivity(i);
            }
        });



    }

}