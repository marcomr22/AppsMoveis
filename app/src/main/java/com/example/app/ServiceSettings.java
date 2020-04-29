package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.common.hash.HashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.Advert;

import static models.Advert.*;

public class ServiceSettings extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private EditText price;
    private CheckBox hourlyRate;
    private RadioGroup serviceCategories;
    private ImageView pic1;
    private ImageView pic2;
    private ImageView pic3;
    private ImageView pic4;
    private ImageView pic5;
    private ImageView pic6;
    private Button confirm;
    private Button delete;
    private Button cancel;
    private  int rating = 0;
    private int voteCount = 0;
    private String advertId = "";
    //Missing to check if a service exists and needs to be checked or if it is a new one
    //Missing buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_settings);
        Intent intent = getIntent();
        int NewService = intent.getIntExtra("New Service",-1);

        title = findViewById(R.id.serviceTitle);
        description = findViewById(R.id.editText4);
        price = findViewById(R.id.price);
        hourlyRate = findViewById(R.id.hour_rate);
        serviceCategories = findViewById(R.id.service_categories);
        pic1 = findViewById(R.id.pic1);
        pic2 = findViewById(R.id.pic2);
        pic3 = findViewById(R.id.pic3);
        pic4 = findViewById(R.id.pic4);
        pic5 = findViewById(R.id.pic5);
        pic6 = findViewById(R.id.pic6);
        confirm = findViewById(R.id.confirmService);
        delete = findViewById(R.id.deleteService);
        cancel = findViewById(R.id.cancelService);



        //In the case of loading an existing Service
        if(NewService == 0) {

            int advertId = intent.getIntExtra("ServiceID", -1);

            //Get service from Database from na id
            //Update the values of the UI

        } else {
            advertId = generateAID();
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHandler.saveAdvert(gatherUiInfo());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHandler.deleteAdvert(gatherUiInfo());
            }
        });


    }

    private String generateAID(){
        String aid = advertId;
        String uid = AuthHandler.getUser().getUid();
        Random r = new Random();
        int beg =  r.nextInt(uid.length()/2) + 6;
        String aux = aid.substring(beg);
        aid = uid + aid + HashCode.fromString(aux);
        return aid;
    }

    //This functions gets all the info in the UI
    public Advert gatherUiInfo() {

        int category_int = serviceCategories.getCheckedRadioButtonId();
        Category category;
        switch (category_int){
            case 0:
                category = Category.CARPENTRY;
                break;
            case 1:
                category = Category.MECHANICS;
                break;
            case 2:
                category = Category.TECHNOLOGY;
                break;
            case 3:
                category = Category.COOKING;
                break;
            case 4:
                category = Category.CHILD;
                break;
            case 5:
                category = Category.PET;
                break;
            case 6:
                category = Category.EVENT;
                break;
            case 7:
                category = Category.HEALTH;
                break;
            default:
                category = Category.OTHER;
        }


        List<String> URLS = new ArrayList<String>();
        Advert advert = new Advert(advertId ,AuthHandler.getUser().getUid().toString(),category, description.getText().toString(), Integer.parseInt(price.getText().toString()), hourlyRate.isChecked(),URLS,rating,voteCount);
        return advert;
    }


}
