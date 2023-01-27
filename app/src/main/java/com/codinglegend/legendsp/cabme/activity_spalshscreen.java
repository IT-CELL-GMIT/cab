package com.codinglegend.legendsp.cabme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.codinglegend.legendsp.cabme.activities.LoginActivity;
import com.codinglegend.legendsp.cabme.databinding.ActivityLoginBinding;

public class activity_spalshscreen extends AppCompatActivity {

    Animation topanim;

    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalshscreen);
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        textView = findViewById(R.id.text);
        textView.setAnimation(topanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity_spalshscreen.this, LoginActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(textView, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity_spalshscreen.this, pairs);
                startActivity(intent,options.toBundle());
                finish();

            }
        },2500);
    }
}