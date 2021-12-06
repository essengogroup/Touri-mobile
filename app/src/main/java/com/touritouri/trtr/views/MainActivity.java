package com.touritouri.trtr.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.touritouri.trtr.R;

public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler.postDelayed(() -> {
            startActivity(new Intent(this,AccueilActivity.class));
            finish();
        }, 2000);
    }
}