package com.wm.lock.core.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wm.lock.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author minWang
 * 
 * @description app第一次启动时的导航控件
 */
public class GuideGroup extends FrameLayout implements ViewPager.OnPageChangeListener {

	public static final int TYPE_BG = 1;
	public static final int TYPE_SRC = 2;

	private int[] mGuideIcons;
	private ViewPager mPager;
	private CirclePageSpaceIndicator mIndicator;
	private OnClickListener mListener;
	private LayoutInflater mInflater;
	private boolean mIsVisible = true;

	private int mDisplayType = TYPE_SRC;
	private CharSequence text;
	private long mAutoInterval;

	private GuideTimer mGuideTimer;

	public GuideGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopTimer();
	}

	private void init() {
		this.initWidget();
	}

	private void initWidget() {
		View parentView = LayoutInflater.from(getContext()).inflate(R.layout.widget_guide, null);
		addView(parentView);
		
		mPager = (ViewPager) parentView.findViewById(R.id.pager);
		mPager.addOnPageChangeListener(this);
		mIndicator = (CirclePageSpaceIndicator)findViewById(R.id.indicator);
		
		mInflater = LayoutInflater.from(getContext());
	}

	private void initGuidePager() {
		mPager.setAdapter(new GuideAdapter());
		mIndicator.setViewPager(mPager);
	}

	/***********************  API *********************/
	
	/**
	 * 设置导航图片
	 */
	public void setGuideIcons(int[] icons) {
		this.mGuideIcons = icons;
		this.initGuidePager();
		startTimer();
	}

	/**
	 * 设置按钮文字
	 */
	public void setText(CharSequence text) {
		this.text = text;
	}
	
	/**
	 * 设置点击按钮的监听
	 * 
	 * @param mListener
	 */
	public void setOnStartAppClickListener(OnClickListener mListener) {
		this.mListener = mListener;
	}
	
	/**
	 * 按钮是否可见
	 * 
	 * @param isVisible true表示可见
	 */
	public void setButtonVisible(boolean isVisible) {
		this.mIsVisible = isVisible;
	}

	public void setDisplayType(int type) {
		this.mDisplayType = type;
	}

	public void setAutoNextInterval(long interval) {
		this.mAutoInterval = interval;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		startTimer();
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	private class GuideAdapter extends android.support.v4.view.PagerAdapter {

		private int mCount = 0;
		
		public GuideAdapter() {
			this.mCount = mGuideIcons.length;
		} 
		
		@Override
		public int getCount() {
			return mCount;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == (View) obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View mView = mInflater.inflate(R.layout.widget_guide_item, null);
			
			// 设置图片
			ImageView iv = (ImageView) mView.findViewById(R.id.guide_imgview);
			if (mDisplayType == TYPE_SRC) {
				iv.setImageResource(mGuideIcons[position]);
			} else {
				iv.setBackgroundResource(mGuideIcons[position]);
			}

			// 设置按钮
			Button btn = (Button) mView.findViewById(R.id.guide_start_btn);
			btn.setText(text);
			btn.setOnClickListener(mListener);

			// 跳过
			View vSkip = mView.findViewById(R.id.v_skip);
			if (vSkip != null) {
				vSkip.setOnClickListener(mListener);
			}

			// 返回
			View vBack = mView.findViewById(R.id.v_back);
			if (vBack != null) {
				vBack.setOnClickListener(mListener);
			}

			if (mIsVisible) {
				vBack.setVisibility(GONE);
				vSkip.setVisibility(VISIBLE);
				btn.setVisibility(position == mCount - 1 ? View.VISIBLE : View.GONE);
			}
			else {
				vBack.setVisibility(VISIBLE);
				vSkip.setVisibility(GONE);
				btn.setVisibility(View.GONE);
			}

			((ViewPager) container).addView(mView, 0);
			return mView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	private void startTimer() {
		if (mAutoInterval > 0) {
			getGuideTimer().start();
		}
	}

	private void stopTimer() {
		if (mAutoInterval > 0) {
			getGuideTimer().stop();
		}
	}

	private GuideTimer getGuideTimer() {
		if (mGuideTimer == null) {
			mGuideTimer = new GuideTimer();
		}
		return mGuideTimer;
	}

	private class GuideTimer {

		private Timer mTimer;
		private TimerTask mTimerTask;

		private Runnable runnable = new Runnable() {
			@Override
			public void run() {
				mPager.setCurrentItem(mPager.getCurrentItem() + 1);
			}
		};

		public void start() {
			stop();
			if (mPager.getCurrentItem() >= mGuideIcons.length - 1) {
				return;
			}

			mTimer = new Timer();
			mTimerTask  = new TimerTask() {
				@Override
				public void run() {
					stop();
					post(runnable);
				}
			};
			mTimer.schedule(mTimerTask, mAutoInterval, mAutoInterval);
		}

		public void stop() {
			if (mTimer != null) {
				mTimer.cancel();
				mTimer.purge();
			}
			if (mTimerTask != null) {
				mTimerTask.cancel();
			}
			mTimerTask = null;
			mTimer = null;
		}
	}

}
