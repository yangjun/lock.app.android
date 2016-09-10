package com.wm.lock.ui.activities;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.load.LoadApi;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity
public class InspectionConstructActivity extends BaseActivity {

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.ll_menu)
    View mVMenu;

    @ViewById(R.id.ll_footer)
    View mFooter;

    @ViewById(R.id.lst_menu)
    ListView mLstMenu;

    private long mInspectionId;
    private List<String> mCategories;
    private int mSelectCategoryIndex = -1;

    private MenuAdapter menuAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.act_inspection_construct;
    }

    @Override
    protected void init() {
        mInspectionId = mSaveBundle.getLong(LockConstants.ID);
        loadCategories();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        setTitle(mSaveBundle.getString(LockConstants.TITLE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_inspection_construct, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!closeMenuIfOpen()) {
            super.onBackPressed();
        }
    }

    @OptionsItem(R.id.menu_submit)
    void onSubmitClick() {
        closeMenuIfOpen();
        showTip("提交...");
        // TODO
    }

    @Click(R.id.iv_category)
    void onCategoryClick() {
        toggleMenu();
    }

    @Click(R.id.iv_unlock)
    void onUnLockClick() {
        closeMenuIfOpen();
        showTip("开锁...");
        // TODO
    }

    @Click(R.id.iv_forward)
    void onForwardClick() {
        closeMenuIfOpen();
        // TODO
    }

    @Click(R.id.iv_backward)
    void onBackwardClick() {
        closeMenuIfOpen();
        // TODO
    }

    private void loadCategories() {
        new LoadApi(this).execute(new LoadApi.LoadCallBack<List<String>>() {

            @Override
            public void onPreExecute() {
                mFooter.setVisibility(View.INVISIBLE);
            }

            @Override
            public List<String> onExecute() {
                final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
                return bizService.listInspectionCategory(mInspectionId);
            }

            @Override
            public void onSuccess(List<String> result) {
                mCategories = result;
                setupMenu();
                setupContent(0);
                mFooter.setVisibility(View.VISIBLE);
            }

            @Override
            public int getContainerId() {
                return R.id.content_frame;
            }
        });
    }

    private void setupMenu() {
        menuAdapter = new MenuAdapter();
        mLstMenu.setAdapter(menuAdapter);
        mLstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeMenu();
                if (mSelectCategoryIndex != position) {
                    setupContent(position);
                }
            }
        });
    }

    private void setupContent(int index) {
        mSelectCategoryIndex = index;
        menuAdapter.notifyDataSetInvalidated();
        showTip("加载内容..." + index);
    }

    private boolean isMenuOpen() {
        return mDrawerLayout.isDrawerOpen(mVMenu);
    }

    private void closeMenu() {
        mDrawerLayout.closeDrawer(mVMenu);
    }

    private void openMenu() {
        mDrawerLayout.openDrawer(mVMenu);
    }

    private void toggleMenu() {
        if (isMenuOpen()) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    private boolean closeMenuIfOpen() {
        if (isMenuOpen()) {
            closeMenu();
            return true;
        }
        return false;
    }


    private class MenuAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        MenuAdapter() {
            mInflater = LayoutInflater.from(InspectionConstructActivity.this);
        }

        @Override
        public int getCount() {
            return mCategories.size();
        }

        @Override
        public Object getItem(int position) {
            return mCategories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_inspection_category_item, null);
            }
            final String item = mCategories.get(position);
            final TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            final ImageView ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            tvName.setText((position + 1) + ". " + item);

            if (mSelectCategoryIndex == position) {
                tvName.setTextColor(getResources().getColor(R.color.txt_title));
                ivArrow.setImageResource(R.mipmap.ic_category_arrow_selected);
                convertView.setBackgroundColor(getResources().getColor(R.color.bg_title));
            }
            else {
                tvName.setTextColor(getResources().getColor(R.color.txt_content));
                ivArrow.setImageResource(R.mipmap.ic_category_arrow_normal);
                convertView.setBackgroundColor(getResources().getColor(R.color.bg_item));
            }
            return convertView;
        }

    }

}
