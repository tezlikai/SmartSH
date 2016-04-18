package com.buaa.tezlikai.smartsh;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.buaa.tezlikai.smartsh.impl.ContentFragment;
import com.buaa.tezlikai.smartsh.impl.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_CONTENT = "TAG_CONTENT";
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加侧边栏
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        //全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //屏幕预留400像素
        slidingMenu.setBehindOffset(400);

        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();//兼容性好一些
        FragmentTransaction transaction = fm.beginTransaction();//开始事务

        //将帧布局替换为对应的Fragment
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.replace(R.id.fl_content, new ContentFragment(), TAG_CONTENT);
        transaction.commit();//提交事务
    }


    /**
     * 获取侧边栏对象
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();//兼容性好一些
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取主页对象
     * @return
     */
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();//兼容性好一些
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
