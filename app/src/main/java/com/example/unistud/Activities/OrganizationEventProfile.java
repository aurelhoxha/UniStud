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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static com.example.unistud.Fragments.OrganizationEventFragment.EVENT_ID;
import static com.example.unistud.Fragments.OrganizationInternshipFragment.INTERNSHIP_ID;

public class OrganizationEventProfile extends AppCompatActivity {

    //Database Variables
    private DatabaseReference databaseReference;
    private String mEventId;
    private String eventTitle;
    private String eventLocation;
    private String eventDesc;
    private String eventDate;
    private String eventImage;

    //UI Variables
    private ImageView mEventImage;
    private TextView mEventTitle;
    private TextView mEventLocation;
    private EditText mEventDesc;
    private EditText mEventLocationEditText;
    private TextView mEventDateTextView;
    private ImageView mEventSetDate;
    private Button mEventEditButton;
    private Button mEventDeleteButton;
    private ProgressDialog mProgress;

    //Others
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_event_profile);

        //Initialize the UI Components
        mEventImage = findViewById(R.id.organization_event_profile_image);
        mEventTitle = findViewById(R.id.organization_event_profile_title);
        mEventLocation = findViewById(R.id.organization_event_profile_location);
        mEventDesc = findViewById(R.id.organization_event_profile_desc);
        mEventLocationEditText = findViewById(R.id.organization_event_profile_loc);
        mEventDateTextView = findViewById(R.id.event_pick_date);
        mEventSetDate = findViewById(R.id.eventDateButton);
        mEventEditButton = findViewById(R.id.organization_event_profile_done);
        mEventDeleteButton = findViewById(R.id.organization_event_profile_delete);
        mProgress    = new ProgressDialog(this);

        //Get The Internship ID
        Intent intent = getIntent();
        mEventId = intent.getStringExtra(EVENT_ID);

        //Initialize the Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(mEventId);
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
                mEventLocationEditText.setText(eventLocation);
                mEventDateTextView.setText(eventDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Set The Listener For Buttons
        mEventSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCal = Calendar.getInstance();
                int year = myCal.get(Calendar.YEAR);
                int month = myCal.get(Calendar.MONTH);
                int day = myCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDialog = new DatePickerDialog(
                        OrganizationEventProfile.this,
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
                mEventDateTextView.setText(date);
            }
        };

        mEventDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Deleting the Event ... ");
                mProgress.show();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
                Query mDeletionQuery = databaseReference.orderByChild("eventId").equalTo(mEventId);
                mDeletionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot internshipSnapshot: dataSnapshot.getChildren()) {
                            internshipSnapshot.getRef().removeValue();
                            mProgress.dismiss();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mEventEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEventDesc.getText().toString().isEmpty() || mEventLocationEditText.getText().toString().isEmpty())
                    Toast.makeText(OrganizationEventProfile.this,"Fill All Fields! ", Toast.LENGTH_SHORT).show();
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(mEventId);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                dataSnapshot.getRef().child("eventDesc").setValue(mEventDesc.getText().toString());
                                dataSnapshot.getRef().child("eventPlace").setValue(mEventLocationEditText.getText().toString());
                                dataSnapshot.getRef().child("eventDate").setValue(mEventDateTextView.getText().toString());

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
