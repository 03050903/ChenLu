package com.chenlu.base.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chenlu.base.R;

/**
 * Created by C&C on 2016/6/3.
 */

public class NoteListHolder extends RecyclerView.ViewHolder
{
    public TextView titleTextView;
    public NoteListHolder(View itemView)
    {
        super(itemView);
        titleTextView= (TextView) itemView.findViewById(R.id.tv_note_title);
    }
}
