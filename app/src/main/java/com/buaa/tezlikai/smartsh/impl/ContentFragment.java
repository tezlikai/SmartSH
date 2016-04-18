package com.buaa.tezlikai.smartsh.impl;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.buaa.tezlikai.smartsh.MainActivity;
import com.buaa.tezlikai.smartsh.R;
import com.buaa.tezlikai.smartsh.base.BaseFragment;
import com.buaa.tezlikai.smartsh.base.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 主页面Fragment
 * Created by Administrator on 2016/4/13.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;//通过注解实现findViewById()
    @ViewInject(R.id.rg_group)
    private RadioGroup rg_group;

    private ArrayList<BasePager> mPagers;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        ViewUtils.inject(this, view);//注入view事件
//        mViewPager = (ViewPager) view.findViewById(R.id.vp_pager);
        return view;
    }

    @Override
    public void initData(){
        //初始化5个标签页面
        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            //低栏标签切换监听
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        //首页
//                        mViewPager.setCurrentItem(0);
                        mViewPager.setCurrentItem(0, false);//禁用viewPager的动画效果
                        mPagers.get(0).initData();//切到当前页面在初始化数据
                        setSlidingMenuEnable(false);//禁用SlidingMenu
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1, false);
                        mPagers.get(1).initData();//切到当前页面在初始化数据
                        setSlidingMenuEnable(true);//开启SlidingMenu侧边栏效果
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2, false);
                        mPagers.get(2).initData();//切到当前页面在初始化数据
                        setSlidingMenuEnable(true);//开启SlidingMenu侧边栏效果
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3, false);
                        mPagers.get(3).initData();//切到当前页面在初始化数据
                        setSlidingMenuEnable(true);//开启SlidingMenu侧边栏效果
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4, false);
                        mPagers.get(4).initData();//切到当前页面在初始化数据
                        setSlidingMenuEnable(false);//禁用SlidingMenu
                        break;
                    default:
                        break;
                }
            }
        });
        mPagers.get(0).initData();//初始化首页的数据
        setSlidingMenuEnable(false);//禁用SlidingMenu
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            container.addView(pager.mRootView);//将页面布局添加到容器中
           // pager.initData();//初始化数据，此处尽量不要初始化，只有切到当前页面，才初始化数据，节省流量和性能
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
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
    /**
     * 设置新闻中页面
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPagers.get(1);
    }
}
