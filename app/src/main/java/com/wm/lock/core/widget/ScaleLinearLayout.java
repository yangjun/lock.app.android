package com.wm.lock.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ScaleLinearLayout extends LinearLayout {

	private float mScale;

	public ScaleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public ScaleLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		String namespace = "http://schemas.android.com/apk/res/android";
		mScale = attrs.getAttributeFloatValue(namespace, "layout_weight", 1.0f);
	}

	@SuppressWarnings("unused")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
				(int) (MeasureSpec.getSize(widthMeasureSpec) * mScale),
				MeasureSpec.getMode(widthMeasureSpec)));
	}

	public void setScale(float scale) {
		mScale = scale;
	}

}
