package com.chenlu.base;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout= (DrawerLayout) findViewById(R.id.dl_main);
        drawerToggle=setupDrawerToggle();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        setupDrawerContent();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onPostCreate(savedInstanceState, persistentState);
        //同步状态
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //当配置信息改变的时候执行
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * 创建一个ActionBarDrawerToggle
     * @return
     */
    private ActionBarDrawerToggle setupDrawerToggle()
    {
        return new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
    }

    /**
     * 设置drawer中选项点击的时候显示的内容
     */
    private void setupDrawerContent()
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                selectItem(item);
                return true;
            }
        });
    }

    /**
     * 当菜单被点击的时候执行的方法
     * @param item
     */
    private void selectItem(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_note_list:
                break;
            case R.id.menu_photo_list:
                break;
        }
        //设置这个菜单被选中
        item.setChecked(true);
        //设置这个页面显示的标题
        setTitle(item.getTitle());
        //关闭Drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //使drawerToggle也可以处理onOptionsItemSelected事件
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
