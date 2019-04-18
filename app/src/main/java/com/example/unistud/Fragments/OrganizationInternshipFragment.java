package com.example.unistud.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unistud.Activities.OrganizationAddInternship;
import com.example.unistud.Activities.OrganizationEventProfile;
import com.example.unistud.Activities.OrganizationInternshipProfile;
import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.EventViewHolder;
import com.example.unistud.Helpers.Internship;
import com.example.unistud.Helpers.InternshipViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OrganizationInternshipFragment extends Fragment {

    private RecyclerView mInternshipList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Internship> options;
    private FirebaseRecyclerAdapter<Internship,InternshipViewHolder> adapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String userId;

    Query mQuery;

    public static final String INTERNSHIP_ID = "InternshipId";
    public static final String ORGANIZATION_ID = "OrganizationID";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_organization_internships, container, false);

        //Initialize Attributes
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        //RecyclerView
        mInternshipList = myFragmentView.findViewById(R.id.organization_internship_lists);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships");
        mInternshipList.setHasFixedSize(true);
        mQuery = databaseReference.orderByChild("internshipCreatorId").equalTo(userId);

        options = new FirebaseRecyclerOptions.Builder<Internship>()
                .setQuery(mQuery, Internship.class).build();

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
                        Intent intent = new Intent(getApplicationContext(), OrganizationInternshipProfile.class);
                        intent.putExtra(INTERNSHIP_ID, internshipId);
                        intent.putExtra(ORGANIZATION_ID,organizationId);
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
        //Return The View
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
