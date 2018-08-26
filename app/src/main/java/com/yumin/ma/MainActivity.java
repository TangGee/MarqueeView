package com.yumin.ma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MarqueeView marqueeView = findViewById(R.id.marquee);
        marqueeView.setText("hahaha");
    }
}
