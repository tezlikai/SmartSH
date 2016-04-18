package com.buaa.tezlikai.smartsh.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.buaa.tezlikai.smartsh.base.BasePager;

/**
 * 首页
 * Created by Administrator on 2016/4/13.
 */
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("智慧上海");
        btnMenu.setVisibility(View.GONE);//首页不现实左上角menu菜单
        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        flContent.addView(view);
    }
}
