package com.example.unistud.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.unistud.Activities.StudentInternships;
import com.example.unistud.Activities.StudentUniversities;
import com.example.unistud.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentOpportunitiesFragment extends Fragment {

    private Button goUniversities;
    private Button goInternships;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_opportunities, container, false);

        goUniversities = (Button) myFragmentView.findViewById(R.id.university_btn);
        goInternships = (Button) myFragmentView.findViewById(R.id.internship_btn);


        goUniversities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentUniversities.class);
                startActivityForResult(intent,1);
            }
        });

        goInternships.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentInternships.class);
                startActivityForResult(intent,1);
            }
        });
        return myFragmentView;
    }
}