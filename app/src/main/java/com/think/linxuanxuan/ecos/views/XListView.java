package com.think.linxuanxuan.ecos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.RefleshTimeManager;


public class XListView extends ListView implements OnScrollListener {

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;

	/*** 最近一次刷新时间*/
	private long mLastRefleshTime = 0;

	private TextView mHeaderTimeView;

	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private TextView mContentView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
	// at bottom, trigger
	// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
	// feature.




	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);

		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);


		//		mHeaderView.setBackgroundResource(R.drawable.bg_bottom);
		addHeaderView(mHeaderView);

		this.setHeaderDividersEnabled(false);
		this.setFooterDividersEnabled(false);

		// init footer view
		mFooterView = new XListViewFooter(context);

		mContentView = (TextView) mFooterView.findViewById(R.id.xlistview_footer_hint_textview);

		//		mFooterView.setBackgroundResource(R.drawable.bg_top);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 *
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 *
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {

		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {

			mPullRefreshing = false;
			resetHeaderHeight();

			updateLastRefleshTime();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	RefleshTimeManager mRefleshTimeManager;
	/***
	 * 初始化刷新管理
	 * @param name
	 */
	public void initRefleshTime(String name)
	{
		mRefleshTimeManager  = new RefleshTimeManager(getContext(), name);
	}

	/**
	 * set last refresh time
	 *
	 * @param time
	 */
	public void setRefreshTime(String time) {

	}

	/*public void initRefleshTime(){
		mLastRefleshTime = System.currentTimeMillis();
	}*/

	/***
	 * 刷新更新时间，在此之前必须已经调用{@link #initRefleshTime}
	 */
	public void updateLastRefleshTimeDisplay(){


		if(mRefleshTimeManager==null || mRefleshTimeManager.getRefleshTime()==0){
			mHeaderTimeView.setText("");
			//隐藏上次刷新时间
			mHeaderView.findViewById(R.id.lly_header_time).setVisibility(View.GONE);
		}
		else{
			Long lastRefleshTime = mRefleshTimeManager.getRefleshTime();
			//显示上次刷新时间
			mHeaderView.findViewById(R.id.lly_header_time).setVisibility(View.VISIBLE);
			mHeaderTimeView.setText(getPassedTime(lastRefleshTime));
		}

	}

	/***
	 * 刷新更新时间，在此之前必须已经调用{@link #initRefleshTime}
	 */
	private void updateLastRefleshTime(){

		mRefleshTimeManager.updateRefleshTime(System.currentTimeMillis());
	}

	/***
	 * 返回从lastRefleshTime到现在为止的时间，刚刷新或几分钟前或几小时前或几天前
	 * @param lastRefleshTime
	 * @return
	 */
	public String getPassedTime(Long lastRefleshTime){

		long intervals = ((System.currentTimeMillis() - lastRefleshTime)/1000);

		int hour = (int) ((intervals%(24*60*60))/(60*60));
		int minute =  (int) ((intervals%(60*60))/60);
		int seconds =  (int) (intervals%60);


		//		Log.d(TAG,day + "天    " + hour + "小时    " + minute + "分钟");
		String passedTime = "";

		if(hour!=0){
			passedTime = passedTime + hour+"小时" + minute+"分" + seconds+"秒";
		}
		else{
			if(minute!=0){
				passedTime = passedTime + minute+"分" + seconds+"秒";
			}
			else{
				passedTime = passedTime + seconds+"秒";
			}
		}

		passedTime = passedTime + "前刷新";
		if(minute == seconds){
			passedTime = "刚刷新";
		}

		return passedTime;
	}


	/***
	 * 设置{@link #mContentView}没有更多信息
	 */
	/*public void nofityNoMoreData(){
		mContentView.setText("没有更多信息了");
	}*/

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
				// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	/****
	 * 列表为空时不能进行上拉
	 */
	private void startLoadMore() {

		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {

			mFooterView.show();
			mListViewListener.onLoadMore();

		}
	}

	private boolean isRefleshing = false;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();

				isRefleshing = false;
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				//			System.out.println("数据监测：" + getFirstVisiblePosition() + "---->"
				//					+ getLastVisiblePosition());
				//下拉刷新

				//上拉加载
				Log.e("XListView", "getLastVisiblePosition():" + getLastVisiblePosition());
				Log.e("XListView", "mTotalItemCount-1:" + (mTotalItemCount - 1));
				Log.e("XListView", "mFooterView.getBottomMargin():" + (mFooterView.getBottomMargin()));
				Log.e("XListView", "deltaY:" + (deltaY));
				if (getFirstVisiblePosition() == 0
						&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();

					if(!isRefleshing)
					{
						updateLastRefleshTimeDisplay();
						isRefleshing = true;
					}
				}
				else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
					// last item, already pulled up or want to pull up.

					//				Log.e("XListView", "getChildCount():"+getChildCount());
					if(getChildCount()<=2)
					{
						return true;
					}
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
				break;
			default:
				mLastY = -1; // reset
				if (getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh
							&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					resetHeaderHeight();
				}
				if (getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad
							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						startLoadMore();
					}
					resetFooterHeight();
				}
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}



	/***
	 * 列表刷新时间管理类
	 * @ClassName: RefleshTimeManager
	 * @Description: TODO(通过name标志不同刷新列表)
	 * @author enlizhang
	 * @date 2015年4月20日 上午10:02:08
	 *
	 */
	/*public static class RefleshTimeManager{

		private final static String TAG = "RefleshTimeManager";
		
		private final static String PREFERENCE_NAME = "RefleshTimeManager";
		private final static int READ_MODE = Context.MODE_WORLD_READABLE;
		private final static int WRITE_MODE = Context.MODE_WORLD_WRITEABLE;
		
		
		private final static String NEW_APPLY_REFLESH_TIME = "newApplyRefleshTime";
		
		private  AccountDataService singleRefleshTimeManager = null;
		
		private Context mContext;
		
		*//*** 存储的键指值 *//*
		private String mName;
		
		private RefleshTimeManager(Context context, String _name)
		{
			mContext = context;
			mName = _name;
		}
		
		*//****
	 * 根据{@link #mName}更新刷新时间
	 * @param time 新的刷新时间
	 *//*
		public void updateRefleshTime(Long time)
		{
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			
			editor.putLong(mName, time);
			editor.commit();
			
		}
		
		*//***
	 * 根据{@link #mName}获取上次刷新时间
	 * @return
	 *//*
		public Long getRefleshTime()
		{
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME,READ_MODE);
			
			return sharedPreferences.getLong(mName, 0);
		}
		
		public static void clearAll(Context currentContext)
		{
			SharedPreferences sharedPreferences = currentContext.getSharedPreferences(PREFERENCE_NAME,WRITE_MODE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			
			editor.clear();
			editor.commit();
		}
	}*/
}
