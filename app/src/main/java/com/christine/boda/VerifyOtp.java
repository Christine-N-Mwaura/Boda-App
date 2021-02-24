package com.christine.boda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyOtp extends AppCompatActivity {
    private String verificationId,phone;
    FirebaseAuth mAuth;


    @BindView(R.id.idBtnVerify) Button btnVerify;
    @BindView(R.id.inputCode1) EditText inputCode1;
    @BindView(R.id.inputCode2) EditText inputCode2;
    @BindView(R.id.inputCode3) EditText inputCode3;
    @BindView(R.id.inputCode4) EditText inputCode4;
    @BindView(R.id.inputCode5) EditText inputCode5;
    @BindView(R.id.inputCode6) EditText inputCode6;
    @BindView(R.id.textMobile) TextView textMobile;
    @BindView(R.id.progressbar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);
        phone = getIntent().getStringExtra("phoneNumber");

        textMobile.setText(String.format(
                "+254-%s",phone
        ));
        verificationId = getIntent().getStringExtra("verificationCode");
        setupOtpInputs();
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCode1.getText().toString().trim().isEmpty()
                        ||inputCode2.getText().toString().trim().isEmpty()
                        ||inputCode3.getText().toString().trim().isEmpty()
                        ||inputCode4.getText().toString().trim().isEmpty()
                        ||inputCode5.getText().toString().trim().isEmpty()
                        ||inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerifyOtp.this, "Please enter a valid code", Toast.LENGTH_SHORT).show();
                    return;
                }

                String code = inputCode1.getText().toString()+
                        inputCode2.getText().toString()+
                        inputCode3.getText().toString()+
                        inputCode4.getText().toString()+
                        inputCode5.getText().toString()+
                        inputCode6.getText().toString();

                if(verificationId  !=null){
                    progressBar.setVisibility(View.VISIBLE);
                    btnVerify.setVisibility(View.INVISIBLE);
                    verifyCode(code);
                }

            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        btnVerify.setVisibility(View.VISIBLE);
                        if(task.isSuccessful()){
                            Intent intent = new Intent(VerifyOtp.this,Register.class);
                            intent.putExtra("phoneNo",phone);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(VerifyOtp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void setupOtpInputs() {
            inputCode1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()){
                        inputCode2.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            inputCode2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()){
                        inputCode3.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            inputCode3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()){
                        inputCode4.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            inputCode4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()){
                        inputCode5.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            inputCode5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()){
                        inputCode6.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });



    }
}