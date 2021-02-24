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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.textUser) TextView mTextUser;
    @BindView(R.id.facebook_img) ImageView mFacebookImg;
    @BindView(R.id.login_button) LoginButton mLoginBtn;
    @BindView(R.id.signInButton) SignInButton mSignInbtn;
    @BindView(R.id.sign_out_btn) Button mSignOutBtn;

    String Tag;
    private int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        ButterKnife.bind(this);


        //firebase auth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        //google signin
        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //set onClickListener to the signin button to trigger sign in
        mSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //set onClickListener for sign out button and hide the button once user is signed out
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(SocialMedia.this,"You are now logged out",Toast.LENGTH_LONG).show();
                mSignOutBtn.setVisibility(View.INVISIBLE);
            }
        });

        //set read permissions for email and profile for Facebook
        mLoginBtn.setReadPermissions("email","public_profile");


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

    // google sign in button implementation
    private void signIn() {
        //use intent to call Google signin client
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
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

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
    //handle the google account result chosen by the user
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(SocialMedia.this,"Signed In Successfully", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(acc);
        }catch (ApiException e){
            Toast.makeText(SocialMedia.this,"Sign In Failed", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }


    }
    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SocialMedia.this,"Successful", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateGui(user);
                }
                else{
                    Toast.makeText(SocialMedia.this,"Failed",Toast.LENGTH_LONG).show();
                    updateGui(null);
                }
            }

        });
    }
    private void updateGui(FirebaseUser fUser){
        mSignOutBtn.setVisibility(View.VISIBLE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if(account != null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            Toast.makeText(SocialMedia.this, personName + personEmail, Toast.LENGTH_LONG).show();
        }

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