package com.example.unistud.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.unistud.Fragments.StudentInternshipFragment.INTERNSHIP_ID;

public class StudentInternshipProfile extends AppCompatActivity {

    //Database Variables
    private DatabaseReference databaseReference;
    private String internshipId;
    private String internshipCreatorId;
    private String internshipTitle;
    private String internshipLocation;
    private String internshipDesc;
    private String internshipDate;
    private String internshipImage;
    private String userId;

    //UI Variables
    private ImageView mInternshipImage;
    private TextView mInternshipTitle;
    private TextView mInternshipLocation;
    private TextView mInternshipDesc;
    private TextView mInternshipDate;
    private Button mInternshipSaveButton;
    private ProgressDialog mProgress;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_internship_profile);

        //Initialize the UI Components
        mInternshipImage = findViewById(R.id.student_internship_profile_image);
        mInternshipTitle = findViewById(R.id.student_internship_profile_title);
        mInternshipLocation = findViewById(R.id.student_internship_profile_location);
        mInternshipDesc = findViewById(R.id.student_internship_profile_desc);
        mInternshipDate = findViewById(R.id.internship_pick_date);
        mInternshipSaveButton = findViewById(R.id.student_internship_profile_save);
        mProgress    = new ProgressDialog(this);

        //Get The Internship ID
        Intent intent = getIntent();
        internshipId = intent.getStringExtra(INTERNSHIP_ID);

        //Initialize the Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships").child(internshipId);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                internshipTitle = (String) dataSnapshot.child("internshipTitle").getValue();
                internshipCreatorId = (String) dataSnapshot.child("internshipCreatorId").getValue();
                internshipDesc = (String) dataSnapshot.child("internshipDesc").getValue();
                internshipDate = (String) dataSnapshot.child("internshipDate").getValue();
                internshipImage = (String) dataSnapshot.child("internshipImage").getValue();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Organizations").child(internshipCreatorId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        internshipLocation = (String) dataSnapshot.child("location").getValue();

                        //Set The Initial Value of the UI Components
                        Picasso.get().load(internshipImage).into(mInternshipImage);
                        mInternshipTitle.setText(internshipTitle);
                        mInternshipLocation.setText(internshipLocation);
                        mInternshipDesc.setText(internshipDesc);
                        mInternshipDate.setText(internshipDate);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mInternshipSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Saving the Internship ... ");
                mProgress.show();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_internships").child(internshipId).child("internship_name");
                databaseReference.setValue(internshipTitle);
                mProgress.dismiss();
            }
        });

    }
}
