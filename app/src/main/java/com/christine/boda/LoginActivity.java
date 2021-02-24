package com.christine.boda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.Toast;

import com.christine.boda.Model.Users;
import com.christine.boda.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.remember_me_chkbx) CheckBox rememberMeChkbx;
    @BindView(R.id.et_phoneNo) EditText etPhoneNo;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.login_Btn) Button loginBtn;
    @BindView(R.id.login_country_code_picker) CountryCodePicker ccp;
    @BindView(R.id.login_progress_bar) ProgressBar loginProgressBar;
    @BindView(R.id.create_Acc_Btn) Button createAccBtn;
    String Phone, Password, userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Paper.init(this);

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phone = etPhoneNo.getText().toString();
                Password = etPassword.getText().toString();
                LoginAccount(Phone,Password);


            }
        });
    }

    private void LoginAccount(String phone, String password) {
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else {
            if(rememberMeChkbx.isChecked())
            {

                Paper.book().write(Prevalent.UserPhoneKey, phone);
                Paper.book().write(Prevalent.UserPasswordKey,password);
            }
            loginProgressBar.setVisibility(View.VISIBLE);

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
                                Intent intent = new Intent(LoginActivity.this,Destination.class);
                                startActivity(intent);
                            }


                        }
                        else
                        {
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        loginProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this,"User with this number does not exist",Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }
}