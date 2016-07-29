package com.chenlu.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.chenlu.base.model.SystemPhotoModel;
import android.provider.MediaStore.Images.Media;

import java.util.ArrayList;
import java.util.List;

/**
 * <font color='#9B77B2'>该类的主要用途:</font><br/><font color='#36FC2C'><b>
 * <p></p>
 * <b/></font><br/><hr/>
 * <b><font color='#05B8FD'>作者: C&C</font></b><br/><br/>
 * <b><font color='#05B8FD'>创建时间：2016/7/28</font></b><br/><br/>
 * <b><font color='#05B8FD'>联系方式：862530304@qq.com</font></b>
 */

public class SystemUtils
{
    /**
     * 获取系统中的本地图片
     * @param context
     * @return
     */
    public static List<SystemPhotoModel> getSystemPhotoList(Context context)
    {
        ContentResolver resolver=context.getContentResolver();
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[]{
                Media.BUCKET_DISPLAY_NAME,
                Media.SIZE,
                Media.ORIENTATION,
                Media.DISPLAY_NAME,
                Media.DATE_ADDED,
                Media.DATA}, null, null, Media.DATE_ADDED+" desc");
        List<SystemPhotoModel> list=new ArrayList<>();
        if (cursor.getCount()>0)
        {

            while(cursor.moveToNext())
            {
                String filePath=cursor.getString(cursor.getColumnIndex(Media.DATA));
                if (FileUtils.isFileExist(filePath))
                {
                    SystemPhotoModel model=new SystemPhotoModel();
                    model.setAddDate(cursor.getLong(cursor.getColumnIndex(Media.DATE_ADDED)));
                    model.setDirName(cursor.getString(cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME)));
                    model.setDisplayName(cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME)));
                    model.setFilePath(filePath);
                    model.setOrientation(cursor.getInt(cursor.getColumnIndex(Media.ORIENTATION)));
                    model.setSize(cursor.getInt(cursor.getColumnIndex(Media.SIZE)));
                    list.add(model);
                }
            }
        }
        cursor.close();
        return list;
    }
}
