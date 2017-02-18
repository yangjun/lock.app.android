package com.wm.lock.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionItemFlag;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.Callable;

@EFragment
public abstract class InspectionConstructFragment_bak extends BaseFragment {

//    @ViewById(R.id.tv_category)
//    TextView mTvCategory;
//
//    @ViewById(R.id.ll_container)
//    LinearLayout mLlContainer;
//
//    @ViewById(R.id.sv)
//    ScrollView mSv;
//
//    private int mCategoryIndex;
//    private long mInspectionId;
//    private String mInspectionRoomName;
//    private String mCategory;
//    private boolean mEnable;
//    private List<InspectionItem> mItemList;
//
//    private boolean mHasRenderer = false;
//    private int mSelectOffset = 0;
//    private int mSelectIndex = -1;
//
//    private TemperatureHumidity mTemperatureHumidity;
//    private boolean mmTemperatureHumidityLoaded = false;
//    private Handler mHandler = new Handler();
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.frag_inspection_construct;
//    }
//
//    @Override
//    protected void init() {
//        final Bundle bundle = getArguments();
//        mCategoryIndex = bundle.getInt(LockConstants.POS);
//        mCategory = bundle.getString(LockConstants.DATA);
//        mInspectionId = bundle.getLong(LockConstants.ID);
//        mInspectionRoomName = bundle.getString(LockConstants.NAME);
//        mEnable = bundle.getBoolean(LockConstants.BOOLEAN);
//
//        mTvCategory.setText(String.format("%s. %s", mCategoryIndex + 1, mCategory));
//        loadData();
//    }
//
//    @Override
//    public void onPause() {
//        save();
//        super.onPause();
//    }
//
//    private void loadData() {
//        new LoadApi(mActivity).execute(new LoadApi.LoadCallBack<List<InspectionItem>>() {
//            @Override
//            public List<InspectionItem> onExecute() {
//                return bizService().listInspectionItemByCategory(mInspectionId, mCategory);
//            }
//
//            @Override
//            public void onSuccess(List<InspectionItem> result) {
//                mItemList = result;
//                renderer();
//            }
//
//            @Override
//            public int getContainerId() {
//                return R.id.fl_content;
//            }
//        });
//    }
//
//    private int getOffset(int index) {
//        final int childCount = mLlContainer.getChildCount();
//        if (childCount <= 0) {
//            return 0;
//        }
//
//        if (index < 0) {
//            index = 0;
//        }
//        else if (index >= childCount) {
//            index = childCount - 1;
//        }
//        final View v = mLlContainer.getChildAt(index);
//        if (v !=null) {
//            return v.getTop();
//        }
//        return 0;
//    }
//
//    private void scrollTo(final int offset) {
//        mSv.post(new Runnable() {
//            @Override
//            public void run() {
//                mSv.smoothScrollTo(0, offset);
//            }
//        });
//    }
//
//    private void renderer() {
//        mLlContainer.removeAllViews();
//        for (int i = 0, size = mItemList.size(); i < size; i++) {
//            final InspectionItem item = mItemList.get(i);
//            rendererItem(i, item);
//        }
//
//        mHasRenderer = true;
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int offset = 0;
//                if (mSelectIndex >= 0) {
//                    offset = getOffset(mSelectIndex);
//                } else if (mSelectOffset > 0) {
//                    offset = mSelectOffset;
//                }
//                scrollTo(offset);
//            }
//        }, 200);
//    }
//
//    private void rendererItem(int index, InspectionItem item) {
//        final LayoutInflater inflater = LayoutInflater.from(mActivity);
//        final View view = inflater.inflate(R.layout.inflate_inspection_construct, null);
//        mLlContainer.addView(view);
//        view.setTag(item);
//
//        // 名称
//        final TextView tvName = (TextView) view.findViewById(R.id.tv_category);
//        tvName.setText(String.format("%s %s", (mCategoryIndex + 1) + "." + (index + 1), item.getItem_name()));
//
//        // 是否正常
//        final RadioGroup rgNormal = (RadioGroup) view.findViewById(R.id.rg_normal);
//        final RadioButton rbNormalYes = (RadioButton) view.findViewById(R.id.rb_normal_yes);
//        final RadioButton rbNormalNo = (RadioButton) view.findViewById(R.id.rb_normal_no);
//        rbNormalYes.setTag(true);
//        rbNormalNo.setTag(false);
//        rgNormal.check(item.getState() ? R.id.rb_normal_yes : R.id.rb_normal_no);
//        rgNormal.setEnabled(mEnable);
//        rbNormalYes.setEnabled(mEnable);
//        rbNormalNo.setEnabled(mEnable);
//
//        // 巡视情况/运行情况
//        final EditText etResult = (EditText) view.findViewById(R.id.et_result);
//        etResult.setText(getDisplayResultString(item));
//        etResult.setEnabled(mEnable);
//
//        // 备注
//        final EditText etNote = (EditText) view.findViewById(R.id.et_note);
//        etNote.setText(item.getNote());
//        etNote.setEnabled(mEnable);
//        rendererUnNecessary(view, R.id.ll_add_note, R.id.ll_note, TextUtils.isEmpty(item.getNote()), new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HardwareUtils.showKeyboard(mActivity, etNote);
//            }
//        });
//
//        // 照片
//        final Bundle bundle = new Bundle();
//        bundle.putLong(LockConstants.ID, item.getId_());
//        bundle.putString(LockConstants.FLAG, AttachmentSource.INSPECTION_ITEM.name());
//        bundle.putBoolean(LockConstants.BOOLEAN, mEnable);
//        final AttachPhotoFragment photoFragment = new AttachPhotoFragment_();
//        photoFragment.setArguments(bundle);
//
//        final int photoAreaId = 1000 + index;
//        final View photoArea = view.findViewById(R.id.fl_photo_area);
//        photoArea.setId(photoAreaId);
//        FragmentUtils.replaceFragment(getChildFragmentManager(), photoAreaId, photoFragment);
//
//        final int attachmentCount = photoFragment.count(AttachmentSource.INSPECTION_ITEM, item.getId_());
//        rendererUnNecessary(view, R.id.ll_add_photo, R.id.ll_photo,  attachmentCount <= 0, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                photoFragment.takePhoto();
//            }
//        });
//    }
//
//    private IBizService bizService() {
//        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
//    }
//
//    private void setVisible(View v, boolean visible) {
//        v.setVisibility(visible ? View.VISIBLE : View.GONE);
//    }
//
//    private void rendererUnNecessary(View parent, int btnId, int containerId, boolean empty, final View.OnClickListener listener) {
//        final View btn = parent.findViewById(btnId);
//        final View container = parent.findViewById(containerId);
//        if (mEnable) {
//            setVisible(btn, empty);
//            setVisible(container, !empty);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setVisible(container, true);
//                    setVisible(btn, false);
//                    if (listener != null) {
//                        listener.onClick(v);
//                    }
//                }
//            });
//        } else {
//            setVisible(btn, false);
//            setVisible(container, !empty);
//        }
//    }
//
//    public void save() {
//        // 记住打开的位置
//        CacheManager.getInstance().putInt(LockConstants.POS + mInspectionId, mSv.getScrollY(), CacheManager.CHANNEL_PREFERENCE);
//
//        if (!mEnable) {
//            return;
//        }
//
//        final int childCount = mLlContainer.getChildCount();
//        if (childCount <= 0) {
//            return;
//        }
//
//        bizService().inTransaction(new Callable<Void>() {
//            @Override
//            public Void call() throws Exception {
//                for (int i = 0; i < childCount; i++) {
//                    final View v = mLlContainer.getChildAt(i);
//                    saveItem(v);
//                }
//                return null;
//            }
//        });
//    }
//
//    public void selectIndex(int index) {
//        if (mHasRenderer) {
//            scrollTo(getOffset(index));
//        }
//        else {
//            mSelectIndex = index;
//        }
//    }
//
//    public void selectOffset(int offset) {
//        if (mHasRenderer) {
//            scrollTo(offset);
//        }
//        else {
//            mSelectOffset = offset;
//        }
//    }
//
//    private void saveItem(View v) {
//        final InspectionItem item = (InspectionItem) v.getTag();
//
//        // 正常情況
//        final RadioGroup rgNormal = (RadioGroup) v.findViewById(R.id.rg_normal);
//        final RadioButton rbNormal = (RadioButton) rgNormal.findViewById(rgNormal.getCheckedRadioButtonId());
//        item.setState(Boolean.valueOf(rbNormal.getTag().toString()));
//
//        // 运行情况
//        final EditText etRun = (EditText) v.findViewById(R.id.et_result);
//        item.setResult(etRun.getText().toString().trim());
//        item.setIs_default_input(isResultInputDefault(item));
//
//        // 备注
//        final EditText etNote = (EditText) v.findViewById(R.id.et_note);
//        item.setNote(etNote.getText().toString().trim());
//
//        bizService().updateInspectionItem(item);
//    }
//
//    private boolean isResultInputDefault(InspectionItem item) {
//        if (item.getItem_flag() == InspectionItemFlag.NORMAL) {
//            return true;
//        }
//        if (!item.is_default_input()) {
//            return false;
//        }
//        return TextUtils.equals(item.getResult(), getDefaultTemperatureHumidityString(item));
//    }
//
//    private String getDisplayResultString(InspectionItem item) {
//        if (item.getItem_flag() == InspectionItemFlag.NORMAL) {
//            return item.getResult();
//        }
//
//        if (!item.is_default_input() && !TextUtils.isEmpty(item.getResult())) {
//            return item.getResult();
//        }
//
//        return getDefaultTemperatureHumidityString(item);
//    }
//
//    private String getDefaultTemperatureHumidityString(InspectionItem item) {
//        if (!mmTemperatureHumidityLoaded) {
//            mTemperatureHumidity = bizService().findTemperatureHumidityByRoomName(mInspectionRoomName, userInfo().getJobNumber());
//            mmTemperatureHumidityLoaded = true;
//        }
//
//        if (mTemperatureHumidity == null) {
//            return null;
//        }
//
//        switch (item.getItem_flag()) {
//            case InspectionItemFlag.TEMPERATURE:
//                return mTemperatureHumidity.getTemperature();
//
//            case InspectionItemFlag.HUMIDITY:
//                return mTemperatureHumidity.getHumidity();
//
//            case InspectionItemFlag.TEMPERATURE_HUMIDITY:
//                return String.format("%s, %s", mTemperatureHumidity.getTemperature(), mTemperatureHumidity.getHumidity());
//        }
//        return null;
//    }
//
//    private UserInfo userInfo() {
//        return ModuleFactory.getInstance().getModuleInstance(IUserService.class).getLoginedInfo();
//    }

}
