package com.example.unistud.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.unistud.Activities.StudentEventProfile;
import com.example.unistud.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentTutorialFragment extends Fragment {

    private Button startLivestreamButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_tutorials, container, false);

        startLivestreamButton = myFragmentView.findViewById(R.id.student_event_profile_save);
        startLivestreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentEventProfile.class);
                startActivityForResult(intent,1);
            }
        });
        return myFragmentView;
    }
}

