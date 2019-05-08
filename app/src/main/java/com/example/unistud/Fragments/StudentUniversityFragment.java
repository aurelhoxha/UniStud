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

import com.example.unistud.Activities.StudentEventProfile;
import com.example.unistud.Activities.StudentUserList;
import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.EventViewHolder;
import com.example.unistud.Helpers.UniversitiesViewHolder;
import com.example.unistud.Helpers.University;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentUniversityFragment extends Fragment {

    private RecyclerView mUniversityList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<University> options;
    private FirebaseRecyclerAdapter<University, UniversitiesViewHolder> adapter;

    public static final String UNIVERSITY_NAME = "UniversityId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.activity_student_universities, container, false);

        //RecyclerView
        mUniversityList = myFragmentView.findViewById(R.id.student_university_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Universities");
        mUniversityList.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<University>()
                .setQuery(databaseReference, University.class).build();

        adapter = new FirebaseRecyclerAdapter<University, UniversitiesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UniversitiesViewHolder holder, int position, @NonNull University model) {
                holder.setUniversityTitle(model.getUniversityName());
                holder.setmUniversityId(model.getUniversityID());
                holder.setmUniversityName(model.getUniversityName());
                final String universityName = holder.getmUniversityName();
                holder.getmViewUniversity().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), StudentUserList.class);
                        intent.putExtra(UNIVERSITY_NAME, universityName);
                        startActivityForResult(intent, 1);
                    }
                });
            }

            @NonNull
            @Override
            public UniversitiesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_university_list, viewGroup, false);
                return new UniversitiesViewHolder(view);
            }
        };

        mUniversityList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.startListening();
        mUniversityList.setAdapter(adapter);
        //Return The View
        return myFragmentView;
    }
}
