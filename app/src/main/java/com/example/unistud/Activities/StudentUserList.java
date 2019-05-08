package com.example.unistud.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.EventViewHolder;
import com.example.unistud.Helpers.Student;
import com.example.unistud.Helpers.StudentViewHolder;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.unistud.Fragments.StudentUniversityFragment.UNIVERSITY_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentUserList extends AppCompatActivity {

    private RecyclerView mUserList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Student> options;
    private FirebaseRecyclerAdapter<Student, StudentViewHolder> adapter;
    String mUniversityName;
    Query mQuery;

    public static final String STUDENT_ID = "StudentId";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_user);

        //Get The Internship ID
        Intent intent = getIntent();
        mUniversityName = intent.getStringExtra(UNIVERSITY_NAME);

        //RecyclerView
        mUserList = findViewById(R.id.student_user_list);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students");
        mQuery = databaseReference.orderByChild("university_name").equalTo(mUniversityName);
        mUserList.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Student>()
                .setQuery(mQuery, Student.class).build();

        adapter = new FirebaseRecyclerAdapter<Student, StudentViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull Student model) {
                holder.setUserTitle(model.getFullname());
                holder.setUserImage(model.getProfile_photo());
                final String userId = model.getAccountId();
                holder.getmSendMessage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StudentUserList.this, MessageActivity.class);
                        intent.putExtra(STUDENT_ID, userId);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_user_list,viewGroup,false);
                return new StudentViewHolder(view);
            }
        };

        mUserList.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
        mUserList.setAdapter(adapter);
    }
}
