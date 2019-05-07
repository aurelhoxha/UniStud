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

import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.EventViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//
//
public class ProfileEvents extends AppCompatActivity {
//
//    private RecyclerView mEventList;
//    private DatabaseReference databaseReference;
//    private FirebaseRecyclerOptions<ProfileEvent> options;
//    private FirebaseRecyclerAdapter<ProfileEvent, EventViewHolder> adapter;
//    public static final String EVENT_ID = "EventId";
//    private FirebaseAuth mFirebaseAuth;
//    private FirebaseUser mFirebaseUser;
//    private String userId;
//    private DatabaseReference databaseReferenceUser;
//    private Event mEvent;
//
//    //Variables to save to display event
//    private String evTitle;
//    private String evDate;
//    private String evDesc;
//    private String evPhoto;
//    private String eventID;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_events);
//
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        userId = mFirebaseUser.getUid();
//
//        //RecyclerView
//        mEventList = findViewById(R.id.profile_event_list);
//        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_events");
//        mEventList.setHasFixedSize(true);
//
//        options = new FirebaseRecyclerOptions.Builder<ProfileEvent>()
//                .setQuery(databaseReference, ProfileEvent.class).build();
//
//        adapter = new FirebaseRecyclerAdapter<ProfileEvent, EventViewHolder> (options) {
//            @Override
//            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull ProfileEvent model) {
//                holder.setEventTitle("Test");
//                holder.setEventDate(model.getEventName());
//                //holder.setEventImage(getApplicationContext(),model.getEventImage());
//                //holder.setmEventId(model.getEventId());
//                final String eventId = holder.getmEventId();
//                holder.getmViewEvent().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(), StudentEventProfile.class);
//                        intent.putExtra(EVENT_ID, eventId);
//                        startActivityForResult(intent,1);
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_event_list,viewGroup,false);
//                return new EventViewHolder(view);
//            }
//        };
//
//        mEventList.setLayoutManager(new LinearLayoutManager(this));
//        adapter.startListening();
//        mEventList.setAdapter(adapter);
//        //Return The View
//
//
//    }
//}
    }
}