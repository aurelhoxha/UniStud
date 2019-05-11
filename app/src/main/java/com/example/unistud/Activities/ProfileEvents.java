package com.example.unistud.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.EventViewHolder;
import com.example.unistud.Helpers.SavedObject;
import com.example.unistud.Helpers.Trade_Item;
import com.example.unistud.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//
//
public class ProfileEvents extends AppCompatActivity {

    private RecyclerView mEventList;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Event> options;
    public static final String EVENT_ID = "EventId";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userId;
    private DatabaseReference databaseReferenceEvent;
    private Event mEvent;

    private ArrayList<Event> eventsList;


    //Variables to save to display event
    private String eventTitle;
    private String eventDate;
    private String eventDesc;
    private String eventImage;
    private String eventLocation;
    private String eventCreator;

    private RecyclerView recyclerView;
    private ObjectAdapter adapter;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_window);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        eventsList = new ArrayList<Event>();

        adapter = new ObjectAdapter(this, eventsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ProfileEvents.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepare();

        try {
            Glide.with(this).load(R.drawable.cover2).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void prepare(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){
                    SavedObject a = taskSnapshot.getValue(SavedObject.class);

                    id = a.getObjectId();
                    Log.d("cheng id", id + "");
                    databaseReferenceEvent = FirebaseDatabase.getInstance().getReference().child("Events").child(id);
                    databaseReferenceEvent.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            eventTitle = (String) dataSnapshot.child("eventTitle").getValue();
                            eventLocation = (String) dataSnapshot.child("eventPlace").getValue();
                            eventDesc = (String) dataSnapshot.child("eventDesc").getValue();
                            eventDate = (String) dataSnapshot.child("eventDate").getValue();
                            eventImage = (String) dataSnapshot.child("eventImage").getValue();
                            eventCreator = (String) dataSnapshot.child("eventCreatorId").getValue();

                            Event event = new Event(id, eventTitle, eventDesc, eventDate, eventLocation, eventImage,eventCreator);
                            eventsList.add(event);

                            adapter.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                   // Log.d("vcccccccc", eventsList.get(0).getEventTitle() + "");
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.d("xxxxxxxxxxxxxxxxx", eventsList.get(0).getEventTitle() + "");

        //adapter.notifyDataSetChanged();
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
