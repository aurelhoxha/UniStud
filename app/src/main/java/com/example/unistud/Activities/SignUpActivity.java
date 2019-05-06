package com.example.unistud.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    //Variables
    private EditText mEmail, mPassword, mConfirmPassword, mUserFullName;
    private Button mSignUpButton;
    private TextView mSignInButton;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;
    //Firebase Connection Instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageReference;

    //Other Variables
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Intialiaze the Variables
        mUserFullName    = (EditText)findViewById(R.id.signup_user_fullname);
        mEmail           = (EditText)findViewById(R.id.signup_email_address);
        mPassword        = (EditText)findViewById(R.id.signup_password1);
        mConfirmPassword = (EditText)findViewById(R.id.signup_password2);
        mRadioGroup      = (RadioGroup)findViewById(R.id.account_type);
        mSignUpButton    = (Button)findViewById(R.id.signup_button);
        mSignInButton    = (TextView)findViewById(R.id.signup_sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDialog = new ProgressDialog(this);
        mStorageReference = FirebaseStorage.getInstance().getReference("DefaultFiles");

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

        final String userEmail, userPassword, userConfirmPassword, userName;
        userName            = mUserFullName.getText().toString().trim();
        userEmail           = mEmail.getText().toString().trim();
        userPassword        = mPassword.getText().toString().trim();
        userConfirmPassword = mConfirmPassword.getText().toString().trim();


        if(TextUtils.isEmpty(userName)){
            Toast.makeText(SignUpActivity.this,"Enter Name",Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(userEmail)){
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

        // get selected radio button from radioGroup
        int selectedId = mRadioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        mRadioButton = (RadioButton) findViewById(selectedId);
        mDialog.setMessage("Creating User! Please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String accountType =  mRadioButton.getText().toString().trim();
                    String userId = mAuth.getCurrentUser().getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                    DatabaseReference currentUserTable = mDatabase;
                    if(accountType.equals("Student")){
                        currentUserTable = currentUserTable.child("Students");
                    }
                    else{
                        currentUserTable = currentUserTable.child("Organizations");
                    }

                    currentUserTable = currentUserTable.child(userId);

                    //Set the Initial Data of the User
                    currentUserTable.child("fullname").setValue(userName);
                    currentUserTable.child("account_type").setValue(accountType);
                    currentUserTable.child("email").setValue(userEmail);
                    currentUserTable.child("profile_completed").setValue("false");
                    if(accountType.equals("Organization")){
                        currentUserTable.child("profile_photo").setValue("https://firebasestorage.googleapis.com/v0/b/unistud-1594c.appspot.com/o/DefaultFiles%2Fimage%3A27?alt=media&token=432f5f55-d50e-4cfa-adc1-c27f84e60606");
                        currentUserTable.child("description").setValue("");
                        currentUserTable.child("domain").setValue("");
                        currentUserTable.child("location").setValue("");
                    }
                    else {
                        currentUserTable.child("profile_photo").setValue("https://firebasestorage.googleapis.com/v0/b/unistud-1594c.appspot.com/o/DefaultFiles%2Fimage%3A27?alt=media&token=432f5f55-d50e-4cfa-adc1-c27f84e60606");
                        currentUserTable.child("birthday").setValue("");
                        currentUserTable.child("gender").setValue("");
                        currentUserTable.child("university_country").setValue("");
                        currentUserTable.child("university_city").setValue("");
                        currentUserTable.child("university_name").setValue("");
                        currentUserTable.child("mobile_phone").setValue("");
                    }
                    mDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this, "Error on creating user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
