package com.example.unistud.Helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.R;
import com.squareup.picasso.Picasso;

public class UniversitiesViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private String mUniversityId;
    private String mUniversityName;
    private TextView mViewUniversity;

    public UniversitiesViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mViewUniversity = (TextView) mView.findViewById(R.id.university_proceed_text);

    }

    public void setUniversityTitle(String title)
    {
        TextView university_title = (TextView)mView.findViewById(R.id.university_name_text);
        university_title.setText(title);
    }

    public String getmUniversityId() {
        return mUniversityId;
    }

    public void setmUniversityId(String mUniversityId) {
        this.mUniversityId = mUniversityId;
    }

    public TextView getmViewUniversity() {
        return mViewUniversity;
    }

    public void setmViewUniversity(TextView mViewUniversity) {
        this.mViewUniversity = mViewUniversity;
    }

    public String getmUniversityName() {
        return mUniversityName;
    }

    public void setmUniversityName(String mUniversityName) {
        this.mUniversityName = mUniversityName;
    }
}