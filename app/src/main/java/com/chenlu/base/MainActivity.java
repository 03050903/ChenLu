package com.chenlu.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.chenlu.base.frag.MainFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //显示菜单
    private DrawerLayout drawerLayout;
    //菜单中显示的内容
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout= (DrawerLayout) findViewById(R.id.dl_main);
        //设置菜单点击的时候执行的事件
        setupDrawerContent();
        MainFragment fragment=new MainFragment();
        fragment.setOnClickedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();

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
        //关闭Drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public void onClick(View v)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
