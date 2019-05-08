package com.example.unistud.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.unistud.Helpers.Chat;
import com.example.unistud.Helpers.Student;
import com.example.unistud.Helpers.UserAdapter;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyMessages extends AppCompatActivity {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;

    private List<Student> mStudent;

    FirebaseUser fUser;

    DatabaseReference reference;

    private List<String> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        studentList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()){
                    Chat chat = taskSnapshot.getValue(Chat.class);

                    if(chat.getSender().equals(fUser.getUid())){
                        studentList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fUser.getUid())) {
                        studentList.add(chat.getSender());
                    }
                }

                readChats();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats(){
        mStudent = new ArrayList<>();
        reference =FirebaseDatabase.getInstance().getReference().child("Students");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStudent.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);

                    for(String id: studentList){
                        if(student.getAccountId().equals(id)){
                            if(mStudent.size() != 0){
                                for(Student student1 : mStudent){
                                    if(!student.getAccountId().equals(student1.getAccountId())){
                                        mStudent.add(student);
                                    }
                                }
                            }
                            else {
                                mStudent.add(student);
                            }
                        }
                    }
                }

                userAdapter = new UserAdapter(MyMessages.this, mStudent);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
