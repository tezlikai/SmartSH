package com.buaa.tezlikai.smartsh;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.buaa.tezlikai.smartsh.utils.DensityUtils;
import com.buaa.tezlikai.smartsh.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 新手引导
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    private int[] mImageIds = new int[] { R.mipmap.guide_1,
            R.mipmap.guide_2, R.mipmap.guide_3 };
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;//小红点
    private int mPointWidth;//圆点之间的宽度;
    private Button btnStart;//开始体验按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_pager);

        llContainer = (LinearLayout) findViewById(R.id.ll_container);

        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);

        //初始化ImageView
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i <mImageIds.length ; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view);

            //初始化圆点布局参数
            ImageView pointView = new ImageView(this);
            pointView.setImageResource(R.drawable.shape_circle_default);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i>0){
                params.leftMargin = DensityUtils.dp2px(8,this);//从第二个点开始设置边距
            }
            pointView.setLayoutParams(params);
            llContainer.addView(pointView);
        }
        mViewPager.setAdapter(new MyAdapter());

        //页面绘制结束之后，计算两个圆点的间距
        //视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            //layout方法执行结束（位置确定）
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                //移除监听
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取两个圆点的间距
                mPointWidth = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
            }
        });


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动过程的回调
                System.out.println("当前位置:" + position + ";偏移百分比:" + positionOffset);
                // 计算当前小红点的左边距
                int leftMargin = (int) (mPointWidth * positionOffset + position
                        * mPointWidth);

                // 修改小红点的左边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length-1){
                    btnStart.setVisibility(View.VISIBLE);
                }else {
                    btnStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                //开始体验
                //记录新手引导已经被展示的状态，下次启动时不再展示
                PrefUtils.putBoolean("is_guide_show", true, this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView view = new ImageView(getApplicationContext());
//            view.setBackgroundResource(mImageIds[position]);
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
