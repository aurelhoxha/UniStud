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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Helpers.Internship;
import com.example.unistud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static com.example.unistud.Fragments.OrganizationInternshipFragment.INTERNSHIP_ID;
import static com.example.unistud.Fragments.OrganizationInternshipFragment.ORGANIZATION_ID;

public class OrganizationInternshipProfile extends AppCompatActivity {

    //Database Variables
    private DatabaseReference databaseReference;
    private String organizationId;
    private String mInternshipId;
    private String internshipLocation;
    private String internshipTitle;
    private String internshipDesc;
    private String internshipDate;
    private String internshipImage;

    //UI Variables
    private ImageView mInternshipImage;
    private TextView mInternshipTitle;
    private TextView mInternshipLocation;
    private EditText mInternshipDesc;
    private TextView mInternshipDateTextView;
    private ImageView mInternshipSetDate;
    private Button mInternshipEditButton;
    private Button mInternshipDeleteButton;
    private ProgressDialog mProgress;

    //Others
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_internship_profile);

        //Initialize the UI Components
        mInternshipImage = findViewById(R.id.organization_internship_profile_image);
        mInternshipTitle = findViewById(R.id.organization_internship_profile_title);
        mInternshipLocation = findViewById(R.id.organization_internship_profile_location);
        mInternshipDesc = findViewById(R.id.organization_internship_profile_desc);
        mInternshipDateTextView = findViewById(R.id.internship_pick_date);
        mInternshipSetDate = findViewById(R.id.internshipDateButton);
        mInternshipEditButton = findViewById(R.id.organization_internship_profile_done);
        mInternshipDeleteButton = findViewById(R.id.organization_internship_profile_delete);
        mProgress    = new ProgressDialog(this);

        //Get The Internship ID
        Intent intent = getIntent();
        mInternshipId = intent.getStringExtra(INTERNSHIP_ID);
        organizationId = intent.getStringExtra(ORGANIZATION_ID);

        //Initialize the Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships").child(mInternshipId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Initialize Useful Variables
                internshipTitle = (String) dataSnapshot.child("internshipTitle").getValue();
                internshipDesc = (String) dataSnapshot.child("internshipDesc").getValue();
                internshipDate = (String) dataSnapshot.child("internshipDate").getValue();
                internshipImage = (String) dataSnapshot.child("internshipImage").getValue();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Organizations").child(organizationId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        internshipLocation = (String) dataSnapshot.child("location").getValue();
                        //Set The Initial Value of the UI Components
                        mInternshipTitle.setText(internshipTitle);
                        mInternshipLocation.setText(internshipLocation);
                        mInternshipDesc.setText(internshipDesc);
                        mInternshipDateTextView.setText(internshipDate);
                        Picasso.get().load(internshipImage).into(mInternshipImage);
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




        //Set The Listener For Buttons
        mInternshipSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCal = Calendar.getInstance();
                int year = myCal.get(Calendar.YEAR);
                int month = myCal.get(Calendar.MONTH);
                int day = myCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDialog = new DatePickerDialog(
                        OrganizationInternshipProfile.this,
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
                mInternshipDateTextView.setText(date);
            }
        };

        mInternshipEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInternshipDesc.getText().toString().isEmpty())
                    Toast.makeText(OrganizationInternshipProfile.this,"Fill Internship Description! ", Toast.LENGTH_SHORT).show();
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships").child(mInternshipId);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                dataSnapshot.getRef().child("internshipDesc").setValue(mInternshipDesc.getText().toString());
                                dataSnapshot.getRef().child("internshipDate").setValue(mInternshipDateTextView.getText().toString());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        mInternshipDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Deleting the Internship ... ");
                mProgress.show();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships");
                Query mDeletionQuery = databaseReference.orderByChild("internshipId").equalTo(mInternshipId);
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
    }
}