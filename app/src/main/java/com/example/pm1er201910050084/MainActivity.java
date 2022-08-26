package com.example.pm1er201910050084;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView imageLogo;
    TextView texto;
    Animation animation1,animation2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        imageLogo.setAnimation(animation1);
        texto.setAnimation(animation2);
        new Handler().postDelayed(this::run, 3000);
    }

    private void init(){
        imageLogo = findViewById(R.id.imageViewLogo);
        texto = findViewById(R.id.txtvU);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.scroll_up);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.scroll_down);
    }

    private void run() {
        Intent principal = new Intent(getApplicationContext(), ActivityPrincipal.class);
        startActivity(principal);
        finish();
    }
}