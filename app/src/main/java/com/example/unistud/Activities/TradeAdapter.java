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
import com.example.unistud.Helpers.Trade_Item;
import com.example.unistud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.MyViewHolder> {


    private Context mContext;
    private List<Trade_Item> itemList;
    private String itemId;
    private String title;
    private String category;
    private String userId;

    //Database References
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public static final String ITEMID = "";
    public static final String CATEGORY = "";


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

         itemId = item.getItemId(); //geting the item ID

         title = item.getTitle();
         category= item.getCategory();


        // loading item cover using Glide library
        Glide.with(mContext).load(item.getImage()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
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
        inflater.inflate(R.menu.menu_trade, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    //Initialize the database
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    userId = mFirebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_items").child(itemId).child("objectTitle");
                    databaseReference.setValue(title);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(userId).child("saved_items").child(itemId).child("objectId");
                    databaseReference.setValue(itemId);

                    return true;
                case R.id.action_view_details:
                    Toast.makeText(mContext, "View Details", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, StudentItemProfile.class);
                    intent.putExtra(ITEMID,itemId + " " + category);
                    mContext.startActivity(intent);

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
