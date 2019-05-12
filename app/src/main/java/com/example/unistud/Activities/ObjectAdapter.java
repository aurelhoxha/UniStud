package com.example.unistud.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.unistud.Helpers.Event;
import com.example.unistud.Helpers.Trade_Item;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.MyViewHolder> {


    private Context mContext;
    private List<Event> eventList;
    private String eventId;
    private String title;
    private String category;
    private String userId;
    private String id;
    private int pos;
    public static final String EVENT_ID = "";

    //Database References
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    int counter = 0;
    String idWeNeed;
    String titleWeNeed;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;
        public ImageView thumbnail, overflow;
        View mView;
        public String mEventId;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public ObjectAdapter(Context mContext, List<Event> list) {
        this.mContext = mContext;
        this.eventList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getEventTitle());
        holder.date.setText(event.getEventDate());

        eventId = event.getEventId();
        title = event.getEventTitle();

        // loading item cover using Glide library
        Glide.with(mContext).load(event.getEventImage()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);

                pos = holder.getAdapterPosition();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                                if (counter == pos) {
                                    Log.d("RIGHTTTTTT", counter + " " + pos);
                                    Event a = taskSnapshot.getValue(Event.class);
                                    idWeNeed = a.getEventId();
                                    titleWeNeed = a.getEventTitle();
                                    break;
                                } else {
                                    Log.d("DIDDDD IT", counter + " ");
                                    counter++;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Log.d("Positionnn", pos + "");
                //category= holder.getCategory();
            }

            });

        }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        counter=0;
        inflater.inflate(R.menu.menu_profile, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_view_details:
                    Toast.makeText(mContext, "View Details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, StudentEventProfile_1.class);
                    intent.putExtra(EVENT_ID,idWeNeed);
                    //Log.d("IDDDD", idWeNeed);
                    mContext.startActivity(intent);
                    counter = 0;

                    return true;
                case R.id.action_remove:
                    Toast.makeText(mContext, "Remove from favorites", Toast.LENGTH_SHORT).show();
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    userId = mFirebaseUser.getUid();
                    Log.d("IDWENEED", idWeNeed);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_events").child(idWeNeed).child("objectTitle");
                    databaseReference.setValue(null);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_events").child(idWeNeed).child("objectId");
                    databaseReference.setValue(null);

                    return true;

                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }



}
