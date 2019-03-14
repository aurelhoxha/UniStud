package com.example.unistud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private EditText mEmail, mPassword;
    private Button mLoginButton;
    private TextView mForgotPassword, mSignUp;
    private ProgressDialog mDialog;

    //Authentication to Firebase Instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set the Variables
        mEmail              = (EditText) findViewById(R.id.login_email_address);
        mPassword           = (EditText) findViewById(R.id.login_password);
        mLoginButton        = (Button)   findViewById(R.id.login_button);
        mForgotPassword     = (TextView)   findViewById(R.id.forget_password);
        mSignUp             = (TextView) findViewById(R.id.login_signup);
        mDialog             = new ProgressDialog(this);

        //Set the Listeners
        mLoginButton.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        mSignUp.setOnClickListener(this);

        mAuth         = FirebaseAuth.getInstance();

        if( mAuth.getCurrentUser() != null ) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
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

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });


    }
}