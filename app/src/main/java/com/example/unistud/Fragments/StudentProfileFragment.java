package com.example.unistud.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.unistud.Activities.PlayVideo;
import com.example.unistud.Activities.TutorialLiveStream;
import com.example.unistud.Helpers.Tutorial;
import com.example.unistud.Helpers.TutorialViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentProfileFragment extends Fragment {

    private RecyclerView mTutorialList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Tutorial> options;
    private FirebaseRecyclerAdapter<Tutorial, TutorialViewHolder> adapter;

    public static final String TUTORIAL_ID = "TutorialId";
    public static final String TUTORIAL_STATUS = "TutorialStatus";
    public static final String TUTORIAL_LINK = "TutorialLink";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_profile, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        //RecyclerView
        mTutorialList = myFragmentView.findViewById(R.id.student_profile_tutorial_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutorials");
        mTutorialList.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Tutorial>().setQuery(databaseReference, Tutorial.class).build();

        adapter = new FirebaseRecyclerAdapter<Tutorial, TutorialViewHolder> (options) {

            @Override
            protected void onBindViewHolder(@NonNull TutorialViewHolder holder, int position, @NonNull final Tutorial model) {
                holder.setTutorialTitle(model.getTutorialTitle());
                holder.setTutorialDate(model.getTutorialDate());
                //holder.setTutorialURL(model.getTutorialURL());
                holder.setmTutorialId(model.getTutorialId());
                holder.setTutorialDesc(model.getTutorialDesc());
                final String tutorialID = model.getTutorialId();
                final String tutorialStatus = model.getTutorialStatus();
                final String tutorialCreator = model.getTutorialCreatorId();
                final String tutorialURL = model.getTutorialURL();

                holder.getmViewTutorial().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//
                        //If the current user is the owner and status is 0 go to record it
                        if(model.getTutorialStatus().equals("added") && userId.equals(tutorialCreator)){
                            //Record it for first time
                            Intent intent = new Intent(getApplicationContext(), TutorialLiveStream.class);
                            intent.putExtra(TUTORIAL_ID, tutorialID);
                            startActivityForResult(intent,1);
                        }
                        else if(model.getTutorialStatus().equals("added") && !userId.equals(tutorialCreator)){
                            Toast.makeText(getApplicationContext(),"Sorry! This tutorial is not available yet!", Toast.LENGTH_LONG).show();
                        }

                        //Status is 1 or 2
                        else if(model.getTutorialStatus().equals("live")){
                            Intent intent = new Intent(getApplicationContext(), PlayVideo.class);
                            intent.putExtra(TUTORIAL_STATUS, tutorialStatus);
                            startActivityForResult(intent,1);
                        }
                        else if(model.getTutorialStatus().equals("saved")){
                            if(!tutorialURL.equals("")) {
                                Intent intent = new Intent(getApplicationContext(), PlayVideo.class);
                                intent.putExtra(TUTORIAL_STATUS, tutorialStatus);
                                startActivityForResult(intent, 1);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Sorry! We are processing the video.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @NonNull
            @Override
            public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_tutorial_list,viewGroup,false);
                return new TutorialViewHolder(view);
            }
        };

        mTutorialList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.startListening();
        mTutorialList.setAdapter(adapter);
        //Return The View
        return myFragmentView;
    }





}