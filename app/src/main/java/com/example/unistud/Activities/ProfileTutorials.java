package com.example.unistud.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.unistud.Helpers.Tutorial;
import com.example.unistud.Helpers.TutorialViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileTutorials extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userId;

    private RecyclerView mTutorialList;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUser;
    private FirebaseRecyclerOptions<Tutorial> options;
    private FirebaseRecyclerAdapter<Tutorial, TutorialViewHolder> adapter;

    public static final String TUTORIAL_ID = "TutorialId";
    public static final String TUTORIAL_STATUS = "TutorialStatus";
    public static final String TUTORIAL_LINK = "TutorialLink";

    private Tutorial mTutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tutorials);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();


        //RecyclerView
        mTutorialList = findViewById(R.id.profile_tutorial_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_tutorials");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Tutorials");
        mTutorialList.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Tutorial>().setQuery(databaseReference, Tutorial.class).build();

        adapter = new FirebaseRecyclerAdapter<Tutorial, TutorialViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TutorialViewHolder holder, int position, @NonNull final Tutorial model) {
                String TuTid = model.getTutorialId();
                databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Tutorials").child(TuTid);
                databaseReferenceUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mTutorial = dataSnapshot.child(userId).getValue(Tutorial.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.setTutorialTitle(mTutorial.getTutorialTitle());
                holder.setTutorialDate(mTutorial.getTutorialDate());
                //holder.setTutorialURL(mTutorial.getTutorialURL());
                holder.setmTutorialId(mTutorial.getTutorialId());
                holder.setTutorialDesc(mTutorial.getTutorialDesc());
                final String tutorialID = mTutorial.getTutorialId();
                final String tutorialStatus = mTutorial.getTutorialStatus();
                final String tutorialCreator = mTutorial.getTutorialCreatorId();
                final String tutorialURL = mTutorial.getTutorialURL();

                holder.getmViewTutorial().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//
                    //If the current user is the owner and status is 0 go to record it
                    if(mTutorial.getTutorialStatus().equals("added") && userId.equals(tutorialCreator)){
                        //Record it for first time
                        Intent intent = new Intent(getApplicationContext(), TutorialLiveStream.class);
                        intent.putExtra(TUTORIAL_ID, tutorialID);
                        startActivityForResult(intent,1);
                    }
                    else if(mTutorial.getTutorialStatus().equals("added") && !userId.equals(tutorialCreator)){
                        Toast.makeText(getApplicationContext(),"Sorry! This tutorial is not available yet!", Toast.LENGTH_LONG).show();
                    }

                    //Status is 1 or 2
                    else if(mTutorial.getTutorialStatus().equals("live")){
                        Intent intent = new Intent(getApplicationContext(), PlayVideo.class);
                        intent.putExtra(TUTORIAL_STATUS, tutorialStatus);
                        startActivityForResult(intent,1);
                    }
                    else if(mTutorial.getTutorialStatus().equals("saved")){
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

        mTutorialList.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
        mTutorialList.setAdapter(adapter);
    }
}
