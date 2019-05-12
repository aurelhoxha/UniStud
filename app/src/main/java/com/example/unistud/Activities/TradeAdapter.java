package com.example.unistud.Activities;
//package info.androidhive.cardview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.unistud.Helpers.SavedObject;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.MyViewHolder> {


    private Context mContext;
    private List<Trade_Item> itemList;
    private String itemId;
    private String title;
    private String category;
    private String userId;
    private String id;
    private int pos;

    //Database References
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public static final String ITEMID = "";
    public static final String CATEGORY = "";

    int counter = 0;
    String idWeNeed;
    String titleWeNeed;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public ImageView thumbnail, overflow;
        View mView;
        public String mItemId;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }

        public void setmEventId(String mItemId){
            this.mItemId = mItemId;
        }

        public String getmItemId(){
            return mItemId;
        }


        public String getEventTitle(){
            return title.toString();
        }
    }


    public TradeAdapter(Context mContext, List<Trade_Item> albumList) {
        this.mContext = mContext;
        this.itemList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trade_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Trade_Item item = itemList.get(position);

        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice() + " TL");

        itemId = item.getItemId();
        title = item.getTitle();
        category= item.getCategory();

        // loading item cover using Glide library
        Glide.with(mContext).load(item.getImage()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
                //geting the item ID

                pos = holder.getAdapterPosition();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Trade").child(category);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
                                if(counter == pos){
                                    Log.d("RIGHTTTTTT", counter + " " + pos);
                                    Trade_Item a = taskSnapshot.getValue(Trade_Item.class);
                                    idWeNeed = a.getItemId();
                                    titleWeNeed = a.getTitle();
                                    break;
                                }
                                else {
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

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        counter = 0;
        inflater.inflate(R.menu.menu_trade, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    //Initialize the database
                    Log.d("Positionnn111", pos + "");
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    userId = mFirebaseUser.getUid();
                    Log.d("IDWENEED", idWeNeed);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_items").child(idWeNeed).child("objectTitle");
                    databaseReference.setValue(titleWeNeed);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_items").child(idWeNeed).child("objectId");
                    databaseReference.setValue(idWeNeed);
                    counter = 0;
                    return true;

                case R.id.action_view_details:
                    Toast.makeText(mContext, "View Details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, StudentItemProfile.class);
                    Log.d("YYYYYYY", idWeNeed + "  " + category);
                    intent.putExtra(ITEMID,idWeNeed + " " + category);
                    mContext.startActivity(intent);
                    counter = 0;

                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
