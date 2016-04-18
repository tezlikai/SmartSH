package com.buaa.tezlikai.smartsh.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能滑动的ViewPager
 * Created by Administrator on 2016/4/13.
 */
public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写ViewPager滑动事件，改为啥都不处理
        return true;
    }

   //决定事件是否中断
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截事件，让嵌套的子viewpager有机会响应触摸事件
    }
}
