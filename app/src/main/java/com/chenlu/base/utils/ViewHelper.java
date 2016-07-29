package com.chenlu.base.utils;

import android.app.Activity;
import android.view.View;

/**
 * <font color='#9B77B2'>该类的主要用途:</font><br/><font color='#36FC2C'><b>
 * <p></p>
 * <b/></font><br/><hr/>
 * <b><font color='#05B8FD'>作者: C&C</font></b><br/><br/>
 * <b><font color='#05B8FD'>创建时间：2016/7/29</font></b><br/><br/>
 * <b><font color='#05B8FD'>联系方式：862530304@qq.com</font></b>
 */

public class ViewHelper
{
    public static <T> T getView(View view, int viewID)
    {
        return (T) view.findViewById(viewID);
    }
    public static <T> T getView(Activity activity,int viewID)
    {
        return (T) activity.findViewById(viewID);
    }
}
