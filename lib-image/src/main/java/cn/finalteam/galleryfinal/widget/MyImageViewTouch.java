package cn.finalteam.galleryfinal.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by Administrator on 2016/3/10.
 */
public class MyImageViewTouch extends ImageViewTouch {

    public MyImageViewTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageViewTouch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canScroll() {
        return super.canScroll();
    }

    @Override
    public boolean canScroll(int direction) {
        RectF bitmapRect = this.getBitmapRect();
        this.updateRect(bitmapRect, this.mScrollPoint);
        Rect imageViewRect = new Rect();
        this.getGlobalVisibleRect(imageViewRect);

        if(bitmapRect == null) {
            return false;
        }
        else if(bitmapRect.right > (float)imageViewRect.right && direction < 0) {
            return Math.abs(bitmapRect.right - (float)imageViewRect.right) > 0.0F;
        }
        else if(bitmapRect.left < 0 && direction > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void resetDisplay(Matrix matrix) {
        if(matrix != null) {
            this.mNextMatrix = new Matrix(matrix);
        }
        super.resetDisplay();
    }

}
