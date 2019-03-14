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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button mSignUpButton;
    private TextView mSignInButton;

    //Firebase Connection Instances
    private FirebaseAuth mAuth;

    //Other Variables
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Intialiaze the Variables
        mEmail           = (EditText)findViewById(R.id.signup_email_address);
        mPassword        = (EditText)findViewById(R.id.signup_password1);
        mConfirmPassword = (EditText)findViewById(R.id.signup_password2);
        mSignUpButton    = (Button)findViewById(R.id.signup_button);
        mSignInButton    = (TextView)findViewById(R.id.signup_sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        //Set the Listeners
        mSignUpButton.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mSignUpButton){
            userSignUp();
        }
        else if(v == mSignInButton) {
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        }
    }

    private void userSignUp(){

        String userEmail, userPassword, userConfirmPassword;
        userEmail           = mEmail.getText().toString().trim();
        userPassword        = mPassword.getText().toString().trim();
        userConfirmPassword = mConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(SignUpActivity.this,"Enter Email",Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(SignUpActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(userConfirmPassword)){
            Toast.makeText(SignUpActivity.this,"Enter Confirm Password",Toast.LENGTH_SHORT).show();
            return;
        }else if(userPassword.length() < 6 ){
            Toast.makeText(SignUpActivity.this,"Password must be greater than 6 letters!",Toast.LENGTH_SHORT).show();
            return;
        } else if(!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(SignUpActivity.this,"Passwords do not match!",Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setMessage("Creating User! Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this, "Error on creating user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
