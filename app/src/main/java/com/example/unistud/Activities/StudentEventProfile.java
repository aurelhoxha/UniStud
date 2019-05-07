package com.example.unistud.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.unistud.Fragments.OrganizationEventFragment.EVENT_ID;

public class StudentEventProfile extends AppCompatActivity {

    //Database Variables
    private DatabaseReference databaseReference;
    private String mEventId;
    private String eventTitle;
    private String eventLocation;
    private String eventDesc;
    private String eventDate;
    private String eventImage;
    private String userId;

    //UI Variables
    private ImageView mEventImage;
    private TextView mEventTitle;
    private TextView mEventLocation;
    private TextView mEventDesc;
    private TextView mEventLocationTextView;
    private TextView mEventDateTextView;
    private Button mEventSaveButton;
    private ProgressDialog mProgress;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_event_profile);

        //Initialize the UI Components
        mEventImage = findViewById(R.id.student_event_profile_image);
        mEventTitle = findViewById(R.id.student_event_profile_title);
        mEventLocation = findViewById(R.id.student_event_profile_location);
        mEventDesc = findViewById(R.id.student_event_profile_desc);
        mEventLocationTextView = findViewById(R.id.student_event_profile_loc);
        mEventDateTextView = findViewById(R.id.student_event_pick_date);
        mEventSaveButton = findViewById(R.id.student_event_profile_save);
        mProgress    = new ProgressDialog(this);

        //Get The Internship ID
        Intent intent = getIntent();
        mEventId = intent.getStringExtra(EVENT_ID);

        //Initialize the Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(mEventId);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventTitle = (String) dataSnapshot.child("eventTitle").getValue();
                eventLocation = (String) dataSnapshot.child("eventPlace").getValue();
                eventDesc = (String) dataSnapshot.child("eventDesc").getValue();
                eventDate = (String) dataSnapshot.child("eventDate").getValue();
                eventImage = (String) dataSnapshot.child("eventImage").getValue();
                Picasso.get().load(eventImage).into(mEventImage);
                mEventTitle.setText(eventTitle);
                mEventLocation.setText(eventLocation);
                mEventDesc.setText(eventDesc);
                mEventLocationTextView.setText(eventLocation);
                mEventDateTextView.setText(eventDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mEventSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Saving the Event ... ");
                mProgress.show();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_events").child(mEventId).child("event_name");
                databaseReference.setValue(eventTitle);


                mProgress.dismiss();
            }
        });
    }
}
