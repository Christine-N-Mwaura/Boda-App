package com.christine.boda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.christine.boda.Model.Users;
import com.christine.boda.Prevalent.Prevalent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {


    @BindView(R.id.bike_img)
    ImageView bikeImage;
    @BindView(R.id.boda_text)
    TextView bodaText;
    @BindView(R.id.tag_line1)
    TextView tagLine;
    private static int SPLASH_TIME_OUT = 5000;
    private String UserPhoneKey,UserPassword;


    boolean result;


    Animation DownTop, Fade, RightToLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Paper.init(this);

        UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        UserPassword = Paper.book().read(Prevalent.UserPasswordKey);

        DownTop = AnimationUtils.loadAnimation(this, R.anim.downtop);
        Fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        RightToLeft = AnimationUtils.loadAnimation(this, R.anim.rightoleft);

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
                if (result == true) {
                    RememberMe();
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);

    }

    public boolean RememberMe() {


        if (UserPhoneKey != "" && UserPassword != "") {

            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPassword)) {
                result = true;
                AllowAccess(UserPhoneKey, UserPassword);
            }
        } else {
            result = false;
        }

       return result;
    }



    private void AllowAccess(final String Phone, final String Password) {
        final DatabaseReference dbRef;

        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(Phone).exists())

                {
                    Users usersData = snapshot.child("Users").child(Phone).getValue(Users.class);

                    if(usersData.getPhone().equals(Phone))
                    {
                        if(usersData.getPassword().equals(Password))
                        {
                            //loginProgressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(SplashScreen.this,Destination.class);
                            startActivity(intent);
                        }


                    }
                    else
                    {
//                        loginProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SplashScreen.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
//                    loginProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SplashScreen.this,"User with this number does not exist",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
