package com.christine.boda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {
    @BindView (R.id.connect_socialTxtView)TextView socialTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText phoneNoEditText = findViewById(R.id.phoneNoEditText);
        final Button registerBtn = findViewById(R.id.registerBtn);
        phoneNoEditText.setHint(R.string.phone_hint);
        CountryCodePicker ccp;
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ButterKnife.bind(this);

        socialTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, SocialMedia.class);
                startActivity(intent);
            }
        });

    }
}