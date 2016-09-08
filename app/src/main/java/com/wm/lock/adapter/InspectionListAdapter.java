package com.wm.lock.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.core.adapter.ListBaseAdapter;
import com.wm.lock.core.adapter.ViewHolderBase;
import com.wm.lock.core.utils.DateUtils;
import com.wm.lock.entity.Inspection;

import java.util.List;

public class InspectionListAdapter extends ListBaseAdapter<Inspection,InspectionListAdapter.ViewHolder> {

    public InspectionListAdapter(Context ctx, List<Inspection> list) {
        super(ctx, list);
    }

    @Override
    public int getViewLayoutId(int position) {
        return R.layout.list_inspection_item;
    }

    @Override
    public void onBindViewHolder(View convertView, ViewHolder viewHolder, Inspection item, int position) {
        viewHolder.tvName.setText(item.getPlan_name());
        viewHolder.tvDispatcher.setText(item.getDispatch_man());
        viewHolder.tvPlanEndTime.setText(DateUtils.getDateTimeStr2(item.getPlan_date()));
    }

    static class ViewHolder extends ViewHolderBase {

        TextView tvName;
        TextView tvDispatcher;
        TextView tvPlanEndTime;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDispatcher = (TextView) view.findViewById(R.id.tv_dispatcher);
            tvPlanEndTime = (TextView) view.findViewById(R.id.tv_plan_end_time);
        }
    }

}
