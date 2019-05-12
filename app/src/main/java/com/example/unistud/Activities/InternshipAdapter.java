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
import com.example.unistud.Helpers.Internship;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.MyViewHolder> {


    private Context mContext;
    private List<Internship> internshipList;
    private String internshipId;
    private String title;
    private String userId;
    private int pos;
    public static final String INTERNSHIP_ID = "";

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

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public InternshipAdapter(Context mContext, List<Internship> list) {
        this.mContext = mContext;
        this.internshipList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Internship internship = internshipList.get(position);
        holder.title.setText(internship.getInternshipTitle());
        holder.date.setText(internship.getInternshipDate());

        internshipId = internship.getInternshipId();
        title = internship.getInternshipTitle();

        // loading item cover using Glide library
        Glide.with(mContext).load(internship.getInternshipImage()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);

                pos = holder.getAdapterPosition();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Internships");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                                if (counter == pos) {
                                    Log.d("RIGHTTTTTT", counter + " " + pos);
                                    Internship a = taskSnapshot.getValue(Internship.class);
                                    idWeNeed = a.getInternshipId();
                                    titleWeNeed = a.getInternshipTitle();
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
                    Intent intent = new Intent(mContext, StudentInternshipProfile_1.class);
                    intent.putExtra(INTERNSHIP_ID,idWeNeed);
                    //Log.d("IDDDD", idWeNeed);
                    mContext.startActivity(intent);
                    counter = 0;

                    return true;

                case R.id.action_remove:
                    Toast.makeText(mContext, "Remove from favorites", Toast.LENGTH_SHORT).show();
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    userId = mFirebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_internships").child(idWeNeed).child("objectTitle");
                    databaseReference.setValue(null);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_internships").child(idWeNeed).child("objectId");
                    databaseReference.setValue(null);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return internshipList.size();
    }



}
