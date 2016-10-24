package com.wm.lock.ui.activities;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.adapter.InspectionListAdapter;
import com.wm.lock.core.adapter.ListBaseAdapter;
import com.wm.lock.core.adapter.ViewHolderBase;
import com.wm.lock.core.load.LoadListFragment;
import com.wm.lock.core.utils.FragmentUtils;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.entity.params.TemperatureHumidityQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class TemperatureHumidityListActivity extends BaseActivity {

    private static final int PAGE_LIMIT = 20;

    protected LoadListFragment<TemperatureHumidity, ListView> mListFragment;

    @Override
    protected int getContentViewId() {
        return R.layout.act_temperature_humility_list;
    }

    @Override
    protected void init() {
        setupList();
    }

    private void setupList() {
        mListFragment = new LoadListFragment<>(new LoadListFragment.LoadListCallBack<TemperatureHumidity>() {
            @Override
            public BaseAdapter getAdapter(List<TemperatureHumidity> list) {
                return new ListAdapter(TemperatureHumidityListActivity.this, list);
            }

            @Override
            public List<TemperatureHumidity> onExecute() {
                final TemperatureHumidityQueryParam param = new TemperatureHumidityQueryParam();
                param.setIndex(mListFragment.getPageNow());
                param.setLimit(PAGE_LIMIT);
                param.setUser_job_number(loginUser().getJobNumber());
                final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
                return bizService.listTemperatureHumidity(param);
            }

        });
        mListFragment.setLoadConfig(new LoadListFragment.LoadListConfig()
                .setPageLimit(PAGE_LIMIT)
                .setIsPullRefreshEnable(true)
                .setPaddingTop((int) getResources().getDimension(R.dimen.activity_horizontal_margin))
                .setPaddingBottom((int) getResources().getDimension(R.dimen.activity_horizontal_margin))
        );
        FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.fl, mListFragment);
    }

    private class ListAdapter extends ListBaseAdapter<TemperatureHumidity, ViewHolder> {

        public ListAdapter(Context ctx, List<TemperatureHumidity> list) {
            super(ctx, list);
        }

        @Override
        public int getViewLayoutId(int position) {
            return R.layout.list_temperature_humility_item;
        }

        @Override
        public void onBindViewHolder(View convertView, ViewHolder viewHolder, TemperatureHumidity item, int position) {
            viewHolder.tvName.setText(item.getRoom_name());
            viewHolder.tvTemperature.setText(item.getTemperature());
            viewHolder.tvHumidity.setText(item.getHumidity());
        }
    }

    static class ViewHolder extends ViewHolderBase {

        TextView tvName;
        TextView tvTemperature;
        TextView tvHumidity;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTemperature = (TextView) view.findViewById(R.id.tv_temperature);
            tvHumidity = (TextView) view.findViewById(R.id.tv_humility);
        }
    }

}
