package com.buaa.tezlikai.smartsh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buaa.tezlikai.smartsh.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的listview
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

	private static final int STATE_PULL_TO_REFRESH = 1;// 下拉刷新
	private static final int STATE_RELEASE_TO_REFRESH = 2;// 松开刷新
	private static final int STATE_REFRESHING = 3;// 正在刷新

	// 下拉刷新头布局
	private View mHeaderView;
	//上拉刷新脚布局
	private View mFooterView;
	// 头布局高度
	private int mHeaderViewHeight;
	// 脚布局高度
	private int mFooterViewHeight;

	private int startY = -1;
	// 当前下拉刷新的状态
	private int mCurrentState = STATE_PULL_TO_REFRESH;// 默认是下拉刷新

	private TextView tvTitle;
	private ImageView ivArrow;
	private ProgressBar pbLoading;
	private TextView tvTime;

	private RotateAnimation animUp;// 箭头向上动画
	private RotateAnimation animDown;// 箭头向下动画

	private boolean isLoadingMore;//表示是否加载更多


	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();//添加脚布局
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();//添加脚布局

	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();//添加脚布局

	}

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.list_refresh_header,
				null);
		this.addHeaderView(mHeaderView);// 添加头布局

		// 隐藏头布局(1, 获取头布局高度, 2.设置负paddingTop,布局就会往上走)
		// int height = mHeaderView.getHeight();//此处无法获取高度,因为布局还没有绘制完成
		// 绘制之前就要获取布局高度
		mHeaderView.measure(0, 0);// 手动测量布局
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();// 测量之后的高度
		// 隐藏头布局
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);

		initAnim();
		setCurrentTime();// 设置初始时间
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView(){
		mFooterView = View.inflate(getContext(),R.layout.list_refresh_footer,null);
		this.addFooterView(mFooterView);//添加脚布局

		mFooterView.measure(0, 0);//手动测量布局
		mFooterViewHeight = mFooterView.getMeasuredHeight();//测量后的高度

		mFooterView.setPadding(0,-mFooterViewHeight,0,0);//隐藏脚布局
		//设置滑动监听
		this.setOnScrollListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 如果用户按住头条新闻向下滑动, 会导致listview无法拿到ACTION_DOWN,
								//因为此时事件会被交给父控件处理
								// 此时要重新获取startY
				startY = (int) ev.getY();
			}

			// 如果当前正在刷新, 什么都不做了
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getY();
			int dy = endY - startY;

			if (dy > 0 && getFirstVisiblePosition() == 0) {// 向下滑动&当前显示的是第一个item,才允许下拉刷新
				int paddingTop = dy - mHeaderViewHeight;// 计算当前的paddingtop值

				// 根据padding切换状态
				if (paddingTop >= 0
						&& mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// 切换到松开刷新
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (paddingTop < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					// 切换到下拉刷新
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				mHeaderView.setPadding(0, paddingTop, 0, 0);// 重新设置头布局padding
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 起始坐标归零

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// 如果当前是松开刷新, 就要切换为正在刷新
				mCurrentState = STATE_REFRESHING;
				// 显示头布局
				mHeaderView.setPadding(0, 0, 0, 0);

				refreshState();

				// 下拉刷新回调
				if (mListener != null) {
					mListener.onRefresh();
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);//让子正常滑动
	}

	/**
	 * 初始化箭头动画
	 */
	private void initAnim() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(500);
		animUp.setFillAfter(true);// 保持状态

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(500);
		animDown.setFillAfter(true);// 保持状态

		//设置滑动监听
		this.setOnScrollListener(this);
	}

	/**
	 * 根据当前状态刷新界面
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			// 箭头向下移动
			ivArrow.startAnimation(animDown);
			// 隐藏进度条
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("松开刷新");
			// 箭头向上移动
			ivArrow.startAnimation(animUp);
			// 隐藏进度条
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			pbLoading.setVisibility(View.VISIBLE);
			ivArrow.clearAnimation();// 必须清除动画,才能隐藏控件
			ivArrow.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}

	private OnRefreshListener mListener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		//下拉回调刷新
		mListener = listener;


	}

	/**
	 * 设置上次刷新时间
	 */
	private void setCurrentTime() {
		// 08:10 8:10 1
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH表示24小时制
		String time = format.format(new Date());
		tvTime.setText(time);
	}

	// 刷新完成
	public void onRefreshComplete(boolean success) {
		if (!isLoadingMore){
			// 隐藏头布局
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			mCurrentState = STATE_PULL_TO_REFRESH;
			// 隐藏进度条
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			tvTitle.setText("下拉刷新");

			// 刷新失败,不需要更新时间
			if (success) {
				setCurrentTime();
			}
		}else {
			//隐藏脚布局
			mFooterView.setPadding(0,-mFooterViewHeight,0,0);
			isLoadingMore = false;
		}
	}



	public interface OnRefreshListener {
		// 下拉刷新的回调
		public void onRefresh();

		//加载更多的回调
		public void loadMore();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE){//SCROLL_STATE_IDLE 处于空闲状态
			int lastVisiblePosition = getLastVisiblePosition();//当前界面显示的最后一个item的位置
			if (lastVisiblePosition >= getCount() -1 && !isLoadingMore){
				isLoadingMore = true;
//				System.out.print("到底了");
				//加载更多（到底了）
				//显示脚布局
				mFooterView.setPadding(0,0,0,0);

				setSelection(getCount() - 1);//跳到加载更多item的位置去展示

				if (mListener !=null){
					mListener.loadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	private OnItemClickListener mOnItemClickListener;

	//重写item点击方法，已消除子类因headerViewlistView中position的开始数值不是0问题
	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
		super.setOnItemClickListener(this);//将点击事件设置给当前的ReFreshListView
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mOnItemClickListener != null){
			//把TabDetailPager种listview设置为position以0开始
			mOnItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
		}
	}

}
