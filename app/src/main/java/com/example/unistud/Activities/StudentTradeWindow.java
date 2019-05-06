package com.example.unistud.Activities;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.unistud.Activities.TradeAdapter;
import com.example.unistud.Helpers.Trade_Item;

import java.util.ArrayList;
import java.util.List;
import com.example.unistud.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.unistud.Fragments.StudentTradeFragment.TRADE_CATEGORY;
import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentTradeWindow  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TradeAdapter adapter;
    private List<Trade_Item> itemList;
    private String category;

    ///Database Variables

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_window);

        Toast.makeText(StudentTradeWindow.this, "Value:" + getIntent().getStringExtra(TRADE_CATEGORY), Toast.LENGTH_SHORT).show();
        category = getIntent().getStringExtra(TRADE_CATEGORY);

        mDatabase= FirebaseDatabase.getInstance().getReference("Trade").child(category);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        itemList = new ArrayList<>();
        adapter = new TradeAdapter(this, itemList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareItems();

        try {
            if(category.equals("Books"))
                Glide.with(this).load(R.drawable.cover1).into((ImageView) findViewById(R.id.backdrop));
            if(category.equals("Notes"))
                Glide.with(this).load(R.drawable.cover2).into((ImageView) findViewById(R.id.backdrop));
            if(category.equals("Devices"))
                Glide.with(this).load(R.drawable.cover3).into((ImageView) findViewById(R.id.backdrop));
            if(category.equals("Others"))
                Glide.with(this).load(R.drawable.cover4).into((ImageView) findViewById(R.id.backdrop));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * Adding few albums for testing
     */
    private void prepareItems() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot taskSnapshot: dataSnapshot.getChildren()){

                    Trade_Item a = taskSnapshot.getValue(Trade_Item.class);
                    itemList.add(a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter.notifyDataSetChanged();
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