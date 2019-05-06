package com.example.unistud.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.unistud.Helpers.Trade_Item;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentAddItem extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private ImageButton mItemSelectImage;
    private EditText mItemTitle;
    private Spinner mItemCategory;
    private EditText mItemDesc;
    private EditText price;
    private EditText mItemLocation;

    private String downloadUrl;

    private Button mSubmitButton;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;
    private String selectedItemText;

    private StorageReference mStorage;
    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Trade");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Connect the Attributes to XML
        mItemSelectImage = (ImageButton) findViewById(R.id.item_image_select);
        mItemTitle = (EditText) findViewById(R.id.item_title_field);
        mItemDesc = (EditText) findViewById(R.id.item_desc_field);
        mItemCategory= (Spinner)findViewById(R.id.item_category_field);
        price = (EditText) findViewById(R.id.item_price_field);
        mSubmitButton = (Button) findViewById(R.id.submitBtn);
        mProgress = new ProgressDialog(this);

        mItemSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        // Initializing String Array for category
        String[] options = new String[]{
                "Select the category...",
                "Books",
                "Notes",
                "Devices",
                "Others"
        };

        final List<String> categoryList = new ArrayList<>(Arrays.asList(options));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,categoryList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mItemCategory.setAdapter(spinnerArrayAdapter);

        mItemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPostingItem();
            }
        });
    }

    private void startPostingItem(){
        mProgress.setMessage("Adding the Item ... ");
        mProgress.show();
        final String title_val  = mItemTitle.getText().toString().trim();
        final String category_val = selectedItemText;
        final String desc_val   = mItemDesc.getText().toString().trim();
        final String price_val = price.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(category_val)&& !TextUtils.isEmpty(price_val) ) {
            final StorageReference filePath = mStorage.child("Item_Images").child(mImageUri.getLastPathSegment());
            final UploadTask mUploadTask = filePath.putFile(mImageUri);

            mUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(StudentAddItem.this,"Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            downloadUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                String userId = mFirebaseUser.getUid();
                                String key = "";
                                Trade_Item mItem = new Trade_Item(key, title_val,category_val,desc_val,price_val,downloadUri.toString(),userId);
                                mDatabase.child(selectedItemText).push().setValue(mItem, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        String uniqueKey = databaseReference.getKey();
                                        mDatabase.child(selectedItemText).child(uniqueKey).child("itemId").setValue(uniqueKey);
                                    }
                                });
                                mProgress.dismiss();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                finish();
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
            mItemSelectImage.setImageURI(mImageUri);
        }
    }





}
