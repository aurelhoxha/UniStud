package com.example.unistud.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.Student;
import com.example.unistud.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class StudentRegister1 extends AppCompatActivity implements View.OnClickListener {



    private TextView student_birthday;
    private ImageView BirthdaySetDate;
    private EditText student_phone;
    private RadioGroup student_gender;
    private RadioButton gender;
    private Button next_Student;
    private ImageButton profile_pic;
    private TextView title;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String userId;

    private String downloadUrl;

    //Firebase Connection Instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageReference;
    //Other Variables
    private ProgressDialog mDialog;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Student mStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register1);

        //Initializing the variables
        student_birthday = (TextView) findViewById(R.id.birthday_pick_date);
        BirthdaySetDate = (ImageView) findViewById(R.id.birthdayDateButton);
        student_phone = (EditText) findViewById(R.id.student_phone);
        student_gender= (RadioGroup)findViewById(R.id.student_gender);
        next_Student = (Button) findViewById(R.id.next_Student);
        profile_pic = (ImageButton) findViewById(R.id.user_profile_image);
        title = (TextView) findViewById(R.id.register_title);


        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Students");
        userId = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStudent = dataSnapshot.child(userId).getValue(Student.class);
                String studentFullName = mStudent.getFullname();
                String studentProfilePicture = mStudent.getProfile_photo();
                Picasso.get().load(studentProfilePicture).into(profile_pic);
                title.setText("Welcome " + studentFullName + " Let's start completing your profile.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDialog = new ProgressDialog(this);

        //Set the Listeners
        next_Student.setOnClickListener(this);

        //the photo

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


        BirthdaySetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCal = Calendar.getInstance();
                int year = myCal.get(Calendar.YEAR);
                int month = myCal.get(Calendar.MONTH);
                int day = myCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDialog = new DatePickerDialog(
                        StudentRegister1.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                student_birthday.setText(date);
            }
        };


    }

    @Override
    public void onClick(View v) {
        if(v == next_Student){
            goNext();
        }
    }

    private void goNext() {


        final String birthday, phoneNumber, g;

        birthday = student_birthday.getText().toString().trim();
        phoneNumber = student_phone.getText().toString().trim();
        int selectedId = student_gender.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        gender = (RadioButton) findViewById(selectedId);
        g = gender.getText().toString().trim();

        if (!TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(g) && mImageUri != null) {
            final StorageReference filePath = mStorageReference.child("Student_Profile_Photo").child(mImageUri.getLastPathSegment());
            final UploadTask mUploadTask = filePath.putFile(mImageUri);

            mUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(StudentRegister1.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Uri downloadUri = task.getResult();
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId);
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            dataSnapshot.getRef().child("birthday").setValue(birthday);
                                            dataSnapshot.getRef().child("gender").setValue(g);
                                            dataSnapshot.getRef().child("mobile_phone").setValue(phoneNumber);
                                            dataSnapshot.getRef().child("profile_photo").setValue(downloadUri.toString());
                                            Intent intent = new Intent(StudentRegister1.this, StudentRegister2.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            profile_pic.setImageURI(mImageUri);
        }
    }

}