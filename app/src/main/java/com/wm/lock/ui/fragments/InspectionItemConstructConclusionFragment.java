package com.wm.lock.ui.fragments;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.entity.InspectionItemRadioOption;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment
public class InspectionItemConstructConclusionFragment extends InspectionItemConstructFragment {

    @ViewById(R.id.tv_title)
    TextView mTvTitle;

    @ViewById(R.id.rg_conclusion)
    RadioGroup mRgConclusion;

    @Override
    public void save() {
        final RadioButton rbConclusion = (RadioButton) mRgConclusion.findViewById(mRgConclusion.getCheckedRadioButtonId());
        mInspectionItem.setState(Integer.valueOf(rbConclusion.getTag().toString()));
        bizService().updateInspectionItem(mInspectionItem);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_item_construct_conclusion;
    }

    @Override
    protected void init() {
        super.init();

        // 标题
        final String title = super.getTitle();
        mTvTitle.setText(title);

        // 作业结果
        setupRadioButton(R.id.rb_conclusion_yes, InspectionItemRadioOption.CONCLUSION_Y, mInspectionItem.getState());
        setupRadioButton(R.id.rb_conclusion_no, InspectionItemRadioOption.CONCLUSION_N, mInspectionItem.getState());
    }

}
