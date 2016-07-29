package com.chenlu.base.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * <font color='#9B77B2'>该类的主要用途:</font><br/><font color='#36FC2C'><b>
 * <p></p>
 * <b/></font><br/><hr/>
 * <b><font color='#05B8FD'>作者: C&C</font></b><br/><br/>
 * <b><font color='#05B8FD'>创建时间：2016/7/15</font></b><br/><br/>
 * <b><font color='#05B8FD'>联系方式：862530304@qq.com</font></b>
 */

public class ScreenUtils
{
    /**
     * 获取状态栏的高度
     * @return          状态栏的高度。获取失败为0
     */
    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取ActionBar的大小
     * @param context
     * @return              获取失败的时候返回0
     */
    public static int getActionSize(Context context)
    {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float dimension = typedArray.getDimension(0, 0);
        return (int) dimension;
    }
}
