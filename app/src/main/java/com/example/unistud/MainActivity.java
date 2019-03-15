package com.example.unistud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //CONSTANTS
    private static final int RC_SIGN_IN = 1;

    //Variables
    private EditText mEmail, mPassword;
    private Button mLoginButton;
    private TextView mForgotPassword, mSignUp;
    private LoginButton mLoginFacebookButton;
    private SignInButton mLoginGoogleButton;
    private ProgressDialog mDialog;

    //Facebook Variable
    private CallbackManager mCallbackManager;

    //Google Variables
    private GoogleApiClient mGoogleApiClient;

    //Authentication to Firebase Instances
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        //Set the Variables
        mEmail               = (EditText)findViewById(R.id.login_email_address);
        mPassword            = (EditText)findViewById(R.id.login_password);
        mLoginButton         = (Button)findViewById(R.id.login_button);
        mForgotPassword      = (TextView)findViewById(R.id.forget_password);
        mSignUp              = (TextView)findViewById(R.id.login_signup);
        mDialog              = new ProgressDialog(this);
        mLoginFacebookButton = (LoginButton)findViewById(R.id.login_button_facebook);
        mLoginGoogleButton   = (SignInButton)findViewById(R.id.login_button_google);

        //Configure Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //Configure Facebook
        mCallbackManager     = CallbackManager.Factory.create();
        mLoginFacebookButton.setReadPermissions(Arrays.asList("email"));

        //Configure Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this,"You got an error!", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //Set the Listeners
        mLoginButton.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        mSignUp.setOnClickListener(this);
        mLoginFacebookButton.setOnClickListener(this);
        mLoginGoogleButton.setOnClickListener(this);

        mAuth         = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if(v == mLoginButton) {
            userEmailSignIn();
        }
        else if(v == mForgotPassword) {
            startActivity(new Intent(MainActivity.this,ForgotPassword.class));
        }
        else if(v == mSignUp) {
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
        }
        else if(v == mLoginFacebookButton) {
            userFacebookSignIn();
        }
        else if(v == mLoginGoogleButton) {
            userGoogleSignIn();
        }
    }

    private void userEmailSignIn(){
        String userEmail = mEmail.getText().toString().trim();
        String userPassword = mPassword.getText().toString().trim();

        //Check For Empty
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(MainActivity.this,"Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(MainActivity.this,"Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setMessage("Login please wait...");
        mDialog.setIndeterminate(true);
        mDialog.show();

        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( !task.isSuccessful() ){
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Login not successful!",Toast.LENGTH_LONG).show();
                }else{
                    mDialog.dismiss();
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserTable = mDatabase.child(userId);
                    currentUserTable = currentUserTable.child("account_type");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    private void userFacebookSignIn(){
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"User cancelled it", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void userGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GOOGLE ERROR", "Google sign in failed", e);
                // ...
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GOOGLE ERROR", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("GOOGLE ERROR", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Authentication Failed!", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }

    private void handleFacebookToken(AccessToken mAccessToken) {
        AuthCredential mCredential = FacebookAuthProvider.getCredential(mAccessToken.getToken());
        mAuth.signInWithCredential(mCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Could not register to Firebase", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}