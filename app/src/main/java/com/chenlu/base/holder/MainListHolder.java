package com.chenlu.base.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenlu.base.R;
import com.chenlu.base.utils.ViewHelper;

/**
 * <font color='#9B77B2'>该类的主要用途:</font><br/><font color='#36FC2C'><b>
 * <p></p>
 * <b/></font><br/><hr/>
 * <b><font color='#05B8FD'>作者: C&C</font></b><br/><br/>
 * <b><font color='#05B8FD'>创建时间：2016/7/29</font></b><br/><br/>
 * <b><font color='#05B8FD'>联系方式：862530304@qq.com</font></b>
 */

public class MainListHolder extends RecyclerView.ViewHolder
{
    public LinearLayout photoLayout;
    public TextView contentTextView;
    public ImageView titleImageView;
    public MainListHolder(View itemView)
    {
        super(itemView);
        photoLayout= ViewHelper.getView(itemView, R.id.ll_photo);
        titleImageView=ViewHelper.getView(itemView,R.id.img_title);
        contentTextView=ViewHelper.getView(itemView,R.id.tv_content);
    }
}
