package com.buaa.tezlikai.smartsh.impl.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.buaa.tezlikai.smartsh.MainActivity;
import com.buaa.tezlikai.smartsh.R;
import com.buaa.tezlikai.smartsh.base.BaseMenuDetailPager;
import com.buaa.tezlikai.smartsh.domain.NewsMenuData.DataBean.ChildrenBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单详情页-新闻
 *
 * ViewPagerIndicator使用流程：
 * 1、引入Library库
 * 2、布局文件配置TabPageIndicator
 * 3、将指针和ViewPager关联起来
 * 4、重写getPageTitle方法，返回每个页面的标题（PagerAdapter）
 * 5、设置activity主题样式
 * 6、修改源码中的样式（改修图，文字颜色）
 *  Created by Administrator on 2016/4/14.
 */
public class NewsMenuDetailPager  extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.vp_news_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;//上面的指示栏

    private List<ChildrenBean> mTabList;//网络数据
    private ArrayList<TabDetailPager> mTabPagers;//页签页面集合

    public NewsMenuDetailPager(Activity mActivity, List<ChildrenBean> children) {
        super(mActivity);
        mTabList = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_news,null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //初始化12个标签
        mTabPagers = new ArrayList<TabDetailPager>();
        for (ChildrenBean tabData: mTabList){
            //创建一个页签
            TabDetailPager pager = new TabDetailPager(mActivity,tabData);
            mTabPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuAdapter());
//        mViewPager.setOnPageChangeListener(this);

        //此方法在viewPager设置完数据之后在调用，否则会报空指针
        mIndicator.setViewPager(mViewPager);//将页面指示器和viewpager关联起来
        mIndicator.setOnPageChangeListener(this);//当viewpager和指针绑定时，需要将页面切换监听给指针

    }

    /**
     * 设置侧边栏可用/不可用
     */
    private void setSlidingMenuEnable(boolean enable){
        MainActivity mainUI = (MainActivity) mActivity;//必须拿到MainActivity才能操作SlidingMenu
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            //禁用掉侧边栏滑动效果
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (position == 0){// 在第一个页签，允许侧边栏出现
                    //开启侧边栏
                    setSlidingMenuEnable(true);
        }else {//其他页签，禁用侧边栏，保证viewpager可以正常向右滑动
                    //关闭侧边栏
                    setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class NewsMenuAdapter extends PagerAdapter{

        //返回页面指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mTabPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();//初始化数据
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    @OnClick(R.id.iv_next_page)
    public void nextPage(View view){//通过xUtils中的注释实现，也可以用平常方法
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }
}
