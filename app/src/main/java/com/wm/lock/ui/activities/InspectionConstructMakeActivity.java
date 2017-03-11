package com.wm.lock.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.TextView;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.load.LoadApi;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.DateUtils;
import com.wm.lock.core.utils.FragmentUtils;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.ui.fragments.AttachPhotoFragment;
import com.wm.lock.ui.fragments.AttachPhotoFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;

import java.util.List;

@EActivity
public class InspectionConstructMakeActivity extends InspectionConstructBaseActivity {

    private Inspection mInspection;
    private boolean menuEnable = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mEnable) {
            getMenuInflater().inflate(R.menu.menu_act_inspection_construct_make, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mEnable) {
            menu.findItem(R.id.submit).setVisible(menuEnable);
            menu.findItem(R.id.open_lock).setVisible(menuEnable);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @OptionsItem(R.id.submit)
    void onSubmitClick() {
        doSubmit();
    }

    @OptionsItem(R.id.open_lock)
    void onUnLockClick() {
        unLock(null);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_inspection_construct_make;
    }

    @Override
    protected void init() {
        super.init();
        loadData();
    }

    private void loadData() {
        new LoadApi(this).execute(new LoadApi.LoadCallBack<Inspection>() {

            @Override
            public void onPreExecute() {
                menuEnable = false;
                invalidateOptionsMenu();
            }

            @Override
            public Inspection onExecute() {
                final IBizService bizService = bizService();
                final Inspection inspection = bizService.findInspection(mInspectionId);
                final List<InspectionItem> items = bizService.listInspectionItem(mInspectionId);
                inspection.setItems(items);
                return inspection;
            }

            @Override
            public void onSuccess(Inspection result) {
                menuEnable = true;
                invalidateOptionsMenu();
                mInspection = result;
                renderer();
            }

            @Override
            public int getContainerId() {
                return R.id.fl;
            }
        });
    }

    private void renderer() {
        // 任务名称
        final TextView tvPlanName = (TextView) findViewById(R.id.tv_plan_name);
        tvPlanName.setText(mInspection.getPlan_name());

        // 编制人
        final TextView tvDispatchMan = (TextView) findViewById(R.id.tv_dispatch_man);
        tvDispatchMan.setText(mInspection.getDispatch_man());

        // 责任人
        final TextView tvDealMan = (TextView) findViewById(R.id.tv_deal_man);
        tvDealMan.setText(mInspection.getDeal_man());

        // 工作人员
        final TextView tvJobMan = (TextView) findViewById(R.id.tv_job_name);
        tvJobMan.setText(mInspection.getJob_man());

        // 工作地点
        final TextView tvJobAddress = (TextView) findViewById(R.id.tv_job_address);
        tvJobAddress.setText(mInspection.getRoom_name());

        // 工作地点
        final TextView tvJobRange = (TextView) findViewById(R.id.tv_job_range);
        tvJobRange.setText(getJobRange());

        // 开始时间
        final TextView tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvStartTime.setText(DateUtils.getDateTimeStr(mInspection.getStart_date()));

        // 结束时间
        final TextView tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvEndTime.setText(DateUtils.getDateTimeStr(mInspection.getEnd_date()));

        // 工作内容
        final TextView tvContent = (TextView) findViewById(R.id.tv_job_content);
        tvContent.setText(mInspection.getContent());

        // 备注
        final TextView tvRemark = (TextView) findViewById(R.id.tv_job_remark);
        tvRemark.setText(mInspection.getNote());

        // 照片
        rendererPhoto();
    }

    private void rendererPhoto() {
        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, mInspectionId);
        bundle.putString(LockConstants.FLAG, AttachmentSource.INSPECTION.name());
        bundle.putBoolean(LockConstants.BOOLEAN, mEnable);
        final AttachPhotoFragment photoFragment = new AttachPhotoFragment_();
        photoFragment.setArguments(bundle);

        FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fl_photos, photoFragment);
    }

    private String getJobRange() {
        final List<InspectionItem> list = mInspection.getItems();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        final String split = ", ";
        final StringBuilder builder = new StringBuilder();
        for (InspectionItem item : list) {
            builder.append(item.getCabinet_name()).append(split);
        }

        String result = builder.toString();
        if (!TextUtils.isEmpty(result) && result.endsWith(split)) {
            result = result.substring(0, result.length() - split.length());
        }
        return result;
    }

}
