package cn.finalteam.galleryfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.widget.preview.ImagePreviewGroup;
import cn.finalteam.galleryfinal.widget.preview.ImagePreviewViewPager;
import cn.finalteam.toolsfinal.ActivityManager;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by wm on 15/12/10.
 */
public class ImagePreviewActivity extends ImageBaseActivity implements ImagePreviewViewPager.OnPageSelectedListener {

    private ImagePreviewGroup mPreviewGroup;
    protected List<ImageInfo> mPhotoList;
    protected int current;

    private ImageHandler.ImagePreviewConfig mPreviewConfig;
    private Handler mHandler = new Handler();

    private View mVTitleBar;
    private boolean showTitleBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            finish();
            return;
        }

        setContentView(R.layout.gf_activity_image_preview);

        setupViews();

        mPreviewConfig = ImageHandler.getInstance().getPreviewConfig();
        mPhotoList = mPreviewConfig.getInitList();
        if (mPhotoList == null || mPhotoList.isEmpty()) {
            onBackPressed();
            return;
        }

        mPreviewGroup = (ImagePreviewGroup) findViewById(R.id.ipg);
        mPreviewGroup.setOnSelectListener(this);
        mPreviewGroup.setOnImageViewTouchSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                mVTitleBar.setVisibility(showTitleBar ? View.GONE : View.VISIBLE);
                showTitleBar = !showTitleBar;
//                setActionBarVisible(!getSupportActionBar().isShowing());
            }
        });

        if (mPreviewConfig.getCurrent() > 0 && mPreviewConfig.getCurrent() < mPhotoList.size()) {
            current = mPreviewConfig.getCurrent();
        }
        bindData();
    }

    private void setupViews() {
        final View v = findViewById(R.id.v_statusbar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.height = getStatusBarHeight();
        v.setLayoutParams(params);

        final int alpha = 240;
        v.getBackground().setAlpha(alpha);
        if (mToolbar != null) {
            mToolbar.getBackground().setAlpha(alpha);
        }

        mVTitleBar = findViewById(R.id.ll);
    }

    protected void bindData() {
        mPreviewGroup.setData(mPhotoList);
        mPreviewGroup.select(current);
        updatePercent();
    }

    private void delete() {
        int position = mPreviewGroup.getCurrentPos();
        ImageInfo dPhoto = mPhotoList.get(position);

        ImageSelectActivity activity = (ImageSelectActivity) ActivityManager.getActivityManager().getActivity(ImageSelectActivity.class.getName());
        if (activity != null) {
            activity.deleteSelect(dPhoto.getId());
        }

        mPreviewGroup.remove(position);
        if (mPhotoList.size() == 0) {
            onBackPressed();
        } else {
            updatePercent();
        }
    }

    private void save() {
        if (ImageHandler.getInstance().getPreviewCallback() != null) {
            ImageHandler.getInstance().getPreviewCallback().onImagePreviewFinished(mPhotoList);
        }

        Intent it = new Intent();
        it.putExtra("data", (Serializable) mPhotoList);
        setResult(RESULT_OK, it);
        finish();
    }

    private int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getApplicationContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    @Override
    public void onBackPressed() {
        if (mPreviewConfig.isBackValid()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    save();
                }
            }, 300); // delay，否则可能列表刷新不过来，导致程序crash
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        current = arg0;
        updatePercent();
    }

    protected void updatePercent() {
        setTitle((current + 1) + "/" + mPhotoList.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            delete();
        }
        else if (item.getItemId() == R.id.finish) {
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gf_preview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean visible = mPreviewConfig.isCanEdit();
        menu.findItem(R.id.delete).setVisible(visible);

        if (!mPreviewConfig.isBackValid()) {
            menu.findItem(R.id.finish).setVisible(visible);
        } else {
            menu.findItem(R.id.finish).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

}
