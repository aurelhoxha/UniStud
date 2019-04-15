package com.example.unistud.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.Internship;
import com.example.unistud.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class OrganizationAddInternship extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private ImageButton mInternshipSelectImage;
    private EditText mInternshipTitle;
    private EditText mInternshipDesc;
    private TextView mInternshipDate;
    private ImageButton mAddInternshipDate;

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
        setContentView(R.layout.activity_organization_add_internship);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Internships");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Connect the Attributes to XML
        mInternshipSelectImage = (ImageButton)findViewById(R.id.internship_image_select);
        mInternshipTitle = (EditText)findViewById(R.id.internship_title_field);
        mInternshipDesc = (EditText)findViewById(R.id.internship_desc_field);
        mInternshipDate = (TextView)findViewById(R.id.internship_pick_date);
        mAddInternshipDate = (ImageButton)findViewById(R.id.internshipDateButton);
        mSubmitButton = (Button)findViewById(R.id.submitBtn);

        mProgress    = new ProgressDialog(this);
        mInternshipSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mAddInternshipDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCal = Calendar.getInstance();
                int year = myCal.get(Calendar.YEAR);
                int month = myCal.get(Calendar.MONTH);
                int day = myCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog myDialog = new DatePickerDialog(
                        OrganizationAddInternship.this,
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
                mInternshipDate.setText(date);
            }
        };

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPostingInternship();
            }
        });
    }

    private void startPostingInternship(){
        mProgress.setMessage("Posting the Internship ... ");
        mProgress.show();
        final String title_val  = mInternshipTitle.getText().toString().trim();
        final String desc_val   = mInternshipDesc.getText().toString().trim();
        final String date_val   = mInternshipDate.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(date_val) && mImageUri != null ) {
            StorageReference filePath = mStorage.child("Internship_Images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                    String userId = mFirebaseUser.getUid();
                    Internship mInternship = new Internship(title_val,desc_val,date_val, downloadUrl,userId);
                    mDatabase.push().setValue(mInternship);
                    mProgress.dismiss();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mInternshipSelectImage.setImageURI(mImageUri);
        }
    }
}
