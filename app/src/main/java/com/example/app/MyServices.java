package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import handlers.FirestoreHandler;
import models.Advert;
import models.User;
import recycleView_cardView.ItemAdapter;
import recycleView_cardView.ItemModel;

public class MyServices  extends AppCompatActivity {

    RecyclerView recyclerView;
    private Button addNew;
    ArrayList<Advert> itemsList;
    private User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        Intent oldIntent = this.getIntent();
        MyUser = oldIntent.getParcelableExtra("MyUser");

        recyclerView = findViewById(R.id.rv);
        itemsList = new ArrayList<>();


        loadMyServices();
        /*
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("https://gd-lisboa.sfo2.cdn.digitaloceanspaces.com/2016/08/pontadapiedadeemlagos.jpg");
        itemsList.add(new Advert("123","123425", Advert.Category.CARPENTRY,"Descrição do produto 1",10,false,strings,0,12));
        itemsList.add(new Advert("2","1234q312325", Advert.Category.OTHER,"Descrição do produto 2",20,true,strings,10,3));
        itemsList.add(new Advert("33","1111123425", Advert.Category.OTHER,"Descrição do produto 3",30,false,strings,2,90));
*/

        addNew = findViewById(R.id.button8);

        /*
        LinearLayoutManager layoutManager = new LinearLayoutManager( this);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;


        ItemAdapter adapter = new ItemAdapter(this, itemsList, ServiceSettings.class);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(rvLiLayoutManager);
         */

        addNew = findViewById(R.id.button8);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyServices.this, ServiceSettings.class);
                i.putExtra("New Service",1);
                i.putExtra("MyUser", MyUser);
                startActivity(i);
            }
        });

    }

    //Devia pedir os meus serviços mas pede os serviços de uma dada categoria
    private void loadMyServices(){
        FirestoreHandler firestoreHandler = new FirestoreHandler(MyServices.this, Advert.Category.OTHER, null);
        firestoreHandler.getAdverts(new FirestoreHandler.QueryCallback() {
            @Override
            public void onCallback(List<Advert> list) {
                Log.d("teste","  " + list.size());
                for (Advert advert : list) {
                    itemsList.add(advert);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager( MyServices.this);
                RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                ItemAdapter adapter = new ItemAdapter(MyServices.this, itemsList, ServiceSettings.class);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(rvLiLayoutManager);
            }
        });

    }

}