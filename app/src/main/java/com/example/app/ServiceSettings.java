package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.hash.HashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.Advert;
import models.User;

import static models.Advert.*;

public class ServiceSettings extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private EditText price;
    private CheckBox hourlyRate;
    private RadioGroup serviceCategories;
    private ImageButton pic1;
    private ImageButton pic2;
    private ImageButton pic3;
    private ImageButton pic4;
    private ImageButton pic5;
    private ImageButton pic6;
    private Button confirm;
    private Button delete;
    private Button cancel;

    private  int rating = 0;
    private int voteCount = 0;
    private String advertId = "";
    private ArrayList<ImageButton> images;
    private User MyUser;
    private Category category;
    private Advert a;

    //Missing buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_settings);
        Intent oldIntent = getIntent();

        int NewService = oldIntent.getIntExtra("New Service",0);
        MyUser = oldIntent.getParcelableExtra("MyUser");

        title = findViewById(R.id.serviceTitle);
        description = findViewById(R.id.editText4);
        price = findViewById(R.id.price);
        hourlyRate = findViewById(R.id.hour_rate);
        serviceCategories = findViewById(R.id.service_categories);

        images = new ArrayList<>();

        pic1 = findViewById(R.id.pic1);
        pic2 = findViewById(R.id.pic2);
        pic3 = findViewById(R.id.pic3);
        pic4 = findViewById(R.id.pic4);
        pic5 = findViewById(R.id.pic5);
        pic6 = findViewById(R.id.pic6);
        images.add(pic1);
        images.add(pic2);
        images.add(pic3);
        images.add(pic4);
        images.add(pic5);
        images.add(pic6);

        confirm = findViewById(R.id.confirmService);
        delete = findViewById(R.id.deleteService);
        cancel = findViewById(R.id.cancelService);

        //In the case of loading an existing Service
        if(NewService == 0) {
            loadAdvert();
        } else {
            advertId = generateAID();
            a = new Advert();
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gatherUiInfo() != a){
                    FirestoreHandler.saveAdvert(gatherUiInfo());
                    Intent intent = new Intent(ServiceSettings.this, MyServices.class);
                    intent.putExtra("MyUser", MyUser);
                    startActivity(intent);
                }else {
                    Toast.makeText(ServiceSettings.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceSettings.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ServiceSettings.this, MyServices.class);
                intent.putExtra("MyUser", MyUser);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHandler.deleteAdvert(a);
                Toast.makeText(ServiceSettings.this, "Your service was deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ServiceSettings.this, MyServices.class);
                intent.putExtra("MyUser", MyUser);
                startActivity(intent);
            }
        });

    }

    //Creates a random AdvertID
    private String generateAID(){
        String aid;
        String uid = MyUser.getuID();
        Random r = new Random();
        int beg = r.nextInt(uid.length()/2);
        String aux = uid.substring(beg);
        aid = uid + aux.hashCode();
        return aid;
    }

    //This functions gets all the info in the UI
    public Advert gatherUiInfo() {

        int selectedId = serviceCategories.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        if(selectedId == -1){
            Toast.makeText(this, "No Category Selected", Toast.LENGTH_SHORT).show();
            return this.a;
        }
        if(description.getText().toString().equals("")){
            Toast.makeText(this, "No Description", Toast.LENGTH_SHORT).show();
            return this.a;
        }
        if(price.getText().toString().equals("")){
            Toast.makeText(this, "No Price", Toast.LENGTH_SHORT).show();
            return this.a;
        }
        category = Category.convert(radioButton.getText().toString().toUpperCase());

        List<String> URLS = new ArrayList<String>();
        //Load das URL para cada Imagem
        Advert advert = new Advert(advertId ,AuthHandler.getUser().getUid().toString(),category, description.getText().toString(), Integer.parseInt(price.getText().toString()), hourlyRate.isChecked(),URLS,rating,voteCount);
        return advert;
    }

    private void loadAdvert(){

        //Load 1 advert
        a = getIntent().getParcelableExtra("Advert");

        description.setText(a.getDescription());
        price.setText(String.valueOf(a.getPrice()));
        hourlyRate.setChecked(a.isHourly());

        serviceCategories.check(serviceCategories.getChildAt(Category.getValue(a.getCategory())).getId());

        for(int i = 0; i < a.getImagesURL().size(); i++) {
            Glide.with(ServiceSettings.this).asBitmap().load(a.getImagesURL().get(i)).into(images.get(i));
        }

        rating = a.getRating();
        voteCount = a.getVoteCount();

    }



}
