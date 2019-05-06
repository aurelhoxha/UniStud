package com.example.unistud.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.unistud.Helpers.Student;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class StudentRegister2 extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner student_country;
    private Spinner student_university;
    private Button submit;
    private String userId;
    private TextView title;
    private ImageView photo;



    private String image;
    private String country;
    private String university;

    private Query myQuery;

    //Firebase Connection Instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageReference;
    private ArrayList<String> universities = new ArrayList<String>();

    //Other Variables
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register2);

        //Initializing the variables

        student_country = (Spinner) findViewById(R.id.student_country_education);
        student_country.setOnItemSelectedListener(this);
        student_university = (Spinner) findViewById(R.id.student_university);
        submit = (Button) findViewById(R.id.submit);
        title = (TextView) findViewById(R.id.register_title2);
        photo = (ImageView) findViewById(R.id.user_profile_image);

        //Databse

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Students");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Universities");
        userId = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student mStudent = dataSnapshot.child(userId).getValue(Student.class);
                String studentFullName = mStudent.getFullname();
                title.setText("Almost done  " + studentFullName);

                image = (String) dataSnapshot.child("profile_photo").getValue();
                Picasso.get().load(image).into(photo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDialog = new ProgressDialog(this);
        mStorageReference = FirebaseStorage.getInstance().getReference("DefaultFiles");

        //Set the Listeners
        submit.setOnClickListener(this);


        //For teh country drop down
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);
        // set the view for the Drop down list
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the ArrayAdapter to the spinner
        student_country.setAdapter(dataAdapter);
        student_country.setSelection(37);

        universities.add("Aurel");
        universities.add("selda");
        ArrayAdapter<String> uniAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, universities);
        // set the view for the Drop down list
        uniAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the ArrayAdapter to the spinner
        student_university.setAdapter(uniAdapter);

    }


    @Override
    public void onClick(View v) {
        if (v == submit) {
            Submit();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        country = parent.getItemAtPosition(position).toString();
        myQuery = mDatabaseRef.orderByChild("universityName").equalTo(country);
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                universities.clear();
                for(DataSnapshot taskSnapshot : dataSnapshot.getChildren()){
                    University mUni = taskSnapshot.getValue(University.class);
                    String uni = (String) mUni.getUniversityCountry();
                    universities.add(uni);
                }

                for (int i = 0; i < universities.size(); i++){
                    Log.d("Checking: ", ""+universities.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onNothingSelected(AdapterView<?> arg0){

    }


    private void Submit() {


      //  final String country, university;

        university = student_university.getSelectedItem().toString();

        final DatabaseReference currentUserTable = mDatabase.child(userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    currentUserTable.child("university_name").setValue(university);
                    currentUserTable.child("university_country").setValue(country);
                    currentUserTable.child("profile_completed").setValue("true");

                    Intent intent = new Intent(StudentRegister2.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}