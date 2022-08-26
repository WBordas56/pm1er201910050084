package com.example.pm1er201910050084;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imageLogo;
    Animation animation1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        imageLogo.setAnimation(animation1);
        new Handler().postDelayed(this::run, 3000);
    }

    private void init(){
        imageLogo = findViewById(R.id.imageViewLogo);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.scroll_up);
    }

    private void run() {
        Intent principal = new Intent(getApplicationContext(), ActivityPrincipal.class);
        startActivity(principal);
        finish();
    }
}