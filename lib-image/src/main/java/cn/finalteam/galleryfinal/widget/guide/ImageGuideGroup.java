package cn.finalteam.galleryfinal.widget.guide;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.finalteam.galleryfinal.R;

/**
 * 
 * @author minWang
 * 
 * @description app第一次启动时的导航控件
 */
public class ImageGuideGroup extends FrameLayout {

	public static final int TYPE_BG = 1;
	public static final int TYPE_SRC = 2;

	private int[] mGuideIcons;
	private ViewPager mPager;
//	private CirclePageIndicator mIndicator;
	private OnClickListener mListener;
	private LayoutInflater mInflater;
	private boolean mIsVisible = true;
	private boolean canSkip;

	private int mDisplayType = TYPE_SRC;
	private CharSequence text;
	
	public ImageGuideGroup(Context context) {
		super(context);
		this.init();
	}

	public ImageGuideGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	private void init() {
		this.initWidget();
	}

	private void initWidget() {
		View parentView = LayoutInflater.from(getContext()).inflate(R.layout.gf_widget_guide, null);
		addView(parentView);
		
		mPager = (ViewPager) parentView.findViewById(R.id.pager);
//		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		
		mInflater = LayoutInflater.from(getContext());
	}

	private void initGuidePager() {
		mPager.setAdapter(new GuideAdapter());
//		mIndicator.setViewPager(mPager);
	}
	
	private void rendererBtn(Button btn) {
		btn.setText(text);
		btn.setVisibility(View.VISIBLE);
		btn.setOnClickListener(mListener);
	}
	
	/***********************  API *********************/
	
	/**
	 * 设置导航图片
	 */
	public void setGuideIcons(int[] icons) {
		this.mGuideIcons = icons;
		this.initGuidePager();
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
	public void isButtonVisible(boolean isVisible) {
		this.mIsVisible = isVisible;
	}

	public void setDisplayType(int type) {
		this.mDisplayType = type;
	}

	public void setCanSkip(boolean canSkip) {
		this.canSkip = canSkip;
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
			View mView = mInflater.inflate(R.layout.gf_widget_guide_item, null);
			
			// 设置图片
			ImageView iv = (ImageView) mView.findViewById(R.id.guide_imgview);
			if (mDisplayType == TYPE_SRC) {
				iv.setImageResource(mGuideIcons[position]);
			} else {
				iv.setBackgroundResource(mGuideIcons[position]);
			}

			// 设置按钮
			Button btn = (Button) mView.findViewById(R.id.guide_start_btn);
			btn.setVisibility(View.GONE);
			if (position == mCount - 1 && mIsVisible) {
				rendererBtn(btn);
			}

			// 跳过
			View vSkip = mView.findViewById(R.id.v_skip);
			if (vSkip != null) {
				if (canSkip) {
					vSkip.setOnClickListener(mListener);
					vSkip.setVisibility(VISIBLE);
				} else {
					vSkip.setVisibility(GONE);
				}
			}

			((ViewPager) container).addView(mView, 0);
			return mView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
