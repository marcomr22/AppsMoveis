package com.example.app;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.media.Image;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;

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
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.GoogleAuthProvider;

        import handlers.AuthHandler;
        import handlers.FirestoreHandler;
        import models.User;

public class Re_Authentication extends AppCompatActivity {
    private static final String TAG = "Re_Authentication";
    private EditText email;
    private EditText email_check;
    private EditText password;
    private Button confirm;
    private Button forgotPassword;
    private AuthHandler authHandler;
    private User MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_authentication);

        Intent oldIntent = this.getIntent();
        MyUser = oldIntent.getParcelableExtra("MyUser");

        email = findViewById(R.id.newEmail);
        email_check = findViewById(R.id.newEmail_2);
        password = findViewById(R.id.password2);
        confirm = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.pass_recovery2);

        authHandler = new AuthHandler();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authHandler.ChangeEmail(email, email_check, password, getApplicationContext());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                authHandler.RecoverPassword(user.getEmail(), getApplicationContext());
            }
        }); }
}


