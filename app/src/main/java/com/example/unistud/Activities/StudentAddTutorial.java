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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Helpers.Tutorial;
import com.example.unistud.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class StudentAddTutorial extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private ImageButton mTutorialSelectImage;
    private EditText mTutorialTitle;
    private EditText mTutorialDesc;
    private EditText mTutorialLocation;
    private String downloadUrl;
    private TextView mTextOfDate;
    private ImageButton mAddTheDateButton;

    private Button mSubmitButton;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;
    private DatePickerDialog.OnDateSetListener mDataSetListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_tutorial);

        mStorage  = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tutorials");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Connect the Attributes to XML
        mTutorialSelectImage = (ImageButton)findViewById(R.id.tutorial_image_select);
        mTutorialTitle = (EditText)findViewById(R.id.tutorial_title_field);
        mTutorialDesc = (EditText)findViewById(R.id.tutorial_desc_field);
        mTutorialLocation = (EditText)findViewById(R.id.tutorial_location_field);
        mTextOfDate = (TextView)findViewById(R.id.tutorial_pick_date);
        mAddTheDateButton = (ImageButton)findViewById(R.id.tutorialDateButton);
        mSubmitButton = (Button)findViewById(R.id.submitTBtn);
        mProgress    = new ProgressDialog(this);

        mTutorialSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mAddTheDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCal = Calendar.getInstance();
                int year = myCal.get(Calendar.YEAR);
                int month = myCal.get(Calendar.MONTH);
                int day = myCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDialog = new DatePickerDialog(
                        StudentAddTutorial.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataSetListener,
                        year,month,day);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                mTextOfDate.setText(date);
            }
        };

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPostingTutorial();
            }
        });

    }

    private void startPostingTutorial(){
        mProgress.setMessage("Posting the Tutorial ... ");
        mProgress.show();
        final String title_val  = mTutorialTitle.getText().toString().trim();
        final String desc_val   = mTutorialDesc.getText().toString().trim();
        final String date_val   = mTextOfDate.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val)  && !TextUtils.isEmpty(date_val) ) {
            String userId = mFirebaseUser.getUid();
            String key = "";
            Tutorial mTutorial = new Tutorial(key, title_val,desc_val,date_val, "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",userId);
            mDatabase.push().setValue(mTutorial, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    String uniqueKey = databaseReference.getKey();
                    mDatabase.child(uniqueKey).child("tutorialId").setValue(uniqueKey);
                }
            });
            mProgress.dismiss();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mTutorialSelectImage.setImageURI(mImageUri);
        }
    }
}
