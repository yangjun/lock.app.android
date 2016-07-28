package cn.finalteam.galleryfinal.widget.preview;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.finalteam.galleryfinal.widget.MyImageViewTouch;

public class ImagePreviewViewPager extends ViewPager {
 
	private static final String TAG = "ImageViewTouchViewPager";

	public static final String VIEW_PAGER_OBJECT_TAG = "image#";

	private int previousPosition;

	private OnPageSelectedListener onPageSelectedListener;
 
	public ImagePreviewViewPager(Context context) {
		super(context);
		init();
	}
 
	public ImagePreviewViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
 
	public void setOnPageSelectedListener(OnPageSelectedListener listener) {
		onPageSelectedListener = listener;
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof MyImageViewTouch) {
			return ((MyImageViewTouch) v).canScroll(dx);
		} else {
			return super.canScroll(v, checkV, dx, x, y);
		}
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
		// 默认的，当选择第一项的时候也执行回调
		if (item == 0 && onPageSelectedListener != null) {
			onPageSelectedListener.onPageSelected(0);
		}
	}
	
	private void init() {
		previousPosition = getCurrentItem();

		setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (onPageSelectedListener != null) {
					onPageSelectedListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == SCROLL_STATE_SETTLING && previousPosition != getCurrentItem()) {
					try {
						MyImageViewTouch imageViewTouch = (MyImageViewTouch)findViewWithTag(VIEW_PAGER_OBJECT_TAG + previousPosition);
						if (imageViewTouch != null) {
							imageViewTouch.resetDisplay(new Matrix());
						}
						previousPosition = getCurrentItem();
					} catch (ClassCastException ex) {
						Log.e(TAG, "This view pager should have only ImageViewTouch as a children.", ex);
					}
				}
			}
		});
	}
	
	/**
	 * 刷新
	 */
	public void refresh() {
		getAdapter().notifyDataSetChanged();
		if (onPageSelectedListener != null) {
			onPageSelectedListener.onPageSelected(getCurrentItem());
		}
	}

	/**
	 * 前一张
	 */
	public void goPrevious() {
		if (isHasPrevious()) {
			setCurrentItem(getCurrentItem() - 1);
		}
	}

	/**
	 * 下一张
	 */
	public void goNext() {
		if (isHasNext()) {
			setCurrentItem(getCurrentItem() + 1);
		}
	}

	/**
	 * 是否有前一张
	 *
	 * @return
	 */
	public boolean isHasPrevious() {
		return isHasPos(true);
	}

	/**
	 * 是否有后一张
	 *
	 * @return
	 */
	public boolean isHasNext() {
		return isHasPos(false);
	}

	private boolean isHasPos(boolean isPrevious) {
		PagerAdapter adapter = getAdapter();
		if (null == adapter || adapter.getCount() == 0) {
			return false;
		}

		int currPos = getCurrentItem();
		return isPrevious ? currPos > 0 : currPos < adapter.getCount() - 1;
	}

	public interface OnPageSelectedListener {

		public void onPageSelected(int position);

	}
 
}