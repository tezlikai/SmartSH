package com.buaa.tezlikai.smartsh.base;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏菜单详情页基类
 * Created by Administrator on 2016/4/14.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;

    //菜单详情页根布局
    public View mRootView;

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
