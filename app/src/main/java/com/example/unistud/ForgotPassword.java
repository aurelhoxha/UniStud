package com.example.unistud;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText inputEmail;
    private Button btnResetPass;
    private Button btnBack;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Set the Variables
        inputEmail   = (EditText)findViewById(R.id.forgot_email_address);
        btnResetPass = (Button)findViewById(R.id.reset_password_button);
        btnBack      = (Button)findViewById(R.id.back_reset_password_button);

        //Set the Listener
        btnResetPass.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //Set the Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if( v== btnBack){
            startActivity(new Intent(ForgotPassword.this,MainActivity.class));
            finish();
        }
        else if( v == btnResetPass){
            resetPassword(inputEmail.getText().toString());
        }
    }

    private void resetPassword(final String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Please check you email: "+email,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Failed to send the email.",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}
