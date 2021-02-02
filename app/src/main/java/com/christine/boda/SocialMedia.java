package com.christine.boda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SocialMedia extends AppCompatActivity {
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    @BindView(R.id.textUser) TextView mTextUser;
    @BindView(R.id.facebook_img) ImageView mFacebookImg;
    @BindView(R.id.login_button) LoginButton mLoginBtn;
    String Tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        ButterKnife.bind(this);


        mLoginBtn.setReadPermissions("email","public_profile");

        mFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

        mCallbackManager =  CallbackManager.Factory.create();
        mLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(Tag,"onsuccess" + loginResult);

                handleAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(Tag,"onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Tag,"onError" + error);

            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user !=null){
                    updateUi(user);
                }
                else {
                    updateUi(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    mFirebaseAuth.signOut();
                }
            }
        };
    }

    private void handleAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(Tag, "sign with credentials");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateUi(user);
                }
                else {
                    Log.d(Tag, "Authentications Failed",task.getException());
                    Toast.makeText(SocialMedia.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                    updateUi(null);


                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUi(FirebaseUser user) {

        if (user !=null){
            mTextUser.setText(user.getDisplayName());

            if (user.getPhotoUrl() != null){
            String photoUrl = user.getPhotoUrl().toString();
            photoUrl = photoUrl + "?type=large";

                Picasso.get().load(photoUrl).into(mFacebookImg);

            }
        }

        else {
            mTextUser.setText("");
            mFacebookImg.setImageResource(R.drawable.logo);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}