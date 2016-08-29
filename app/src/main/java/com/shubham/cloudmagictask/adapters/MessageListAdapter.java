package com.shubham.cloudmagictask.adapters;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubham.cloudmagictask.MyApplication;
import com.shubham.cloudmagictask.R;
import com.shubham.cloudmagictask.models.Message;

import java.util.ArrayList;

/**
 * Created by Shubham Gupta on 28-08-2016.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MenuHolder> {
    public ArrayList<Message> messages;

    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MenuHolder menuHolder = new MenuHolder(v);
        return menuHolder;
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {
        holder.participant.setText(messages.get(position).getParticipants().get(0));
        holder.subject.setText(messages.get(position).getSubject());
        holder.preview.setText(messages.get(position).getPreview());
        if(messages.get(position).isStarred())
            holder.isStarred.getDrawable().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.starredEmailColor), PorterDuff.Mode.SRC_ATOP);
        else
            holder.isStarred.getDrawable().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.unstarredEmailColor), PorterDuff.Mode.SRC_ATOP);

        if(messages.get(position).isRead()) {
            holder.participant.setTypeface(Typeface.DEFAULT);
            holder.subject.setTypeface(Typeface.DEFAULT);
        }
        else{
            holder.participant.setTypeface(Typeface.DEFAULT_BOLD);
            holder.subject.setTypeface(Typeface.DEFAULT_BOLD);
        }
        holder.timeStamp.setText(messages.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MenuHolder extends RecyclerView.ViewHolder {
        TextView participant;
        TextView subject;
        TextView preview;
        TextView timeStamp;
        ImageView isStarred;

        public MenuHolder(View v) {
            super(v);
            participant = (TextView) v.findViewById(R.id.textview_emailSender);
            subject = (TextView) v.findViewById(R.id.textview_subject);
            preview = (TextView) v.findViewById(R.id.textview_preview);
            timeStamp = (TextView) v.findViewById(R.id.textview_time);
            isStarred = (ImageView) v.findViewById(R.id.imageview_isStarred);


        }
    }
}
