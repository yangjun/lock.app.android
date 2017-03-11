package com.wm.lock.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.load.LoadApi;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.DataUtils;
import com.wm.lock.core.utils.FragmentUtils;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionItemFlag;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.wm.lock.R.id.fl;

@EFragment
public class InspectionConstructFragment extends BaseFragment {

    @ViewById(R.id.tv_category)
    TextView mTvCategory;

    @ViewById(R.id.ll_container)
    LinearLayout mLlContainer;

    @ViewById(R.id.sv)
    ScrollView mSv;

    private int mCategoryIndex;
    private long mInspectionId;
    private String mCategory;
    private boolean mEnable;
    private boolean mLast;
    private List<InspectionItem> mItemList;
    private List<InspectionItemConstructFragment> mItemFragmentList;

    private boolean mHasRenderer = false;
    private int mSelectOffset = 0;
    private int mSelectIndex = -1;

    private Handler mHandler = new Handler();

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_construct;
    }

    @Override
    protected void init() {
        final Bundle bundle = getArguments();
        mCategoryIndex = bundle.getInt(LockConstants.POS);
        mCategory = bundle.getString(LockConstants.DATA);
        mInspectionId = bundle.getLong(LockConstants.ID);
        mEnable = bundle.getBoolean(LockConstants.BOOLEAN);
        mLast = bundle.getBoolean(LockConstants.FLAG);

        mTvCategory.setText(String.format("%s. %s", mCategoryIndex + 1, mCategory));
        loadData();
    }

    @Override
    public void onPause() {
        save();
        super.onPause();
    }

    private void loadData() {
        new LoadApi(mActivity).execute(new LoadApi.LoadCallBack<List<InspectionItem>>() {
            @Override
            public List<InspectionItem> onExecute() {
                return bizService().listInspectionItemByCategory(mInspectionId, mCategory);
            }

            @Override
            public void onSuccess(List<InspectionItem> result) {
                mItemList = result;
                renderer();
            }

            @Override
            public int getContainerId() {
                return R.id.fl_content;
            }
        });
    }

    private int getOffset(int index) {
        final int childCount = mLlContainer.getChildCount();
        if (childCount <= 0) {
            return 0;
        }

        if (index < 0) {
            index = 0;
        }
        else if (index >= childCount) {
            index = childCount - 1;
        }
        final View v = mLlContainer.getChildAt(index);
        if (v !=null) {
            return v.getTop();
        }
        return 0;
    }

    private void scrollTo(final int offset) {
        mSv.post(new Runnable() {
            @Override
            public void run() {
                mSv.smoothScrollTo(0, offset);
            }
        });
    }

    private void renderer() {
        mLlContainer.removeAllViews();
        mItemFragmentList = new ArrayList<>();

        for (int i = 0, size = mItemList.size(); i < size; i++) {
            final InspectionItem item = mItemList.get(i);
            rendererItem(i, item);
        }

        if (mLast) {
            rendererPhoto();
        }

        mHasRenderer = true;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int offset = 0;
                if (mSelectIndex >= 0) {
                    offset = getOffset(mSelectIndex);
                } else if (mSelectOffset > 0) {
                    offset = mSelectOffset;
                }
                scrollTo(offset);
            }
        }, 200);
    }

    private void rendererItem(int index, InspectionItem item) {
        final int containerId = 10000 + index;

        final FrameLayout fl = new FrameLayout(mActivity);
        fl.setId(containerId);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = mActivity.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        mLlContainer.addView(fl, params);

        InspectionItemConstructFragment fragment = null;
        switch (item.getItem_flag()) {
            case InspectionItemFlag.CONCLUSION:
                fragment = InspectionItemConstructConclusionFragment_.builder().build();
                break;

            case InspectionItemFlag.REMARK:
                fragment = InspectionItemConstructRemarkFragment_.builder().build();
                break;

            default:
                fragment = InspectionItemConstructNormalFragment_.builder().build();
                break;
        }

        final Bundle bundle = new Bundle();
        bundle.putSerializable(LockConstants.DATA, item);
        bundle.putBoolean(LockConstants.BOOLEAN, mEnable);
        bundle.putInt(LockConstants.INDEX, mCategoryIndex);
        bundle.putInt(LockConstants.POS, index);
        fragment.setMyArguments(bundle);
        FragmentUtils.replaceFragment(getChildFragmentManager(), containerId, fragment);

        mItemFragmentList.add(fragment);
    }

    private void rendererPhoto() {
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.frag_inspection_item_construct_photo, null);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLlContainer.addView(view, params);

        final int id = DataUtils.generateViewId();
        view.findViewById(R.id.fl_photos).setId(id);

        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, mInspectionId);
        bundle.putString(LockConstants.FLAG, AttachmentSource.INSPECTION.name());
        bundle.putBoolean(LockConstants.BOOLEAN, mEnable);
        final AttachPhotoFragment photoFragment = new AttachPhotoFragment_();
        photoFragment.setArguments(bundle);

        FragmentUtils.replaceFragment(getChildFragmentManager(), id, photoFragment);
    }

    private IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    public void save() {
        // 记住打开的位置
        CacheManager.getInstance().putInt(LockConstants.POS + mInspectionId, mSv.getScrollY(), CacheManager.CHANNEL_PREFERENCE);

        if (!mEnable) {
            return;
        }

        final int childCount = mLlContainer.getChildCount();
        if (childCount <= 0) {
            return;
        }

        bizService().inTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = 0; i < childCount; i++) {
                    final View v = mLlContainer.getChildAt(i);
                    saveItem(v);
                }
                return null;
            }
        });
    }

    public void selectIndex(int index) {
        if (mHasRenderer) {
            scrollTo(getOffset(index));
        }
        else {
            mSelectIndex = index;
        }
    }

    public void selectOffset(int offset) {
        if (mHasRenderer) {
            scrollTo(offset);
        }
        else {
            mSelectOffset = offset;
        }
    }

    private void saveItem(View v) {
        if (!CollectionUtils.isEmpty(mItemFragmentList)) {
            for (InspectionItemConstructFragment fragment : mItemFragmentList) {
                fragment.save();
            }
        }
    }

}
