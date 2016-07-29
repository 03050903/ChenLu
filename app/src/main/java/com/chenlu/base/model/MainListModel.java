package com.chenlu.base.model;

import java.util.ArrayList;

/**
 * <font color='#9B77B2'>该类的主要用途:</font><br/><font color='#36FC2C'><b>
 * <p>首页显示的数据</p>
 * <b/></font><br/><hr/>
 * <b><font color='#05B8FD'>作者: C&C</font></b><br/><br/>
 * <b><font color='#05B8FD'>创建时间：2016/7/28</font></b><br/><br/>
 * <b><font color='#05B8FD'>联系方式：862530304@qq.com</font></b>
 */

public class MainListModel
{
    /**
     * 相册首页显示的图片
     */
    private String album;

    /**
     * 打开相册显示的图片
     */
    private ArrayList<SystemPhotoModel> photoList;
    /**
     * 相册的描述
     */
    private String description;

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public ArrayList<SystemPhotoModel> getPhotoList()
    {
        return photoList;
    }

    public void setPhotoList(ArrayList<SystemPhotoModel> photoList)
    {
        this.photoList = photoList;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
