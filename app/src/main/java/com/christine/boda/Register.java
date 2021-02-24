package com.christine.boda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    @BindView(R.id.fNameEditTxt) EditText etFName;
    @BindView(R.id.lNameEditTxt) EditText etLName;
    @BindView(R.id.emailEditTxt) EditText etEmail;
    @BindView(R.id.passwordEditTxt) EditText etPassword;
    @BindView(R.id.createAccBtn) Button btnCreateAcc;
    ProgressDialog LoadingBar;
    String Lname,Fname,Password,Email,phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        final Drawable upArrow =  ContextCompat.getDrawable(Register.this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(Register.this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //style for actionbar color and text
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("");

        LoadingBar = new ProgressDialog(this);

        LoadingBar.setTitle("Creating Account");
        LoadingBar.setMessage("Please wait,while we verify your password");
        LoadingBar.setCanceledOnTouchOutside(false);


        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get values entered by user
                Fname = etFName.getText().toString();
                Lname = etLName.getText().toString();
                Email = etEmail.getText().toString();
                Password = etPassword.getText().toString();
                phone = getIntent().getStringExtra("phoneNo");

                CreateNewAccount(Fname,Lname,Email,Password,phone);
        }

        });





    }
    private void CreateNewAccount(String fname, String lname, String email, String password, String phone) {
        //check if the edit texts are empty
        if(TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lname)){
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        }
        else{
//            Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
        }

        //begin saving the data
        final DatabaseReference mDbRef;
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Users").child(phone).exists())
                {
                    //if the user does not exist, create a new account
                    HashMap<String,Object> userdata = new HashMap<>();
                    userdata.put("Phone",phone);
                    userdata.put("FirstName",fname);
                    userdata.put("LastName",lname);
                    userdata.put("EmailAddress",email);
                    userdata.put("Password",password);


                    mDbRef.child("Users").child(phone).updateChildren(userdata)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        LoadingBar.dismiss();
                                        Toast.makeText(Register.this, "Registeration successful", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        LoadingBar.dismiss();
                                        Toast.makeText(Register.this, "Please retry after sometime", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    LoadingBar.dismiss();
                    Toast.makeText(Register.this, "User with this number already exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        
    }
}

