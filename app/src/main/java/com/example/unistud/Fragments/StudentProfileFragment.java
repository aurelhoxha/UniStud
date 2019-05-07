package com.example.unistud.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Activities.PlayVideo;
import com.example.unistud.Activities.ProfileEvents;
import com.example.unistud.Activities.ProfileInternships;
import com.example.unistud.Activities.ProfileItems;
import com.example.unistud.Activities.ProfileTutorials;
import com.example.unistud.Activities.TutorialLiveStream;
import com.example.unistud.Helpers.Tutorial;
import com.example.unistud.Helpers.TutorialViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentProfileFragment extends Fragment {

    private DatabaseReference databaseReference;

    public static final String TUTORIAL_ID = "TutorialId";
    public static final String TUTORIAL_STATUS = "TutorialStatus";
    public static final String TUTORIAL_LINK = "TutorialLink";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userId;

    private TextView tutorialsnr;
    private TextView tutorialstxt;
    private TextView eventsnr;
    private TextView eventstxt;
    private TextView internshipsnr;
    private TextView internshipstxt;
    private TextView itemsnr;
    private TextView itemstxt;

    private String tutorialCount = "0";
    private String eventCount = "0";
    private String internshipCount = "0";
    private String itemCount = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_profile, container, false);
        tutorialsnr = (TextView)myFragmentView.findViewById(R.id.tutorial_nr);
        tutorialstxt = (TextView)myFragmentView.findViewById(R.id.tutorial_txt);
        eventsnr = (TextView)myFragmentView.findViewById(R.id.event_nr);
        eventstxt = (TextView)myFragmentView.findViewById(R.id.event_txt);
        internshipsnr = (TextView)myFragmentView.findViewById(R.id.internship_nr);
        internshipstxt = (TextView)myFragmentView.findViewById(R.id.internship_txt);
        itemsnr = (TextView)myFragmentView.findViewById(R.id.item_nr);
        itemstxt = (TextView)myFragmentView.findViewById(R.id.item_txt);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child("saved_events");
        //tutorialCount = databaseReference.


        databaseReference = FirebaseDatabase.getInstance().getReference("Tutorials");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tutorialsnr.setText((int)dataSnapshot.getChildrenCount() + "");
                }
                else {
                    tutorialsnr.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tutorialsnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Tuts1", Toast.LENGTH_LONG).show();

                //Intent intent = new Intent(getApplicationContext(), ProfileTutorials.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);

            }
        });

        tutorialstxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Tuts2", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileTutorials.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);

            }
        });

        eventsnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Eve1", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileEvents.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);
            }
        });

        eventstxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Eve2", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileEvents.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);
            }
        });

        internshipsnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Int1", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileInternships.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);
            }
        });

        internshipstxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Int2", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileInternships.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);
            }
        });

        itemsnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Ite1", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileItems.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                // startActivityForResult(intent,1);
            }
        });

        itemstxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),"Ite2", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(), ProfileItems.class);
                //intent.putExtra(TUTORIAL_ID, tutorialID);
                //startActivityForResult(intent,1);
            }
        });

        //Return The View
        return myFragmentView;
    }
}



