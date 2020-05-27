package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.app.handlers.AuthHandler;
import com.example.app.handlers.FirestoreHandler;
import com.example.app.models.Advert;
import com.example.app.models.User;
import com.example.app.recycleView_cardView.ItemAdapter;

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
        if(MyUser == null){
            FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
                @Override
                public void onCallback(User user) {
                    MyUser = user;
                    FirestoreHandler.getAdvertsByUser(MyUser.getuID(), new FirestoreHandler.QueryCallback() {
                        @Override
                        public void onCallback(List<Advert> list) {
                            for (Advert advert : list) {
                                itemsList.add(advert);
                            }
                            Log.d("teste", "size: " + list.size() + "   UID:  " + MyUser.getuID() );
                            LinearLayoutManager layoutManager = new LinearLayoutManager( MyServices.this);
                            RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                            ItemAdapter adapter = new ItemAdapter(MyServices.this, itemsList, ServiceSettings.class);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(rvLiLayoutManager);
                        }
                    });
                }
            });
        }else {
            FirestoreHandler.getAdvertsByUser(MyUser.getuID(), new FirestoreHandler.QueryCallback() {
                @Override
                public void onCallback(List<Advert> list) {
                    for (Advert advert : list) {
                        itemsList.add(advert);
                    }
                    Log.d("teste", "size: " + list.size() + "   UID:  " + MyUser.getuID() );
                    LinearLayoutManager layoutManager = new LinearLayoutManager( MyServices.this);
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                    ItemAdapter adapter = new ItemAdapter(MyServices.this, itemsList, ServiceSettings.class);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(rvLiLayoutManager);
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i = new Intent (MyServices.this, Menu.class);
        i.putExtra("MyUser", MyUser);
        startActivity(i);
    }

}