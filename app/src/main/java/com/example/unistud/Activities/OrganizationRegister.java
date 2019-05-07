package com.example.unistud.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unistud.Helpers.Organization;
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

public class OrganizationRegister extends AppCompatActivity implements View.OnClickListener {

    private EditText category;
    private EditText location;
    private EditText description;
    private TextView title;

    private ImageButton profile_pic;
    private Button submit;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String orgId;

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
    Organization mOrganization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_registration);

        //Initializing the variables

        category = (EditText) findViewById(R.id.organization_category);
        location = (EditText) findViewById(R.id.organization_location);
        description = (EditText) findViewById(R.id.organization_description);
        title = (TextView) findViewById(R.id.register_title);
        submit = (Button) findViewById(R.id.submit_org);
        profile_pic = (ImageButton) findViewById(R.id.organization_profile_image);

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Organizations");
        orgId = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mOrganization = dataSnapshot.child(orgId).getValue(Organization.class);
                String organizationFullName = mOrganization.getFullname();
                String organizationProfilePicture = mOrganization.getProfile_photo();
                Picasso.get().load(organizationProfilePicture).into(profile_pic);
                title.setText("Welcome " + organizationFullName + " Let's start completing your Organization profile.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDialog = new ProgressDialog(this);

        //Set the Listeners
        submit.setOnClickListener(this);

        //the photo

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == submit){
            submitProfile();
        }
    }


    private void submitProfile() {

        final String mcategory, mlocation, mdescription;

        mcategory = category.getText().toString().trim();
        mlocation = location.getText().toString().trim();
        mdescription = description.getText().toString().trim();

        if (!TextUtils.isEmpty(mcategory) && !TextUtils.isEmpty(mlocation) && !TextUtils.isEmpty(mdescription) && mImageUri != null){

            final StorageReference filePath = mStorageReference.child("Organization_Profile_Photo").child(mImageUri.getLastPathSegment());
            final UploadTask mUploadTask = filePath.putFile(mImageUri);


            mUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(OrganizationRegister.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Organizations").child(orgId);
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            dataSnapshot.getRef().child("domain").setValue(mcategory);
                                            dataSnapshot.getRef().child("location").setValue(mlocation);
                                            dataSnapshot.getRef().child("description").setValue(mdescription);
                                            dataSnapshot.getRef().child("profile_photo").setValue(downloadUri.toString());
                                            dataSnapshot.getRef().child("profile_completed").setValue("true");
                                            Intent intent = new Intent(OrganizationRegister.this, MainActivity.class);
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
