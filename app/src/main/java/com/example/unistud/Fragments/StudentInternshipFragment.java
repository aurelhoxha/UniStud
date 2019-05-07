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

import com.example.unistud.Activities.StudentInternshipProfile;
import com.example.unistud.Helpers.Internship;
import com.example.unistud.Helpers.InternshipViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentInternshipFragment extends Fragment {

    private RecyclerView mInternshipList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Internship> options;
    private FirebaseRecyclerAdapter<Internship, InternshipViewHolder> adapter;

    public static final String INTERNSHIP_ID = "InternshipId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.activity_student_internships, container, false);

        //RecyclerView
        mInternshipList = myFragmentView.findViewById(R.id.student_internship_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships");
        mInternshipList.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Internship>()
                .setQuery(databaseReference, Internship.class).build();

        adapter = new FirebaseRecyclerAdapter<Internship, InternshipViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull InternshipViewHolder holder, int position, @NonNull Internship model) {
                holder.setInternshipTitle(model.getInternshipTitle());
                holder.setInternshipDate(model.getInternshipDate());
                holder.setInternshipImage(getActivity(),model.getInternshipImage());
                holder.setmInternshipId(model.getInternshipId());
                holder.setmOrganizationId(model.getInternshipCreatorId());
                final String internshipId = holder.getmInternshipId();
                final String organizationId = holder.getmOrganizationId();
                holder.getmViewInternship().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), StudentInternshipProfile.class);
                        intent.putExtra(INTERNSHIP_ID, internshipId);
                        startActivityForResult(intent,1);
                    }
                });
            }

            @NonNull
            @Override
            public InternshipViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_internship_list,viewGroup,false);
                return new InternshipViewHolder(view);
            }
        };

        mInternshipList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.startListening();
        mInternshipList.setAdapter(adapter);
        return myFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
    }
}