package com.christine.boda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText phoneNoEditText = findViewById(R.id.phoneNoEditText);
        final Button registerBtn = findViewById(R.id.registerBtn);
        phoneNoEditText.setHint(R.string.phone_hint);
        CountryCodePicker ccp;
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
    }
}