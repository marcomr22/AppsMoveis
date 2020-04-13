package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import recycleView_cardView.ItemAdapter;
import recycleView_cardView.ItemModel;

public class FullListShort extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ItemModel> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_list_short);

        recyclerView = findViewById(R.id.rv);

        itemsList = new ArrayList<>();

        itemsList.add(new ItemModel(R.drawable.ic_launcher_foreground,"chaat","100"));
        itemsList.add(new ItemModel(R.drawable.ic_launcher_background,"segundaaaaaa","50"));
        itemsList.add(new ItemModel(R.drawable.ic_launcher_foreground,"terceirrrrrra","250"));


        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLiLayoutManager);

        ItemAdapter adapter = new ItemAdapter(this, itemsList);

        recyclerView.setAdapter(adapter);
    }
}
