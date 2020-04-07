package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button LoginButton = (Button) findViewById(R.id.Login);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Invalid E-mail or Password";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
            }
        });
    }

    public void registerButtonClick(View v){
        Intent i = new Intent(MainActivity.this, Register.class);
        startActivity(i);
    }

}
