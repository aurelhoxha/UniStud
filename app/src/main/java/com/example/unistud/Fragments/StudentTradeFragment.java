package com.example.unistud.Fragments;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.unistud.Activities.StudentTradeWindow;
import com.example.unistud.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StudentTradeFragment  extends Fragment {
    public static final String TRADE_CATEGORY = "TradeCategory";
    GridLayout mainGrid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_student_unitrade, container, false);

        mainGrid = (GridLayout)myFragmentView.findViewById(R.id.main_grid);

        //set event
        setSingleEvent(mainGrid);

        return myFragmentView;
    }

    private void setSingleEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {

            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), StudentTradeWindow.class);
                    if(finalI ==0)
                        intent.putExtra(TRADE_CATEGORY,"Books");
                    if(finalI ==1)
                        intent.putExtra(TRADE_CATEGORY,"Notes");
                    if(finalI==2)
                        intent.putExtra(TRADE_CATEGORY,"Devices");
                    if(finalI==3)
                        intent.putExtra(TRADE_CATEGORY,"Others");

                    startActivityForResult(intent,1);
                }
            });
        }
    }
}



