package com.wm.lock.core.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.core.adapter.ListBaseAdapter;
import com.wm.lock.core.adapter.ViewHolderBase;

import java.util.List;

/**
 * Created by wangmin on 16/7/13.
 */
public class ChoiceItemLayout extends FrameLayout {

    private ListView mListView;
    private ItemAdapter mAdapter;
    private List<ChoiceItem> mList;
    private ChoiceItem mSelectItem;

    private OnChoiceChangeListener mChangeListener;

    public ChoiceItemLayout(Context context) {
        super(context);
        init();
    }

    public ChoiceItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChoiceItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChoiceItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setOnChoiceChangeListener(OnChoiceChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_choiceitem, this);
        mListView = (ListView) findViewById(R.id.nls_item);
    }

    public void setData(List<ChoiceItem> list) {
        if (list != null && !list.isEmpty()) {
            mList = list;
            mAdapter = new ItemAdapter(getContext(), list);
            mListView.setAdapter(mAdapter);
        }
    }

    public ChoiceItem getSelectItem() {
        return mSelectItem;
    }

    public void setSelectItem(ChoiceItem item) {
        if (item == null) {
            return;
        }

        if (mSelectItem != null && mSelectItem == item) {
            return;
        }

        changeSelect(item, false);
    }

    private void changeSelect(ChoiceItem item, boolean callback) {
        final ChoiceItem lastItem = mSelectItem;

        if (mSelectItem != null) {
            mSelectItem.setIsSelect(false);
        }
        item.setIsSelect(true);
        mSelectItem = item;

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (callback && mChangeListener != null) {
            mChangeListener.onChoiceChanged(lastItem, item);
        }
    }

    private class ItemAdapter extends ListBaseAdapter<ChoiceItem, ViewHolder> {

        public ItemAdapter(Context ctx, List<ChoiceItem> list) {
            super(ctx, list);
        }

        @Override
        public int getViewLayoutId(int position) {
            return R.layout.widget_choiceitem_item;
        }

        @Override
        public void onBindViewHolder(View convertView, ViewHolder holder, final ChoiceItem item, final int position) {
            holder.tvTitle.setText(item.getTitle());
            holder.ivIcon.setImageDrawable(item.getIcon() > 0 ? getResources().getDrawable(item.getIcon()) : null);
            if (item.isSelect()) {
                mSelectItem = item;
                holder.ivIndicator.setImageResource(R.mipmap.ic_choice);
            }
            else {
                holder.ivIndicator.setImageDrawable(null);
            }
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectItem != item) {
                        changeSelect(item, true);
                    }
                }
            });
        }
    }

    static class ViewHolder extends ViewHolderBase {

        TextView tvTitle;
        ImageView ivIcon;
        ImageView ivIndicator;

        public ViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTitle = (TextView) view.findViewById(R.id.tv_title_item);
            ivIndicator = (ImageView) view.findViewById(R.id.iv_indicator_item);
        }
    }

    public static interface OnChoiceChangeListener {
        public void onChoiceChanged(ChoiceItem lastItem, ChoiceItem newItem);
    }

    public static class ChoiceItem {

        private int icon;
        private String title;
        private boolean isSelect;
        private Object tag;

        public ChoiceItem() {

        }

        public ChoiceItem(int icon, String title, boolean isSelect, Object tag) {
            this.icon = icon;
            this.title = title;
            this.isSelect = isSelect;
            this.tag = tag;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }

}
