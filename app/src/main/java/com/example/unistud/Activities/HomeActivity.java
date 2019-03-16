package com.example.unistud.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLogOutButton;

    //Authentication to Firebase Instances
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLogOutButton = (Button) findViewById(R.id.log_out_button);
        mAuth         = FirebaseAuth.getInstance();
        mLogOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mLogOutButton) {
           mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
