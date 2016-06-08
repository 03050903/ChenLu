package com.chenlu.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chenlu.base.adapter.NoteListAdapter;
import com.chenlu.base.model.NoteListModel;
import com.promegu.xlog.base.XLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C&C on 2016/6/3.
 */
@XLog
public class NotelistActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);

        CollapsingToolbarLayout layout= (CollapsingToolbarLayout) findViewById(R.id.ctl_header);
        layout.setTitle(getString(R.string.note_list));
        layout.setCollapsedTitleTextColor(Color.WHITE);
        layout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        List<NoteListModel> noteList=new ArrayList();
        for (int i=0;i<50;i++)
        {
            NoteListModel model=new NoteListModel();
            model.setNoteTitle("Title"+i);
            noteList.add(model);
        }
        NoteListAdapter adapter=new NoteListAdapter(noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        testChen();
    }
    private void testChen()
    {
        Log.e("testChen","testChen");
    }
    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
    }
}
