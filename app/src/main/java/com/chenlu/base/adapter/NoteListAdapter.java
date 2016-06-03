package com.chenlu.base.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chenlu.base.R;
import com.chenlu.base.model.NoteListHolder;
import com.chenlu.base.model.NoteListModel;

import java.util.List;

/**
 * Created by C&C on 2016/6/3.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListHolder>
{
    private List<NoteListModel> list;

    public NoteListAdapter(List<NoteListModel> list)
    {
        this.list = list;
    }
    @Override
    public NoteListHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=View.inflate(parent.getContext(), R.layout.item_note_list,null);
        NoteListHolder viewHolder=new NoteListHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteListHolder holder, int position)
    {
        holder.titleTextView.setText(list.get(position).getNoteTitle());
    }

    @Override
    public int getItemCount()
    {
        return list==null?0:list.size();
    }
}
