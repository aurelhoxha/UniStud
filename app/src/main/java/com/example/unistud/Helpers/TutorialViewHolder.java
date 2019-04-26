package com.example.unistud.Helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class TutorialViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private TextView mViewTutorial;
    private String mTutorialId;

    public TutorialViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mViewTutorial = (TextView) mView.findViewById(R.id.tutorial_proceed_text);
    }

    public void setTutorialTitle(String title)
    {
        TextView post_title = (TextView)mView.findViewById(R.id.tutorial_name_text);
        post_title.setText(title);
    }

    public void setTutorialDate(String date)
    {
        TextView post_desc = (TextView)mView.findViewById(R.id.tutorial_date_text);
        post_desc.setText(date);
    }


    public void setmTutorialId(String mTutorialId){
        this.mTutorialId = mTutorialId;
    }

    public TextView getmViewTutorial(){
        return mViewTutorial;
    }

    public String getmTutorialId(){
        return mTutorialId;
    }

}