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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import handlers.AuthHandler;
import handlers.FirebaseStorageHandler;
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

        title = findViewById(R.id.serviceTitle);
        description = findViewById(R.id.editText4);
        price = findViewById(R.id.price);
        hourlyRate = findViewById(R.id.hour_rate);
        serviceCategories = findViewById(R.id.service_categories);

        finalList = new ArrayList<>();
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

        pic1.setOnClickListener(picturesListener);


    }

    private String generateAID(){
        return UUID.randomUUID().toString();
    }
    /*
    //Creates a random AdvertID
    private String generateAID(){
        String aid;
        String uid = MyUser.getuID();
        Random r = new Random();
        byte[] array = new byte[10];
        int beg = r.nextInt(uid.length()-1);
        r.nextBytes(array);
        String aux = uid.substring(beg);
        String extra = new String(array, Charset.forName("UTF-8"));
        aid = uid + aux.hashCode() + extra;
        return aid;
    }
*/
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

        //save to firestorage

        //Load das URL para cada Imagem
        for (String s: finalList) Log.d("teste", "URL no final vectot: " + s);


        return new Advert(advertId ,AuthHandler.getUser().getUid().toString(),category, description.getText().toString(), Integer.parseInt(price.getText().toString()), hourlyRate.isChecked(),finalList,rating,voteCount);
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
        if(arrayList != null) {
            for (int i = 0; i < arrayList.size() && i < 6; i++) {
                Glide.with(ServiceSettings.this).asBitmap().load(arrayList.get(i)).into(images.get(i));
            }
        }

        rating = a.getRating();
        voteCount = a.getVoteCount();

    }

    // saves the images and saves the URL's
    private void saveImages(){
        for(int i = 0; i < imageList.size(); i++){
            FirebaseStorageHandler.savePicture(imageList.get(i), new FirebaseStorageHandler.ImageSaved() {
                @Override
                public void onComplete(Uri url) {
                    finalList.add(url.toString());
                    Log.d("teste: " , url.toString());
                }
            });
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
            ClipData clipData = data.getClipData();

            //Several Images selected
            if(clipData != null){
                for (int i = 0; i < clipData.getItemCount() && i < 6; i++){
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream stream = getContentResolver().openInputStream(imageUri);
                        Bitmap image = BitmapFactory.decodeStream(stream);
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
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    byte[] imageBytes = stream2.toByteArray();
                    imageList.add(imageBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            saveImages();
            for (int i = 0; i < finalList.size() && i < 6; i++) {
                Glide.with(ServiceSettings.this).asBitmap().load(finalList.get(i)).into(images.get(i));
            }
        }
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

}
