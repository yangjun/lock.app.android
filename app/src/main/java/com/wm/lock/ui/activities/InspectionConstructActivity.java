package com.wm.lock.ui.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
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
import com.wm.lock.attachment.AttachmentProcessor;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.callback.Injector;
import com.wm.lock.core.load.LoadApi;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.FragmentUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.dto.InspectionResultDto;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.AttachmentUploadSource;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.helper.Helper;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.ui.fragments.InspectionConstructFragment;
import com.wm.lock.ui.fragments.InspectionConstructFragment_;
import com.wm.lock.websocket.WebSocketWriter;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

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

    private boolean mEnable;
    private long mInspectionId;
    private String mPlanId;
    private String mInspectionName;
    private List<String> mCategories;
    private int mSelectCategoryIndex = 0;

    private MenuAdapter menuAdapter;
    private InspectionConstructFragment mCurrContentFragment;

//    private boolean mIsTrySubmit = false;

    @Override
    protected int getContentViewId() {
        return R.layout.act_inspection_construct;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        // 记住选择的分类
        CacheManager.getInstance().putInt(LockConstants.INDEX + mInspectionId, mSelectCategoryIndex, CacheManager.CHANNEL_PREFERENCE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void init() {
        mEnable = mSaveBundle.getBoolean(LockConstants.BOOLEAN, true);
        mInspectionId = mSaveBundle.getLong(LockConstants.ID);
        mPlanId = mSaveBundle.getString(LockConstants.FLAG);
        mInspectionName = mSaveBundle.getString(LockConstants.NAME);
        loadCategories();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        setTitle(mSaveBundle.getString(LockConstants.TITLE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mEnable) {
            getMenuInflater().inflate(R.menu.menu_act_inspection_construct, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!closeMenuIfOpen()) {
//            if (mIsTrySubmit) {
//                setResult(RESULT_FIRST_USER);
//            }
            finish();
        }
    }

    @OptionsItem(R.id.menu_submit)
    void onSubmitClick() {
        closeMenuIfOpen();
        mCurrContentFragment.save(); // 保存当前数据
        submit();
    }

    @Click(R.id.iv_category)
    void onCategoryClick() {
        toggleMenu();
    }

    @Click(R.id.iv_unlock)
    void onUnLockClick() {
        closeMenuIfOpen();

        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, mInspectionId);
        bundle.putString(LockConstants.DATA, mCategories.get(mSelectCategoryIndex));
        RedirectUtils.goActivity(this, OpenCabinetActivity_.class, bundle);
    }

    @Click(R.id.iv_forward)
    void onForwardClick() {
        closeMenuIfOpen();
        if (mSelectCategoryIndex == 0) {
            showTip(R.string.message_inspection_category_no_forward);
        }
        else {
            final int index = mSelectCategoryIndex - 1;
            setupContent(index, false);
        }
    }

    @Click(R.id.iv_backward)
    void onBackwardClick() {
        closeMenuIfOpen();
        if (mSelectCategoryIndex >= mCategories.size() - 1) {
            showTip(R.string.message_inspection_category_no_backward);
        }
        else {
            final int index = mSelectCategoryIndex + 1;
            setupContent(index, false);
        }
    }

    private void submit() {
        new AsyncExecutor().execute(new AsyncWork<InspectionItem>() {

            @Override
            public void onPreExecute() {
                showWaittingDialog(R.string.message_submit_checking);
            }

            @Override
            public void onSuccess(InspectionItem result) {
                dismissDialog();
                if (result == null) {
                    doSubmit();
                } else {
                    int categoryIndex = -1;
                    for (int i = 0, len = mCategories.size(); i < len; i++) {
                        if (mCategories.get(i).equals(result.getItem_cate_name())) {
                            categoryIndex = i;
                            break;
                        }
                    }

                    int index = -1;
                    final List<InspectionItem> itemList = bizService().listInspectionItemByCategory(mInspectionId, mCategories.get(categoryIndex));
                    for (int i = 0, len = itemList.size(); i < len; i++) {
                        if (itemList.get(i).getId_() == result.getId_()) {
                            index = i;
                            break;
                        }
                    }

                    final String msg = String.format(getString(R.string.message_submit_check_success), (categoryIndex + 1) + "." + (index + 1));
                    showTip(msg);
                }
            }

            @Override
            public void onFail(Exception e) {
                Logger.p("fail to check before submit", e);
                dismissDialog();
                showTip(R.string.message_submit_check_fail);
            }

            @Override
            public InspectionItem onExecute() throws Exception {
                final List<InspectionItem> itemList = bizService().listInspectionItem(mInspectionId);
                for (int i = 0, len = itemList.size(); i < len; i++) {
                    final InspectionItem item = itemList.get(i);
                    if (TextUtils.isEmpty(item.getResult())) {
                        return item;
                    }
                }
                return null;
            }

        });
    }

    private void doSubmit() {
        DialogManager.showConfirmDialog(this, R.string.label_notify, getString(R.string.message_submit_confirm), false, new Injector() {
            @Override
            public void execute() {
                bizService().submitInspection(mInspectionId);
                // 有附件, 走附件上传处理器
                if (hasAttachment()) {
                    AttachmentProcessor.getInstance().startIfNot();
                }
                // 没有附件, 走web socket
                else {
                    WebSocketWriter.submitInspection(mInspectionId, true);
                }
                showTip(R.string.message_submit_to_background);
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });

//        new AsyncExecutor().execute(new AsyncWork<Void>() {
//            @Override
//            public void onPreExecute() {
//                mIsTrySubmit = true;
//                showWaittingDialog(R.string.message_submiting);
//            }
//
//            @Override
//            public void onSuccess(Void result) {
//
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                Logger.p("fail to submit inspection", e);
//
//                final InspectionResultDto dto = new InspectionResultDto();
//                dto.setSuccess(false);
//                EventBus.getDefault().post(dto);
//            }
//
//            @Override
//            public Void onExecute() throws Exception {
//                bizService().submitInspection(mInspectionId);
//                WebSocketWriter.submitInspection(mInspectionId);
//                return null;
//            }
//        });
    }

    public void onEventMainThread(InspectionResultDto dto) {
        if (mEnable) {
            return;
        }

        // 如果该任务提交成功,则回到主页
        if (dto.isSuccess() && dto.getPlan_id().equals(mPlanId)) {
            showTip(R.string.message_submit_success_in_fail_list);
            finish();
        }
    }

    private boolean hasAttachment() {
        final List<InspectionItem> inspectionItemList = bizService().listInspectionItem(mInspectionId);
        for (InspectionItem item : inspectionItemList) {
            final List<String> attachmentList = bizService().listAttachments(item.getId_(), AttachmentSource.INSPECTION_ITEM, AttachmentType.PHOTO);
            if (!CollectionUtils.isEmpty(attachmentList)) {
                return true;
            }
        }
        return false;
    }

    private IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    private void loadCategories() {
        new LoadApi(this).execute(new LoadApi.LoadCallBack<List<String>>() {

            @Override
            public void onPreExecute() {
                mFooter.setVisibility(View.INVISIBLE);
            }

            @Override
            public List<String> onExecute() {
                return bizService().listInspectionCategory(mInspectionId);
            }

            @Override
            public void onSuccess(List<String> result) {
                mCategories = result;
                setupMenu();
                setupContent(CacheManager.getInstance().getInt(LockConstants.INDEX + mInspectionId, CacheManager.CHANNEL_PREFERENCE), true);
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
                    setupContent(position, false);
                }
            }
        });
    }

    private void setupContent(int index, boolean isFirst) {
        mSelectCategoryIndex = index;
        menuAdapter.notifyDataSetInvalidated();

        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, mInspectionId);
        bundle.putString(LockConstants.NAME, mInspectionName);
        bundle.putInt(LockConstants.POS, mSelectCategoryIndex);
        bundle.putString(LockConstants.DATA, mCategories.get(mSelectCategoryIndex));
        bundle.putBoolean(LockConstants.BOOLEAN, mEnable);

        mCurrContentFragment = InspectionConstructFragment_.builder().build();
        mCurrContentFragment.setArguments(bundle);
        if (isFirst && mEnable) {
            final int lastOffset = CacheManager.getInstance().getInt(LockConstants.POS + mInspectionId, CacheManager.CHANNEL_PREFERENCE);
            mCurrContentFragment.selectOffset(lastOffset);
        }
        FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.content_frame, mCurrContentFragment);
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
