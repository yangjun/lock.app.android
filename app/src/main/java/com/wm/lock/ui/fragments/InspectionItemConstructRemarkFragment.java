package com.wm.lock.ui.fragments;

import android.widget.EditText;
import android.widget.TextView;

import com.wm.lock.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class InspectionItemConstructRemarkFragment extends InspectionItemConstructFragment {

    @ViewById(R.id.tv_title)
    TextView mTvTitle;

    @ViewById(R.id.et_remark)
    EditText mEtRemark;

    @Override
    public void save() {
        mInspectionItem.setNote(mEtRemark.getText().toString().trim());
        bizService().updateInspectionItem(mInspectionItem);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_item_construct_remark;
    }

    @Override
    protected void init() {
        super.init();

        // 标题
        final String title = super.getTitle();
        mTvTitle.setText(title);

        // 备注
        mEtRemark.setText(mInspectionItem.getNote());
    }

}
