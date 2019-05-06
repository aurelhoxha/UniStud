package com.example.unistud.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.unistud.Helpers.University;
import com.example.unistud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class AddUniversity extends AppCompatActivity implements View.OnClickListener {

    private Spinner universityCountry;
    private EditText universityName;
    private Button universitySubmitButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_university);
        universityCountry = (Spinner) findViewById(R.id.university_country_spinner);
        universityName = (EditText)findViewById(R.id.university_name);
        universitySubmitButton = (Button)findViewById(R.id.university_submit_button);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Universities");
        universitySubmitButton.setOnClickListener(this);

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);
        // set the view for the Drop down list
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the ArrayAdapter to the spinner
        universityCountry.setAdapter(dataAdapter);
        universityCountry.setSelection(37);

    }

    @Override
    public void onClick(View v) {
        if (v == universitySubmitButton) {
            submitUniversity();
        }
    }


    private void submitUniversity() {


        final String country, university;

        country = universityCountry.getSelectedItem().toString();
        university = universityName.getText().toString().trim();
        String key = "";

        University mUniversity = new University(key, country, university);
        mDatabase.push().setValue(mUniversity, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                String uniqueKey = databaseReference.getKey();
                mDatabase.child(uniqueKey).child("universityId").setValue(uniqueKey);
            }
        });
    }
}
