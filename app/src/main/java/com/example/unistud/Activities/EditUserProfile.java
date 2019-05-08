package com.example.unistud.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Fragments.StudentProfileFragment;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EditUserProfile extends AppCompatActivity {

    //Database Variables
    private DatabaseReference databaseReference;
    private String fullName;
    private String email;
    private String birthday;
    private String profilePic;
    private String userID;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    //UI Variables
    private ImageView userProfPic;
    private TextView userName;
    private EditText emailUser;
    private TextView birthdayDateView;
    private ImageView birthdaySetDate;
    private Button approve;
    private ProgressDialog mProgress;

    //Others
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userID = mFirebaseUser.getUid();

        userProfPic = findViewById(R.id.user_profile_img);
        userName = findViewById(R.id.user_name_edit);
        emailUser = findViewById(R.id.user_profile_email);

        birthdayDateView = findViewById(R.id.bd_pick_date);
        birthdaySetDate = findViewById(R.id.user_profile_birthday);
        mProgress = new ProgressDialog(this);

        approve = findViewById(R.id.approve_profile);

        //Initialize the Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName = (String) dataSnapshot.child("fullname").getValue();
                email = (String) dataSnapshot.child("email").getValue();
                birthday = (String) dataSnapshot.child("birthday").getValue();
                profilePic = (String) dataSnapshot.child("profile_photo").getValue();
                Picasso.get().load(profilePic).into(userProfPic);
                userName.setText(fullName);
                emailUser.setText(email);
                birthdayDateView.setText(birthday);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Set The Listener For Buttons
        birthdaySetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCal = Calendar.getInstance();
                int year = myCal.get(Calendar.YEAR);
                int month = myCal.get(Calendar.MONTH);
                int day = myCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDialog = new DatePickerDialog(
                        EditUserProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                birthdayDateView.setText(date);
            }
        };

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().isEmpty() || emailUser.getText().toString().isEmpty())
                    Toast.makeText(EditUserProfile.this,"Fill All Fields! ", Toast.LENGTH_SHORT).show();
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Students").child(userID);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                dataSnapshot.getRef().child("fullname").setValue(userName.getText().toString());
                                dataSnapshot.getRef().child("email").setValue(emailUser.getText().toString());
                                dataSnapshot.getRef().child("birthday").setValue(birthdayDateView.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }



}
