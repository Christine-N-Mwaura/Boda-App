package com.christine.boda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity {
    @BindView (R.id.connect_socialTxtView)TextView socialTxtView;
    @BindView(R.id.getOtp) Button btnGetOtp;
    @BindView(R.id.phoneNoEditText) EditText etPhoneNumber;
    CountryCodePicker ccp;





    String phoneNumber;
    private FirebaseAuth mAuth;
    private String verificationCode;
    String Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mAuth =FirebaseAuth.getInstance();


        etPhoneNumber.setHint(R.string.phone_hint);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        socialTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SocialMedia.class);
                startActivity(intent);
            }
        });

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etPhoneNumber.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                }else{
                    phoneNumber = "+254" + etPhoneNumber.getText().toString();
                    sendVerificationCode(phoneNumber);
                }

            }
        });




    }

    private void sendVerificationCode(String phoneNumber) {
        // this method is used for sending OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                MainActivity.this,
                mCallbacks

        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationCode = s;
                    Intent intent = new Intent(MainActivity.this,VerifyOtp.class);
                    intent.putExtra("phoneNumber",etPhoneNumber.getText().toString());
                    intent.putExtra("verificationCode",verificationCode);
                    startActivity(intent);
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            };


}