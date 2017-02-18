package com.wm.lock.ui.fragments;

import android.text.TextUtils;
import android.widget.RadioButton;

import com.wm.lock.LockConstants;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EFragment;

@EFragment
public abstract class InspectionItemConstructFragment extends BaseFragment {

    protected InspectionItem mInspectionItem;
    protected boolean mEnable;
    protected int mCategoryIndex;
    protected int mIndex;

    @Override
    protected void init() {
        mInspectionItem = (InspectionItem) mArguments.getSerializable(LockConstants.DATA);
        mEnable = mArguments.getBoolean(LockConstants.BOOLEAN);
        mCategoryIndex = mArguments.getInt(LockConstants.INDEX);
        mIndex = mArguments.getInt(LockConstants.POS);
    }

    public abstract void save();

    protected String getTitle() {
        final int categoryIndex = mCategoryIndex + 1;
        final int index = mIndex + 1;
        String title =  String.format("%s.%s %s", categoryIndex, index, mInspectionItem.getItem_name());
        if (!TextUtils.isEmpty(mInspectionItem.getItem_standard())) {
            title = String.format("%s(%s)", title, mInspectionItem.getItem_standard());
        }
        return title;
    }

    protected void setupRadioButton(int id, int value, int selectValue) {
        final RadioButton radioButton = (RadioButton) findViewById(id);
        radioButton.setTag(value);
        radioButton.setChecked(selectValue == value);
        radioButton.setEnabled(mEnable);
    }

    protected IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

}
