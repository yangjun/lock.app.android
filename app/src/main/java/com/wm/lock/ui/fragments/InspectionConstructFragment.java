package com.wm.lock.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.load.LoadApi;
import com.wm.lock.core.utils.FragmentUtils;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.Callable;

@EFragment
public class InspectionConstructFragment extends BaseFragment {

    @ViewById(R.id.tv_category)
    TextView mTvCategory;

    @ViewById(R.id.ll_container)
    LinearLayout mLlContainer;

    @ViewById(R.id.sv)
    ScrollView mSv;

    private int mIndex;
    private long mInspectionId;
    private String mCategory;
    private boolean mEnable;
    private List<InspectionItem> mItemList;

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_construct;
    }

    @Override
    protected void init() {
        final Bundle bundle = getArguments();
        mIndex = bundle.getInt(LockConstants.POS);
        mCategory = bundle.getString(LockConstants.DATA);
        mInspectionId = bundle.getLong(LockConstants.ID);
        mEnable = bundle.getBoolean(LockConstants.BOOLEAN);

        mTvCategory.setText(String.format("%s. %s", mIndex + 1, mCategory));
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

    private void renderer() {
        mLlContainer.removeAllViews();
        for (int i = 0, size = mItemList.size(); i < size; i++) {
            final InspectionItem item = mItemList.get(i);
            rendererItem(i, item);
        }

        final int lastPos = CacheManager.getInstance().getInt(LockConstants.POS, CacheManager.CHANNEL_PREFERENCE);
        mSv.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSv.scrollTo(0, lastPos);
            }
        }, 200);
    }

    private void rendererItem(int index, InspectionItem item) {
        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        final View view = inflater.inflate(R.layout.inflate_inspection_construct, null);
        mLlContainer.addView(view);
        view.setTag(item);

        // 名称
        final TextView tvName = (TextView) view.findViewById(R.id.tv_category);
        tvName.setText(String.format("%s %s", (mIndex + 1) + "." + (index + 1), item.getItem_name()));

        // 是否正常
        final RadioGroup rgNormal = (RadioGroup) view.findViewById(R.id.rg_normal);
        view.findViewById(R.id.rb_normal_yes).setTag(true);
        view.findViewById(R.id.rb_normal_no).setTag(false);
        rgNormal.check(item.getState() ? R.id.rb_normal_yes : R.id.rb_normal_no);
        rgNormal.setEnabled(mEnable);

        // 巡视情况/运行情况
        final EditText etResult = (EditText) view.findViewById(R.id.et_result);
        etResult.setText(item.getResult());
        etResult.setEnabled(mEnable);

        // 备注
        final EditText etNote = (EditText) view.findViewById(R.id.et_note);
        etNote.setText(item.getNote());
        etNote.setEnabled(mEnable);
        rendererUnNecessary(view, R.id.ll_add_note, R.id.ll_note, TextUtils.isEmpty(item.getNote()), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HardwareUtils.showKeyboard(mActivity, etNote);
            }
        });

        // 照片
        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, item.getId_());
        bundle.putBoolean(LockConstants.BOOLEAN, mEnable);
        final AttachPhotoFragment photoFragment = new AttachPhotoFragment_();
        photoFragment.setArguments(bundle);

        final int photoAreaId = 1000 + index;
        final View photoArea = view.findViewById(R.id.fl_photo_area);
        photoArea.setId(photoAreaId);
        FragmentUtils.replaceFragment(getChildFragmentManager(), photoAreaId, photoFragment);

        rendererUnNecessary(view, R.id.ll_add_photo, R.id.ll_photo, photoFragment.count(item.getId_()) <= 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFragment.takePhoto();
            }
        });
    }

    private IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    private void setVisible(View v, boolean visible) {
        v.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void rendererUnNecessary(View parent, int btnId, int containerId, boolean empty, final View.OnClickListener listener) {
        final View btn = parent.findViewById(btnId);
        final View container = parent.findViewById(containerId);
        if (mEnable) {
            setVisible(btn, empty);
            setVisible(container, !empty);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setVisible(container, true);
                    setVisible(btn, false);
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
        } else {
            setVisible(btn, false);
            setVisible(container, !empty);
        }
    }

    public void save() {
        // 记住打开的位置
        CacheManager.getInstance().putInt(LockConstants.POS, mSv.getScrollY(), CacheManager.CHANNEL_PREFERENCE);

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

    private void saveItem(View v) {
        final InspectionItem item = (InspectionItem) v.getTag();

        // 正常情況
        final RadioGroup rgNormal = (RadioGroup) v.findViewById(R.id.rg_normal);
        final RadioButton rbNormal = (RadioButton) rgNormal.findViewById(rgNormal.getCheckedRadioButtonId());
        item.setState(Boolean.valueOf(rbNormal.getTag().toString()));

        // 运行情况
        final EditText etRun = (EditText) v.findViewById(R.id.et_result);
        item.setResult(etRun.getText().toString().trim());

        // 备注
        final EditText etNote = (EditText) v.findViewById(R.id.et_note);
        item.setNote(etNote.getText().toString().trim());

        bizService().updateInspectionItem(item);
    }

}
