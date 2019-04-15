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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.unistud.Helpers.Event;
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

public class OrganizationAddEvent extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private ImageButton mEventSelectImage;
    private EditText mEventTitle;
    private EditText mEventDesc;
    private EditText mEventLocation;

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
        setContentView(R.layout.activity_organization_add_event);

        mStorage  = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Connect the Attributes to XML
        mEventSelectImage = (ImageButton)findViewById(R.id.event_image_select);
        mEventTitle = (EditText)findViewById(R.id.event_title_field);
        mEventDesc = (EditText)findViewById(R.id.event_desc_field);
        mEventLocation = (EditText)findViewById(R.id.event_location_field);
        mTextOfDate = (TextView)findViewById(R.id.event_pick_date);
        mAddTheDateButton = (ImageButton)findViewById(R.id.eventDateButton);
        mSubmitButton = (Button)findViewById(R.id.submitBtn);
        mProgress    = new ProgressDialog(this);

        mEventSelectImage.setOnClickListener(new View.OnClickListener() {
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
                        OrganizationAddEvent.this,
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
                startPostingEvent();
            }
        });

    }

    private void startPostingEvent(){
        mProgress.setMessage("Posting the Event ... ");
        mProgress.show();
        final String title_val  = mEventTitle.getText().toString().trim();
        final String desc_val   = mEventDesc.getText().toString().trim();
        final String loc_val   = mEventLocation.getText().toString().trim();
        final String date_val   = mTextOfDate.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(loc_val) && !TextUtils.isEmpty(date_val) && mImageUri != null ) {
            StorageReference filePath = mStorage.child("Event_Images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    String userId = mFirebaseUser.getUid();
                    Event mEvent = new Event(title_val,desc_val,date_val,loc_val, downloadUrl,userId);
                    mDatabase.push().setValue(mEvent);
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
            mEventSelectImage.setImageURI(mImageUri);
        }
    }
}
