package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

import com.example.app.handlers.AuthHandler;
import com.example.app.handlers.FirestoreHandler;
import com.example.app.models.Advert;
import com.example.app.models.User;

public class Login extends AppCompatActivity{
    private static final String TAG = "Google Sign In";
    private EditText email;
    private EditText password;
    private AuthHandler authHandler;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        authHandler = new AuthHandler();

        Button LoginButton = findViewById(R.id.Login);
        Button RegisterButton = findViewById(R.id.Register);
        Button PassRecoveryButton = findViewById(R.id.pass_recovery);
        ImageButton LoginGoogle = findViewById(R.id.LoginGoogle);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.SignInUser(email, password, getApplicationContext());
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
                signInGoogle();
            }
        });

        PassRecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, PasswordRecovery.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("test", "initiated");
        FirestoreHandler
                .saveAdvert(new Advert("advert12", "uid0", Advert.Category.CARPENTRY, "está funcionar", 12, false, new ArrayList<String>(), 10,10));


        if (AuthHandler.getUser() != null){
            Intent i = new Intent(Login.this, FullListShort.class);
            startActivity(i);
        }

        FirestoreHandler.getUser("uid0", new FirestoreHandler.UserCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("Test", user.toString());

            }
        });

    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("APP", "onActivityRESULT CALLED1");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("APP", "onActivityRESULT BEOFRE FIREBASE!!!");
                authHandler.firebaseAuthWithGoogle(account,getApplicationContext());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }
}
