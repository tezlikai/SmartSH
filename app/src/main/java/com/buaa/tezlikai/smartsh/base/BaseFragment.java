package com.buaa.tezlikai.smartsh.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 * 1、初始化布局initView
 * 2、初始化数据initData
 * Created by Administrator on 2016/4/13.
 */
public abstract class BaseFragment extends Fragment{
    //就是activity就是MainActivity
    public Activity mActivity;

    //Fragment被创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();//获取所在的activity对象
    }

    //初始化Fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }


    //activity创建结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据，子类可以不实现
     */
    public void initData() {
    }

    /**
     * 初始化Fragment布局，子类必须实现
     * @return: view
     */
    public abstract View initView();

}
