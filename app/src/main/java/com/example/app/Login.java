package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import handlers.AuthHandler;
import handlers.FirestoreHandler;
import models.Advert;
import models.User;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private AuthHandler AuthHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FirestoreHandler.saveAdvert(new Advert("aid0", "uid0", Advert.Category.CARPENTRY, "test", 50, true, Arrays.asList("asdf")));
        FirestoreHandler.saveAdvert(new Advert("aid1", "uid0", Advert.Category.CARPENTRY, "test5", 50, true, Arrays.asList("asdf"), 10, 2));
        FirebaseFirestore bd = FirebaseFirestore.getInstance();
        bd.collectionGroup("advert").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("test", queryDocumentSnapshots.getDocuments().toString());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("test", "onFailure: " + e.toString());
                    }
                });

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        AuthHandler = new AuthHandler();

        Button LoginButton = findViewById(R.id.Login);
        Button RegisterButton = findViewById(R.id.Register);
        Button PassRecoveryButton = findViewById(R.id.pass_recovery);
        ImageButton LoginGoogle = findViewById(R.id.LoginGoogle);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHandler.SignInUser(email, password, getApplicationContext());
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        LoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHandler.SignInGoogle();
            }
        });

        PassRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, PasswordRecovery.class);
                startActivity(i);
            }
        });

        if (AuthHandler.getUser() != null){
            Intent i = new Intent(Login.this, Menu.class);
            startActivity(i);
        }

        FirestoreHandler.getUser("uid0", new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("Test", user.toString());

            }
        });

    }
}
