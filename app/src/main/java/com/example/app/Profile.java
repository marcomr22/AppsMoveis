package com.example.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import handlers.AuthHandler;
import handlers.FirebaseStorageHandler;
import handlers.FirestoreHandler;
import io.grpc.internal.IoUtils;
import models.User;

public class Profile extends AppCompatActivity {
    private static final String TAG = "Profile";
    private Button ChangeUsernameButton;
    private Button ChangeEmailButton;
    private Button ChangeContactButton;
    private Button ChangePasswordButton;
    private Button DeleteAccountButton;
    private EditText PhoneNumber;
    private EditText Email;
    private EditText Username;
    private AuthHandler authHandler;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ChangeUsernameButton = findViewById(R.id.changeUsernameButton);
        ChangeEmailButton = findViewById(R.id.changeEmailButton);
        ChangeContactButton = findViewById(R.id.changePhoneNumberButton);
        ChangePasswordButton = findViewById(R.id.changePasswordButton);
        DeleteAccountButton = findViewById(R.id.deleteAccountButton);
        PhoneNumber = findViewById(R.id.phoneNumber);
        Email = findViewById(R.id.email);
        Username = findViewById(R.id.username);
        imageButton = findViewById(R.id.imageButton2);

        authHandler = new AuthHandler();

        if (FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getSignInProvider().equals("google.com")) {
            ChangeEmailButton.setVisibility(View.GONE);
            ChangePasswordButton.setVisibility(View.GONE);
        }

        FirestoreHandler.getUser(AuthHandler.getUser().getUid(), new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                Username.setText(user.getName());
                PhoneNumber.setText(user.getNumber());
                Email.setText(user.getEmail());
            }
        });

        ChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (Profile.this, ChangePassword.class);
                startActivity(i);
            }
        });

        ChangeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.ChangeEmail(Email, getApplicationContext());
            }
        });

        DeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.deleteUser(getApplicationContext());
                Intent i = new Intent (Profile.this, Login.class);
                startActivity(i);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Pick the Image"), 1);
            }
        });

    }

    //Problemas no guardar a imagem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

           try {
               InputStream stream = getContentResolver().openInputStream(data.getData());
               Bitmap image = BitmapFactory.decodeStream(stream);
               ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
               image.compress(Bitmap.CompressFormat.PNG, 100, stream2);
               imageButton.setImageBitmap(image);
               byte[] imageBytes = IoUtils.toByteArray(stream);

               FirebaseStorageHandler.savePicture(stream2.toByteArray(), new FirebaseStorageHandler.ImageSaved() {
                   @Override
                   public void onComplete(Uri url) {

                   }
               });
           } catch (FileNotFoundException e){
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }

        }
    }
}