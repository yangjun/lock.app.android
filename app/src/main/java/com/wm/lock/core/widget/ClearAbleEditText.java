package com.wm.lock.core.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.wm.lock.R;
import com.wm.lock.core.utils.HardwareUtils;

public class ClearAbleEditText extends EditText {
	
	private Drawable imgEnable;
	private Drawable leftEnable;
	private Context context;
    private OnFocusChangeListener focusChangeListener;

    private Handler mHandler = new Handler();

    public ClearAbleEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public ClearAbleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }
    public ClearAbleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
                                                                                 
    private void init() {
        imgEnable = getCompoundDrawables()[2];
        if (imgEnable == null) {
            imgEnable = context.getResources().getDrawable(R.mipmap.ic_clear);
        }
        imgEnable.setBounds(0, 0, imgEnable.getMinimumWidth(), imgEnable.getMinimumHeight());
        leftEnable = getCompoundDrawables()[0];
        if (leftEnable != null) {
            leftEnable.setBounds(0, 0, leftEnable.getMinimumWidth(), leftEnable.getMinimumHeight());
        }
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        super.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setCompoundDrawablesWithIntrinsicBounds(leftEnable, null,
                        (length() == 0 || !hasFocus) ? null : imgEnable, null);
                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
        //默认隐藏清除按钮
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideClearIcon();
            }
        }, 50);
        setDrawable();
    }
                                                                                 
    /**
     * 设置删除图片
     */
    private void setDrawable() {
        if(length() == 0) {
            hideClearIcon();
        }else {
            showClearIcon();
        }
    }

    public void hideClearIcon() {
        setCompoundDrawablesWithIntrinsicBounds(leftEnable, null, null, null);
    }

    public void showClearIcon() {
        setCompoundDrawablesWithIntrinsicBounds(leftEnable, null, imgEnable, null);
    }

    /**
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(imgEnable != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX() ;
            //判断触摸点是否在水平范围内
            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&
                    (x < (getWidth() - getPaddingRight()));
            //获取删除图标的边界，返回一个Rect对象
            Rect rect = imgEnable.getBounds();
            //获取删除图标的高度
            int height = rect.height();
            int y = (int) event.getY();
            //计算图标底部到控件底部的距离
            int distance = (getHeight() - height) /2;
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            boolean isInnerHeight = (y > distance) && (y < (distance + height));
                                                                                         
            if(isInnerWidth && isInnerHeight) {
                setText("");
            }
        }
          
        if (event.getAction() == MotionEvent.ACTION_UP) {
        }
        return super.onTouchEvent(event);
    }
                                                                                 
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
     
    public void toPasswordType() {
		setTransformationMethod(PasswordTransformationMethod.getInstance());
	}
    
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
    	focusChangeListener = l;
    }
    
    @Override
    public void setText(CharSequence text, BufferType type) {
    	super.setText(text, type);
    	HardwareUtils.setCursorPos(this);
    }
    
}