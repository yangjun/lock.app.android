package com.wm.lock.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.core.adapter.ListBaseAdapter;
import com.wm.lock.core.adapter.ViewHolderBase;
import com.wm.lock.entity.LockDevice;

import java.util.List;

public class BlueDeviceLockListAdapter extends ListBaseAdapter<LockDevice, BlueDeviceLockListAdapter.ViewHolder>{

    public BlueDeviceLockListAdapter(Context ctx, List<LockDevice> list) {
        super(ctx, list);
    }

    @Override
    public int getViewLayoutId(int position) {
        return R.layout.list_bluetooth_lock_item;
    }

    @Override
    public void onBindViewHolder(View convertView, ViewHolder viewHolder, LockDevice item, int position) {
        final String name = String.format("%s. %s", position + 1, item.getLock_name());
        viewHolder.tvName.setText(name);
    }

    static class ViewHolder extends ViewHolderBase {

        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }
    }

}
