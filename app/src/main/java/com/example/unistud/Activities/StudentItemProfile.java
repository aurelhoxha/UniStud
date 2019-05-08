package com.example.unistud.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.unistud.Activities.TradeAdapter.CATEGORY;
import static com.example.unistud.Activities.TradeAdapter.ITEMID;


public class StudentItemProfile extends AppCompatActivity {

    //Database Variables
    private DatabaseReference databaseReference;
    private String itemInfo;
    private String mItemId;
    private String itemTitle;
    private String itemLocation;
    private String itemDesc;
    private String itemImage;
    private String price;
    private String category;
    private String userId;

    private ImageView mItemImage;
    private TextView mItemTitle;
    private TextView mItemLocation;
    private TextView mItemDesc;
    private TextView mPrice;
    private TextView mItemCategory;

    private ProgressDialog mProgress;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_item_profile);

        //Initialize the UI Components
        mItemImage = findViewById(R.id.student_item_profile_image);
        mItemTitle = findViewById(R.id.student_item_profile_title);
        mPrice = findViewById(R.id.student_item_price);
        mItemDesc = findViewById(R.id.student_item_profile_desc);
        mItemCategory = findViewById(R.id.student_item_profile_category);

        //Get The Internship ID
        Intent intent = getIntent();
        itemInfo = intent.getStringExtra(ITEMID);
        mItemId = itemInfo.substring(0, itemInfo.indexOf(" "));
        category = itemInfo.substring(itemInfo.indexOf(" ") + 1);
        //Initialize the Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trade").child(category).child(mItemId);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemTitle = (String) dataSnapshot.child("title").getValue();
                category = (String) dataSnapshot.child("category").getValue();
                itemDesc = (String) dataSnapshot.child("description").getValue();
                price = (String) dataSnapshot.child("price").getValue();
                itemImage = (String) dataSnapshot.child("image").getValue();
                Picasso.get().load(itemImage).into(mItemImage);
                mItemTitle.setText(itemTitle);
                mItemCategory.setText(category);
                mItemDesc.setText(itemDesc);
                mPrice.setText(price);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
