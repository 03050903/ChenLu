package com.chenlu.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chenlu.base.R;
import com.chenlu.base.holder.MainListHolder;
import com.chenlu.base.model.MainListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <font color='#9B77B2'>该类的主要用途:</font><br/><font color='#36FC2C'><b>
 * <p></p>
 * <b/></font><br/><hr/>
 * <b><font color='#05B8FD'>作者: C&C</font></b><br/><br/>
 * <b><font color='#05B8FD'>创建时间：2016/7/29</font></b><br/><br/>
 * <b><font color='#05B8FD'>联系方式：862530304@qq.com</font></b>
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListHolder>
{
    private Context context;
    private List<MainListModel> list;
    private List<ImageView> cacheList;
    public MainListAdapter(Context context, ArrayList<MainListModel> list)
    {
        this.context=context;
        this.list=list;

    }
    @Override
    public MainListHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= View.inflate(context, R.layout.item_main_list,null);
        MainListHolder holder=new MainListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainListHolder holder, int position)
    {
        MainListModel model = list.get(position);
        Glide.with(context).load(model.getAlbum()).into(holder.titleImageView);
        holder.contentTextView.setText(model.getDescription());
        int size=model.getPhotoList().size();
        int childCount = holder.photoLayout.getChildCount();
        if (childCount>size)
        {
            while(holder.photoLayout.getChildCount()>size)
            {
                ImageView imageView= (ImageView) holder.photoLayout.getChildAt(holder.photoLayout.getChildCount()-1);
                holder.photoLayout.removeViewAt(;
            }

        }

    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
