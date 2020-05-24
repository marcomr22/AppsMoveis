package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class IconCredits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_credits);
        TextView t1 = findViewById(R.id.credits1);
        t1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t2 = findViewById(R.id.credits2);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t3 = findViewById(R.id.credits3);
        t3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t4 = findViewById(R.id.credits4);
        t4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t5 = findViewById(R.id.credits5);
        t5.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t6 = findViewById(R.id.credits6);
        t6.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
