package com.chenlu.base.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenlu.base.R;
import com.chenlu.base.adapter.MainListAdapter;
import com.chenlu.base.model.ConstantParams;
import com.chenlu.base.model.MainListModel;
import com.chenlu.base.model.SystemPhotoModel;
import com.chenlu.base.utils.ScreenUtils;
import com.chenlu.base.utils.SystemUtils;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示主页面
 */
public class MainFragment extends Fragment
{


    private AppBarLayout appBarLayout;
    private ImageView headImageView;
    private ImageView photoImageView;
    private Toolbar toolBar;
    private LinearLayout headLayout;
    private TextView titleTextView;
    private RecyclerView listView;
    private View.OnClickListener listener;
    //HeadImageView刚开始的时候所在的位置
    //HeadImageView刚开始时候所在的位置X
    private int headStartX=0;
    private int toolBarStartY=0;
    private int headFinalSize=0;
    private int headStartSize=0;
    //HeadImageView移动的时候半径的差
    private int headRadiusDis=0;
    private int titleXDis=0;
    private int titleYDis=0;
    private int titleLayoutYDis=0;
    private int titleLayoutHieght=0;

    public MainFragment()
    {

    }

    /**
     * 设置点击头像的时候执行的监听器
     * @param listener
     */
    public void setOnClickedListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        listView= (RecyclerView) view.findViewById(R.id.recycler_view);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appBar);
        headImageView= (ImageView) view.findViewById(R.id.img_head);
        toolBar= (Toolbar) view.findViewById(R.id.toolbar);
        photoImageView= (ImageView) view.findViewById(R.id.img_photo);
        headLayout= (LinearLayout) view.findViewById(R.id.ll_head);
        titleTextView= (TextView) view.findViewById(R.id.tv_main_title);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                initScrollInfo();
                changeChildInfo(verticalOffset);
            }
        });
        if (listener!=null)
        {
            headImageView.setOnClickListener(listener);
        }
        initList();

    }

    /**
     * 获取显示的数据
     * @return
     */
    private ArrayList<MainListModel> getListData()
    {
        ArrayList<MainListModel> list=new ArrayList<>();
        List<SystemPhotoModel> systemPhotoList = SystemUtils.getSystemPhotoList(getActivity());
        int size=systemPhotoList.size()/ ConstantParams.MAIN_LIST_COUNT;
        for (int i=0;i<size;i++)
        {
            int count=(i!=(size-1))?ConstantParams.MAIN_LIST_COUNT:ConstantParams.MAIN_LIST_COUNT+systemPhotoList.size()%ConstantParams.MAIN_LIST_COUNT;
            MainListModel model=new MainListModel();
            model.setAlbum(systemPhotoList.get(i*ConstantParams.MAIN_LIST_COUNT).getFilePath());
            model.setDescription(getString(R.string.app_name)+"0"+i);
            ArrayList<SystemPhotoModel> modelList=new ArrayList<>();
            for (int j=0;j<count;j++)
            {
                modelList.add(systemPhotoList.get(i*ConstantParams.MAIN_LIST_COUNT+j));
            }
            model.setPhotoList(modelList);
            list.add(model);

        }
        return list;

    }

    /**
     * 初始化显示列表的数据和控件
     */
    private void initList()
    {
        MainListAdapter adapter=new MainListAdapter(getActivity(),getListData());
        listView.setAdapter(adapter);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
//        listView.addItemDecoration();
        listView.setLayoutManager(manager);

    }
    private void initScrollInfo()
    {
        if (titleLayoutHieght==0)
        {
            toolBarStartY=toolBar.getTop();
            headImageView.getTop();
            headFinalSize= ScreenUtils.getActionSize(getContext())-getResources().getDimensionPixelSize(R.dimen.image_final_width);
            headStartSize = headImageView.getHeight();
            headRadiusDis=(headStartSize-headFinalSize)/2;
            headStartX= headImageView.getLeft()+headRadiusDis;
            titleXDis=titleTextView.getLeft()-ScreenUtils.getActionSize(getContext());
            titleYDis=titleTextView.getTop()-(ScreenUtils.getActionSize(getContext())-titleTextView.getHeight())/2;
            titleLayoutYDis=headLayout.getHeight()-ScreenUtils.getActionSize(getContext());
            titleLayoutHieght=headLayout.getPaddingBottom();
        }
    }

    /**
     * 改变子控件的信息
     * @param verticalOffset        竖直方向的偏移
     */
    private void changeChildInfo(int verticalOffset)
    {
        verticalOffset=Math.abs(verticalOffset);

        float scale=(float) verticalOffset/toolBarStartY;
        //改变头像的大小
        float currentSize = headStartSize - (headStartSize - headFinalSize) * scale;
        float scaleSize=currentSize/headStartSize;
        headImageView.setScaleY(scaleSize);
        headImageView.setScaleX(scaleSize);
        //改变Head位置X
        int xSize=headStartX-getResources().getDimensionPixelSize(R.dimen.image_final_width)/2;
//        headImageView.setTranslationX(0);
        headImageView.setTranslationX(-xSize*scale);
        //改变Head位置Y
        headImageView.setTranslationY(-headRadiusDis*scale);
        //改变字体的位置X
        titleTextView.setTranslationX(-titleXDis*scale);
        //改变字体的位置Y
        titleTextView.setTranslationY(-titleYDis*scale);
//        headLayout.invalidate();
        int padding=(int) (titleLayoutHieght-(titleLayoutYDis*scale));
        headLayout.setPadding(headLayout.getPaddingLeft(),headLayout.getPaddingTop(),headLayout.getPaddingRight(), padding);
    }

}
