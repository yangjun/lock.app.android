package com.wm.lock.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.core.utils.HardwareUtils;

import java.util.List;

/**
 * Created by wm on 15/12/27.
 */
public class VerticalPopMenu extends PopupWindow {

    private static final int DP_MARGIN_TOP = 16;

    private Context mCtx;
    private List<VerticalPopMenuItem> menuItems;
    private final int[] mLocation = new int[2];

    private LinearLayout mLL;
    private LinearLayout mLLContent;

    public VerticalPopMenu(Context context) {
        super(context);
        this.mCtx = context;
        init();
    }

    public void setData(List<VerticalPopMenuItem> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("the VerticalPopMenu items can not be null or empty");
        }

        menuItems = list;
        renderer();
    }

//    @Override
//    public void dismiss() {
//        super.dismiss();
//        setWindowAlpha(1f);
//    }

    public void showBottom(final View anchor) {
        anchor.getLocationOnScreen(mLocation);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLLContent.getLayoutParams();
        params.rightMargin = ((WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() - anchor.getRight();
        params.topMargin = mLocation[1] + anchor.getHeight() - HardwareUtils.dip2px(mCtx, DP_MARGIN_TOP);
        mLLContent.setLayoutParams(params);
        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
//        setWindowAlpha(0.4f);
    }

//    private void setWindowAlpha(float alpha) {
//        Window window = ((Activity) mCtx).getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = alpha;
//        window.setAttributes(lp);
//        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//    }

    private void init() {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.dialog_vertical_popmenu, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);

        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setClippingEnabled(false); // 设置为false将允许Windows精确定位
    }

    private void renderer() {
        View view = getContentView();
        mLL = (LinearLayout) view.findViewById(R.id.ll_pop);
        mLLContent = (LinearLayout) view.findViewById(R.id.ll_content);

        for (int i = 0, len = menuItems.size(); i < len; i++) {
            View itemView = LayoutInflater.from(mCtx).inflate(R.layout.dialog_vertical_popmenu_item, null);
            final VerticalPopMenuItem menuItem = menuItems.get(i);

            ImageView ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            ivIcon.setImageResource(menuItem.getIcon());

            TextView tvLabel = (TextView) itemView.findViewById(R.id.tv_pop_item);
            tvLabel.setText(menuItem.getLabel());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (menuItem.getClickListener() != null) {
                        menuItem.getClickListener().onClick(view);
                    }
                }
            });
            itemView.findViewById(R.id.v_divider).setVisibility(i == len - 1 ? View.GONE : View.VISIBLE);
            if (len == 1) {
                itemView.setBackgroundResource(R.drawable.selector_round_rectangle);
            }
            else if (i == 0) {
                itemView.setBackgroundResource(R.drawable.selector_round_rectangle_top);
            }
            else if (i == len - 1) {
                itemView.setBackgroundResource(R.drawable.selector_round_rectangle_bottom);
            }
            else {
                itemView.setBackgroundResource(R.drawable.selector_round_rectangle_middle);
            }
            mLL.addView(itemView);
        }
        setContentView(view);
    }

    public static class VerticalPopMenuItem {
        private int icon;
        private String label;
        private View.OnClickListener clickListener;

        public VerticalPopMenuItem() {

        }

        public VerticalPopMenuItem(int icon, String label, View.OnClickListener clickListener) {
            this.icon = icon;
            this.label = label;
            this.clickListener = clickListener;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public View.OnClickListener getClickListener() {
            return clickListener;
        }

        public void setClickListener(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

}
