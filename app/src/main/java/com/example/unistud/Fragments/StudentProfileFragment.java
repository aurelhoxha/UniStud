package com.example.unistud.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Activities.EditUserProfile;
import com.example.unistud.Activities.MainActivity;
import com.example.unistud.Activities.PlayVideo;
import com.example.unistud.Activities.ProfileEvents;
import com.example.unistud.Activities.ProfileInternships;
import com.example.unistud.Activities.ProfileItems;
import com.example.unistud.Activities.ProfileTutorials;
import com.example.unistud.Activities.TutorialLiveStream;
import com.example.unistud.Helpers.Student;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentProfileFragment extends Fragment {

    private DatabaseReference db;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceTutorials;
    private DatabaseReference databaseReferenceEvents;
    private DatabaseReference databaseReferenceInternships;
    private DatabaseReference databaseReferenceItems;

    private Button deleteProfile;
    private Button editProfile;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userId;

    private TextView userName;

    private ImageView profileImg;

    private TextView tutorialsnr;
    private TextView tutorialstxt;
    private TextView eventsnr;
    private TextView eventstxt;
    private TextView internshipsnr;
    private TextView internshipstxt;
    private TextView itemsnr;
    private TextView itemstxt;

    private int tutorialCount = 0;
    private int eventCount = 0;
    private int internshipCount = 0;
    private int itemCount = 0;
    String img;

    private final String USER_ID = "userId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_profile, container, false);
       // tutorialsnr = (TextView)myFragmentView.findViewById(R.id.tutorial_nr);
        //tutorialstxt = (TextView)myFragmentView.findViewById(R.id.tutorial_txt);
        eventsnr = (TextView)myFragmentView.findViewById(R.id.event_nr);
        eventstxt = (TextView)myFragmentView.findViewById(R.id.event_txt);
        internshipsnr = (TextView)myFragmentView.findViewById(R.id.internship_nr);
        internshipstxt = (TextView)myFragmentView.findViewById(R.id.internship_txt);
        itemsnr = (TextView)myFragmentView.findViewById(R.id.item_nr);
        itemstxt = (TextView)myFragmentView.findViewById(R.id.item_txt);
        deleteProfile = myFragmentView.findViewById(R.id.delete_account);
        editProfile = myFragmentView.findViewById(R.id.edit_profile);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditUserProfile.class);
                startActivityForResult(intent,1);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Toast.makeText(getApplicationContext(), "We are sorry to see you go!", Toast.LENGTH_LONG).show();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                databaseReference.removeValue();
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 1);


            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Thank you for staying with us!", Toast.LENGTH_LONG).show();
                // Do nothing
                dialog.dismiss();
            }
        });

       final AlertDialog alert = builder.create();

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();

            }
        });

        profileImg = (ImageView)myFragmentView.findViewById(R.id.profilee_image);

        userName = (TextView)myFragmentView.findViewById(R.id.user_name);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userName.setText((String) dataSnapshot.child("fullname").getValue());
                    img = (String) dataSnapshot.child("profile_photo").getValue();

                    Log.d("mmmm", img + "");
                    //Picasso.get().load(img).into(profileImg);
                }
                else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        databaseReferenceTutorials = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_tutorials");
//        databaseReferenceTutorials.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    tutorialCount = (int)dataSnapshot.getChildrenCount();
//                    tutorialsnr.setText((int)dataSnapshot.getChildrenCount() + "");
//                }
//                else {
//                    tutorialsnr.setText("0");
//                    tutorialCount = 0;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        databaseReferenceEvents = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_events");
        databaseReferenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    eventCount = (int)dataSnapshot.getChildrenCount();
                    eventsnr.setText((int)dataSnapshot.getChildrenCount() + "");
                }
                else {
                    eventsnr.setText("0");
                    eventCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferenceInternships = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_internships");
        databaseReferenceInternships.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    internshipCount = (int)dataSnapshot.getChildrenCount();
                    internshipsnr.setText((int)dataSnapshot.getChildrenCount() + "");
                }
                else {
                    internshipsnr.setText("0");
                    internshipCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferenceItems = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_items");
        databaseReferenceItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    itemCount = (int)dataSnapshot.getChildrenCount();
                    itemsnr.setText((int)dataSnapshot.getChildrenCount() + "");
                }
                else {
                    itemsnr.setText("0");
                    itemCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        tutorialsnr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if(tutorialCount != 0 ) {
//                    Intent intent = new Intent(getApplicationContext(), ProfileTutorials.class);
//                    startActivityForResult(intent,1);
//                }
//                else{
//
//                }
//
//            }
//        });
//
//        tutorialstxt.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                //Intent intent = new Intent(getApplicationContext(), ProfileTutorials.class);
//                //startActivityForResult(intent,1);
//
//            }
//        });

        eventsnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(eventCount != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileEvents.class);
                    startActivityForResult(intent, 1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please add an event",Toast.LENGTH_LONG).show();
                }
            }
        });

        eventstxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(eventCount != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileEvents.class);
                    startActivityForResult(intent, 1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please add an event",Toast.LENGTH_LONG).show();
                }
            }
        });

        internshipsnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(internshipCount != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileInternships.class);
                    startActivityForResult(intent,1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please add an internship",Toast.LENGTH_LONG).show();
                }
            }
        });

        internshipstxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(internshipCount != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileInternships.class);
                    startActivityForResult(intent,1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please add an internship",Toast.LENGTH_LONG).show();
                }
            }
        });

        itemsnr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(itemCount != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileItems.class);
                    startActivityForResult(intent, 1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please add an item",Toast.LENGTH_LONG).show();
                }
            }
        });

        itemstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(itemCount != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileItems.class);
                    startActivityForResult(intent, 1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please add an item",Toast.LENGTH_LONG).show();
                }
            }
        });

        //Return The View
        return myFragmentView;
    }
}