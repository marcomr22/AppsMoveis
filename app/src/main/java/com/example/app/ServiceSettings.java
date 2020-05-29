package com.example.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.app.handlers.AuthHandler;
import com.example.app.handlers.FirebaseStorageHandler;
import com.example.app.handlers.FirestoreHandler;
import com.example.app.models.Advert;
import com.example.app.models.User;

import static com.example.app.models.Advert.*;

public class ServiceSettings extends AppCompatActivity {

    private EditText description;
    private EditText price;
    private CheckBox hourlyRate;
    private RadioGroup serviceCategories;
    private Button confirm;
    private Button delete;
    private Button cancel;

    private  int rating = 0;
    private int voteCount = 0;
    private int picNumber = -1;
    private String advertId = "";
    private ImageButton image;
    private ImageButton left;
    private ImageButton right;
    private User MyUser;
    private Category category;
    private ArrayList<Bitmap> bitmapList = new ArrayList<>();
    List<byte[]> imageList = new ArrayList<>();
    List<String> finalList;
    private Advert a;


    //Missing buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_settings);
        Intent oldIntent = getIntent();

        //Load de User
        int NewService = oldIntent.getIntExtra("New Service",0);
        MyUser = oldIntent.getParcelableExtra("MyUser");

        description = findViewById(R.id.editText4);
        price = findViewById(R.id.price);
        hourlyRate = findViewById(R.id.hour_rate);
        serviceCategories = findViewById(R.id.service_categories);

        finalList = new ArrayList<>();
        image = findViewById(R.id.servicePic2);
        left = findViewById(R.id.leftArrow2);
        right = findViewById(R.id.rightArrow2);

        confirm = findViewById(R.id.confirmService);
        delete = findViewById(R.id.deleteService);
        cancel = findViewById(R.id.cancelService);

        //In the case of loading an existing Service
        if(NewService == 0) {
            loadAdvert();
        } else {
            advertId = generateAID();
            a = new Advert();
            a.setImagesURL(new ArrayList<String>());
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Advert aux = gatherUiInfo();
                if(aux != a){
                    FirestoreHandler.saveAdvert(aux);
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

        image.setOnClickListener(picturesListener);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bitmapList.isEmpty()){
                    if(picNumber != -1) {
                        if (picNumber == 0) {
                            picNumber = bitmapList.size() - 1;
                        } else {
                            picNumber--;
                        }
                        image.setImageBitmap(bitmapList.get(picNumber));
                    }
                } else if(picNumber != -1){
                       if(picNumber == 0){
                           picNumber = finalList.size()-1;
                       }else {
                           picNumber--;
                       }
                       Glide.with(ServiceSettings.this).asBitmap().load(finalList.get(picNumber)).into(image);
                   }
                }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bitmapList.isEmpty()){
                    if(picNumber != -1) {
                        if (picNumber == bitmapList.size() - 1) {
                            picNumber = 0;
                        } else {
                            picNumber++;
                        }
                        image.setImageBitmap(bitmapList.get(picNumber));
                    }
                } else if(picNumber != -1){
                    if(picNumber == finalList.size()-1){
                        picNumber = 0;
                    }else {
                        picNumber++;
                    }
                    Glide.with(ServiceSettings.this).asBitmap().load(finalList.get(picNumber)).into(image);
                }
            }
        });
    }

    private String generateAID(){
        return UUID.randomUUID().toString();
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

        //Load das URL para cada Imagem
        for (String s: finalList) Log.d("teste", "URL no final vectot: " + s);


        String desc = description.getText().toString();
        float priceValue = Float.parseFloat(String.valueOf(price.getText()));

        return new Advert(advertId ,AuthHandler.getUser().getUid().toString(),category, desc, priceValue , hourlyRate.isChecked(),finalList,rating,voteCount);
    }

    private void loadAdvert(){

        //Load 1 advert
        a = getIntent().getParcelableExtra("Advert");
        advertId = a.getId();

        description.setText(a.getDescription());
        price.setText(String.valueOf(a.getPrice()));
        hourlyRate.setChecked(a.isHourly());

        serviceCategories.check(serviceCategories.getChildAt(Category.getValue(a.getCategory())).getId());


        ArrayList<String> arrayList = this.getIntent().getStringArrayListExtra("URLs");
        if(!arrayList.isEmpty()) {
            finalList = arrayList;
            picNumber = 0;
            Glide.with(ServiceSettings.this).asBitmap().load(arrayList.get(picNumber)).into(image);
        }

        rating = a.getRating();
        voteCount = a.getVoteCount();

    }

    // saves the images and saves the URL's
    private void saveImages(){
        finalList = new ArrayList<>();
        for(int i = 0; i < imageList.size(); i++){
            FirebaseStorageHandler.savePicture(imageList.get(i), new FirebaseStorageHandler.ImageSaved() {
                @Override
                public void onComplete(Uri url) {
                    finalList.add(url.toString());
                    Log.d("teste" , url.toString());
                }
            });
            if(!finalList.isEmpty()) {
                Glide.with(ServiceSettings.this).asBitmap().load(finalList.get(0)).into(image);
            }
        }
    }

    //Images Listener
    View.OnClickListener picturesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(checkExternalStoragePermission()){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pick the Images"), 6);
            }
            else {
                verifyPermission();
            }
        }
    };

    //Images association
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 6) {

            imageList = new ArrayList<>();
            bitmapList = new ArrayList<Bitmap>();
            ClipData clipData = data.getClipData();

            //Several Images selected
            if(clipData != null){
                for (int i = 0; i < clipData.getItemCount() && i < 6; i++){
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream stream = getContentResolver().openInputStream(imageUri);
                        Bitmap image = BitmapFactory.decodeStream(stream);
                        bitmapList.add(image);
                        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                        byte[] imageBytes = stream2.toByteArray();
                        imageList.add(imageBytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            //1 Image selected
            } else{
                try {
                    InputStream stream = getContentResolver().openInputStream(data.getData());
                    Bitmap image = BitmapFactory.decodeStream(stream);
                    bitmapList.add(image);
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    byte[] imageBytes = stream2.toByteArray();
                    imageList.add(imageBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            displayImages();
            saveImages();
        }
    }

    private void displayImages(){
        picNumber = 0;
        image.setImageBitmap(bitmapList.get(0));
    }

    //Permissions
    private void verifyPermission(){
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        ActivityCompat.requestPermissions(ServiceSettings.this, new String[]{permission},1);
    }

    //Permissions
    private boolean checkExternalStoragePermission(){
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int permissionRequest = ActivityCompat.checkSelfPermission(ServiceSettings.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i = new Intent (ServiceSettings.this, MyServices.class);
        i.putExtra("MyUser", MyUser);
        startActivity(i);
    }

}
