package com.wm.lock.ui.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wm.lock.R;
import com.wm.lock.adapter.InspectionListAdapter;
import com.wm.lock.core.load.LoadListFragment;
import com.wm.lock.core.utils.FragmentUtils;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.ui.activities.HomeActivity;

import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment
public abstract class InspectionListFragment extends BaseFragment {

    protected int mPageLimit = Integer.MAX_VALUE;
    protected LoadListFragment<Inspection, ListView> mListFragment;
    private long mItemCount;

    @Override
    protected int getContentViewId() {
        return R.layout.frag_inspection_list;
    }

    @Override
    protected void init() {
        reload();
    }

    public long getItemCount() {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        mItemCount = bizService.countInspection(getQueryParam());
        return mItemCount;
    }

    public void reload() {
        mListFragment = new LoadListFragment<>(new LoadListFragment.LoadListCallBack<Inspection>() {
            @Override
            public BaseAdapter getAdapter(List<Inspection> list) {
                return new InspectionListAdapter(mActivity, list);
            }

            @Override
            public List<Inspection> onExecute() {
                getItemCount();
                final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
                return bizService.listInspection(getQueryParam());
            }

            @Override
            public void onSuccess(List<Inspection> result) {
                ((HomeActivity) mActivity).updateCount(mItemCount, InspectionListFragment.this);
            }
        });
        mListFragment.setLoadConfig(new LoadListFragment.LoadListConfig()
                .setPageLimit(mPageLimit)
                .setIsPullRefreshEnable(false)
                .setPaddingTop((int) getResources().getDimension(R.dimen.activity_horizontal_margin))
                .setPaddingBottom((int) getResources().getDimension(R.dimen.activity_horizontal_margin))
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InspectionListFragment.this.onItemClick((Inspection) parent.getItemAtPosition(position));
                    }
                }));
        FragmentUtils.replaceFragment(getChildFragmentManager(), R.id.fl, mListFragment);
    }

    protected InspectionQueryParam getQueryParam() {
        final InspectionQueryParam param = new InspectionQueryParam();
        param.setIndex(mListFragment.getPageNow());
        param.setLimit(mPageLimit);
        param.setUser_job_number(mActivity.loginUser().getJobNumber());
        return param;
    }
    
    protected abstract void onItemClick(Inspection item);

}
