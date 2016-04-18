package com.buaa.tezlikai.smartsh.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.buaa.tezlikai.smartsh.base.BasePager;

/**
 * 设置
 */
public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		tvTitle.setText("设置");
		btnMenu.setVisibility(View.GONE);//隐藏左上角menu菜单
		TextView view = new TextView(mActivity);
		view.setText("设置");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);
	}

}
