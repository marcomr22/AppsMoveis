package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.example.app.handlers.AuthHandler;
import com.example.app.handlers.FirestoreHandler;
import com.example.app.models.Advert;
import com.example.app.models.User;
import com.example.app.recycleView_cardView.ItemAdapter;

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

        FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                MyUser = user;
                loadMyServices();
            }
        });




    }

    //Pede os Hired Services todos do utilizador
    private void loadMyServices(){
        for (String AID: MyUser.getHiredServices()) {
            FirestoreHandler.getAdvertsByAdverId(AID, new FirestoreHandler.QueryCallback() {
                @Override
                public void onCallback(List<Advert> list) {
                for (Advert advert : list) {
                    itemsList.add(advert);
                }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(HiredServices.this);
                    RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
                    ItemAdapter adapter = new ItemAdapter(HiredServices.this, itemsList, RateService.class);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(rvLiLayoutManager);
                }
            });
        }
    }
}
