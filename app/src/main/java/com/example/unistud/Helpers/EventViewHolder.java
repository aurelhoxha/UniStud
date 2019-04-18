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

public class EventViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private TextView mViewEvent;
    private String mEventId;


    public EventViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mViewEvent = (TextView) mView.findViewById(R.id.event_proceed_text);
    }

    public void setEventTitle(String title)
    {
        TextView post_title = (TextView)mView.findViewById(R.id.event_name_text);
        post_title.setText(title);
    }

    public void setEventDate(String date)
    {
        TextView post_desc = (TextView)mView.findViewById(R.id.event_date_text);
        post_desc.setText(date);
    }

    public void setEventImage(Context ctx, String image)
    {
        ImageView post_image = (ImageView)mView.findViewById(R.id.event_image_view);
        Picasso.get().load(image).into(post_image);
    }
    public void setmEventId(String mEventId){
        this.mEventId = mEventId;
    }

    public TextView getmViewEvent(){
        return mViewEvent;
    }
    public String getmEventId(){
        return mEventId;
    }

}