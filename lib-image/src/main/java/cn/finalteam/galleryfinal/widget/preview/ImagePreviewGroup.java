package cn.finalteam.galleryfinal.widget.preview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.utils.ImageUtils;
import cn.finalteam.galleryfinal.widget.MyImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by wm on 15/9/7.
 */
public class ImagePreviewGroup<T> extends FrameLayout {

    private ImagePreviewViewPager mPager;
    private ImagePagerAdapter mAdapter;
    private List<T> mList;
    private ItemViewCreator mItemViewCreator;
    private ImageViewTouch.OnImageViewTouchSingleTapListener mSingleTapListener;

    public ImagePreviewGroup(Context context) {
        super(context);
        init();
    }

    public ImagePreviewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImagePreviewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImagePreviewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.gf_widget_preview_group, this);
        mPager = (ImagePreviewViewPager) findViewById(R.id.advp);
    }

    public void setData(T item) {
        if (item != null) {
            List<T> list = new ArrayList<>();
            list.add(item);
            setData(list);
        }
    }

    public void setData(List<T> list) {
        mList = list;
        mAdapter = new ImagePagerAdapter(getContext(), list);
        mPager.setAdapter(mAdapter);
    }

    public void select(int pos) {
        mPager.setCurrentItem(pos);
    }

    public void remove(int pos) {
        mList.remove(pos);
        mAdapter.notifyDataSetChanged();
    }

    public void setOnSelectListener(ImagePreviewViewPager.OnPageSelectedListener listener) {
        mPager.setOnPageSelectedListener(listener);
    }

    public void setItemViewCreator(ItemViewCreator creator) {
        this.mItemViewCreator = creator;
    }

    public int getCurrentPos() {
        return mPager.getCurrentItem();
    }

    public void setOnImageViewTouchSingleTapListener( ImageViewTouch.OnImageViewTouchSingleTapListener listener) {
        this.mSingleTapListener = listener;
    }

    public void rendererImageViewTouch(final MyImageViewTouch imageView, final View loadingView, int position) {
        imageView.setSingleTapListener(mSingleTapListener);
        imageView.setTag(ImagePreviewViewPager.VIEW_PAGER_OBJECT_TAG + position);

        String mImageUrl = mList.get(position).toString();

        ImageUtils.loadImage(getContext(), mImageUrl, imageView, ImageUtils.getImageOptions(-1),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        showLoadingView();
                        super.onLoadingStarted(imageUri, view);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        showImageView();
                        super.onLoadingFailed(imageUri, view, failReason);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (null != loadedImage && !loadedImage.isRecycled()) {
                            imageView.setImageBitmap(loadedImage, new Matrix(), -1.0f, -1.0f);
                        }
                        showImageView();
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }

                    private void showImageView() {
                        imageView.setVisibility(View.VISIBLE);
                        loadingView.setVisibility(View.GONE);
                    }

                    private void showLoadingView() {
                        imageView.setVisibility(View.GONE);
                        loadingView.setVisibility(View.VISIBLE);
                    }

                }
        );
    }

    private ItemViewCreator getDefaultItemCreator() {
        return new ItemViewCreator() {
            @Override
            public View getItemView(ViewGroup container, int position) {
                View parent = LayoutInflater.from(getContext()).inflate(R.layout.gf_widget_preview_item, null);

                final View mLoadingView = parent.findViewById(R.id.loading);
                final MyImageViewTouch mImageView = (MyImageViewTouch) parent.findViewById(R.id.image);
                rendererImageViewTouch(mImageView, mLoadingView, position);

                ((ViewPager) container).addView(parent, 0);

                return parent;
            }
        };
    }

    private class ImagePagerAdapter<T> extends android.support.v4.view.PagerAdapter {

        private final String TAG = ImagePagerAdapter.class.getSimpleName();

        private Context ctx;
        private List<T> list;

        private float minScale;
        private float maxScale;

//        private ImageLoader imageLoader;
//        private DisplayImageOptions options;

        public ImagePagerAdapter(Context ctx, List<T> list) {
            this(ctx, list, 0.5f, 2.0f);
        }

        public ImagePagerAdapter(Context ctx, List<T> list, float minScale, float maxScale) {
            this.ctx = ctx;
            this.list = list;
            this.minScale = minScale;
            this.maxScale = maxScale;

//            imageLoader = ImageLoader.getInstance();
//            options = ImageUtils.getImageOptions(-1);
        }

        @Override
        public int getCount() {
            return null == list ? 0 : list.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == (View) obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mItemViewCreator == null) {
                mItemViewCreator = getDefaultItemCreator();
            }
            return mItemViewCreator.getItemView(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public static interface ItemViewCreator {
        public View getItemView(ViewGroup container, int position);
    }

}
