package com.christine.boda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {


    @BindView(R.id.bike_img) ImageView bikeImage;
    @BindView(R.id.boda_text) TextView bodaText;
    @BindView(R.id.tag_line1) TextView tagLine;
    private static int SPLASH_TIME_OUT = 5000;


    Animation DownTop,Fade,RightToLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        DownTop = AnimationUtils.loadAnimation(this,R.anim.downtop);
        Fade = AnimationUtils.loadAnimation(this,R.anim.fade);
        RightToLeft = AnimationUtils.loadAnimation(this,R.anim.rightoleft);

        bikeImage.setAnimation(RightToLeft);
        YoYo.with(Techniques.Pulse).delay(700).duration(800).playOn(bodaText);
        tagLine.setAnimation(DownTop);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashScreen.this, Register.class));
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
