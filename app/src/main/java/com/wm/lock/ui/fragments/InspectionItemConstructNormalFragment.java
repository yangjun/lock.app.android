package com.wm.lock.ui.fragments;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.entity.InspectionItemRadioOption;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class InspectionItemConstructNormalFragment extends InspectionItemConstructFragment {

    @ViewById(R.id.tv_title)
    TextView mTvTitle;

    @ViewById(R.id.rg_normal)
    RadioGroup mRgNormal;

    @Override
    public void save() {
        final RadioButton rbNormal = (RadioButton) mRgNormal.findViewById(mRgNormal.getCheckedRadioButtonId());
        mInspectionItem.setState(Integer.valueOf(rbNormal.getTag().toString()));
        bizService().updateInspectionItem(mInspectionItem);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_item_construct_normal;
    }

    @Override
    protected void init() {
        super.init();

        // 标题
        final String title = super.getTitle();
        mTvTitle.setText(title);

        // 作业标准
        setupRadioButton(R.id.rb_normal_yes, InspectionItemRadioOption.NORMAL_Y, mInspectionItem.getState());
        setupRadioButton(R.id.rb_normal_no, InspectionItemRadioOption.NORMAL_N, mInspectionItem.getState());
        setupRadioButton(R.id.rb_normal_not_support, InspectionItemRadioOption.NORMAL_NOT_SUPPORT, mInspectionItem.getState());
    }

}
