package com.buaa.tezlikai.smartsh.utils;

import android.content.Context;

public class DensityUtils {
	/**
	 * dp转换成px像素
	 * @param dp
	 * @param ctx
	 * @return
	 */
	public static int dp2px(float dp, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;//获取屏幕的密度值
		// 4.1->4, 4.9->4
		int px = (int) (dp * density + 0.5f);// 加0.5可以四舍五入
		return px;
	}

	/**
	 * px像素转换成dp
	 * @param px
	 * @param ctx
	 * @return
	 */
	public static float px2dp(int px, Context ctx) {
		float density = ctx.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
