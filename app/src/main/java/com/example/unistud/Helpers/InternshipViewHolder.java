package com.example.unistud.Helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.R;
import com.squareup.picasso.Picasso;

public class InternshipViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private TextView mViewInternship;
    private String mInternshipId;
    private String mOrganizationId;

    public InternshipViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mViewInternship = (TextView) mView.findViewById(R.id.internship_proceed_text);

    }

    public void setInternshipTitle(String title)
    {
        TextView internship_title = (TextView)mView.findViewById(R.id.internship_name_text);
        internship_title.setText(title);
    }

    public void setInternshipDate(String date)
    {
        TextView internship_date = (TextView)mView.findViewById(R.id.internship_date_text);
        internship_date.setText(date);
    }

    public void setInternshipImage(Context ctx, String image)
    {
        ImageView internship_image = (ImageView)mView.findViewById(R.id.internship_image_view);
        Picasso.get().load(image).into(internship_image);
    }

    public TextView getmViewInternship() {
        return mViewInternship;
    }

    public void setmInternshipId(String mInternshipId) {
        this.mInternshipId = mInternshipId;
    }

    public String getmInternshipId() {
        return mInternshipId;
    }

    public String getmOrganizationId() {
        return mOrganizationId;
    }

    public void setmOrganizationId(String mOrganizationId) {
        this.mOrganizationId = mOrganizationId;
    }
}