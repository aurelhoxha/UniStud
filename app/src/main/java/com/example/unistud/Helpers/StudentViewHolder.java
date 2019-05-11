package com.example.unistud.Helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unistud.R;
import com.squareup.picasso.Picasso;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private TextView mSendMessage;
    private String mStudentId;


    public StudentViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mSendMessage = (TextView) mView.findViewById(R.id.user_send_text);
    }

    public void setUserTitle(String title)
    {
        TextView user_title = (TextView)mView.findViewById(R.id.user_name_text);
        user_title.setText(title);
    }


    public void setUserImage(String image)
    {
        ImageView user_image = (ImageView)mView.findViewById(R.id.user_image_view);
        Picasso.get().load(image).into(user_image);
    }

    public TextView getmSendMessage() {
        return mSendMessage;
    }

    public void setmSendMessage(TextView mSendMessage) {
        this.mSendMessage = mSendMessage;
    }

    public String getmStudentId() {
        return mStudentId;
    }

    public void setmStudentId(String mStudentId) {
        this.mStudentId = mStudentId;
    }
}