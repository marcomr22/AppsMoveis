package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.app.models.Advert;
import com.example.app.models.User;

public class RateService extends AppCompatActivity {

    private ImageView image;
    private TextView desc;
    private TextView price;
    private RatingBar ratingBar;
    private EditText comment;
    private Button comfirmButton;

    private Advert advert;
    private User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_service);

        image = findViewById(R.id.item_image);
        desc = findViewById(R.id.item_description);
        price = findViewById(R.id.item_price);
        ratingBar = findViewById(R.id.ratingBar2);
        comment = findViewById(R.id.editText);
        comfirmButton = findViewById(R.id.confirmService);

        Intent oldIntent = this.getIntent();
        MyUser = oldIntent.getParcelableExtra("MyUser");
        advert = oldIntent.getParcelableExtra("Advert");

        loadAddToUI();

        comfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });


    }

    private void loadAddToUI(){
        if(!advert.getImagesURL().isEmpty()){
            Glide.with(RateService.this).asBitmap().load(advert.getImagesURL().get(0)).into(image);
        }
        desc.setText(advert.getDescription());
        price.setText(String.valueOf(advert.getPrice()));
    }

    // Supposed to save the comment and the Rating
    private void saveComment(){

    }
}
