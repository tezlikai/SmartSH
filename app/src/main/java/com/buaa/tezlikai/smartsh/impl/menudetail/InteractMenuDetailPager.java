package com.buaa.tezlikai.smartsh.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.buaa.tezlikai.smartsh.base.BaseMenuDetailPager;

/**
 * 菜单详情页-互动
 * Created by Administrator on 2016/4/14.
 */
public class InteractMenuDetailPager extends BaseMenuDetailPager{

    public InteractMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-互动");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
