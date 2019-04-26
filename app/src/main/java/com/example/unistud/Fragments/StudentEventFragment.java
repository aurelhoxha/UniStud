package com.example.unistud.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unistud.Activities.OrganizationEventProfile;
import com.example.unistud.Activities.StudentEventProfile;
import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.EventViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentEventFragment extends Fragment {

    private RecyclerView mEventList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Event> options;
    private FirebaseRecyclerAdapter<Event,EventViewHolder> adapter;

    public static final String EVENT_ID = "EventId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_student_events, container, false);

        //RecyclerView
        mEventList = myFragmentView.findViewById(R.id.student_event_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventList.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(databaseReference, Event.class).build();

        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {
                holder.setEventTitle(model.getEventTitle());
                holder.setEventDate(model.getEventDate());
                holder.setEventImage(getActivity(),model.getEventImage());
                holder.setmEventId(model.getEventId());
                final String eventId = holder.getmEventId();
                holder.getmViewEvent().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), StudentEventProfile.class);
                        intent.putExtra(EVENT_ID, eventId);
                        startActivityForResult(intent,1);
                    }
                });
            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_event_list,viewGroup,false);
                return new EventViewHolder(view);
            }
        };

        mEventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.startListening();
        mEventList.setAdapter(adapter);
        //Return The View
        return myFragmentView;
    }
}