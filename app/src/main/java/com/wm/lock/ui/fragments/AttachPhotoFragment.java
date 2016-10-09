package com.wm.lock.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wm.lock.LockConfig;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.IoUtils;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import cn.finalteam.galleryfinal.ImageHandler;
import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.utils.ImageUtils;

@EFragment
public class AttachPhotoFragment extends BaseFragment implements ImageHandler.ImageTakeCallback,
        ImageHandler.ImagePreviewCallback,  AdapterView.OnItemClickListener {

    private long mForeignId;
    private AttachmentSource mSource;
    private boolean mEnable;

    @ViewById(R.id.nsg)
    GridView mGridView;

    private List<String> mPhotoPathList;
    private GridAdapter mGridAdapter;

    private String mLastPhotoPath;

    @Override
    protected int getContentViewId() {
        return R.layout.frag_attach_photo;
    }

    @Override
    protected void init() {
        final Bundle bundle = getArguments();
        mForeignId = bundle.getLong(LockConstants.ID);
        mSource = AttachmentSource.valueOf(bundle.getString(LockConstants.FLAG));
        mEnable = bundle.getBoolean(LockConstants.BOOLEAN);

        mPhotoPathList = loadPathList(mForeignId);
        mGridAdapter = new GridAdapter();
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(this);
    }

    public static int count(AttachmentSource source, long foreignId) {
        return bizService().countAttachments(foreignId, source, AttachmentType.PHOTO);
    }

    public void takePhoto() {
        mLastPhotoPath = bizService().getAttachmentSavePath(mForeignId, mSource, AttachmentType.PHOTO);
        final ImageHandler.ImageTakeConfig config = new ImageHandler.ImageTakeConfig().setPath(mLastPhotoPath);
        ImageHandler.getInstance().takePhoto(mActivity, config, this);
    }

    @Override
    public void onImageTaked(Intent data) {
        new AsyncExecutor().execute(new AsyncWork<Void>() {
            @Override
            public void onPreExecute() {
                mActivity.showWaittingDialog(R.string.message_photo_processing);
            }

            @Override
            public void onSuccess(Void result) {
                mActivity.dismissDialog();
                mPhotoPathList.add(mLastPhotoPath);
                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Exception e) {
                Logger.p("fail to compress photo at path: " + mLastPhotoPath, e);
                mActivity.dismissDialog();
                IoUtils.deleteFile(mLastPhotoPath);
                mActivity.showTip(R.string.message_photo_process_fail);
            }

            @Override
            public Void onExecute() throws Exception {
                ImageUtils.zoomAndSave(mLastPhotoPath,
                        mLastPhotoPath,
                        LockConfig.PHOTO_WIDTH_MAX,
                        LockConfig.PHOTO_WIDTH_MAX,
                        80);
                return null;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Object obj = parent.getItemAtPosition(position);
        if (obj != null) {
            ImageHandler.ImagePreviewConfig config = new ImageHandler.ImagePreviewConfig()
                    .setCanEdit(mEnable)
                    .setCurrent(position)
                    .setInitList(toImageInfoList())
                    .setIsBackValid(mEnable);
            ImageHandler.getInstance().preview(mActivity, config, this);
        }
    }

    @Override
    public void onImagePreviewFinished(List<ImageInfo> result) {
        if (!mEnable) {
            return;
        }

        final List<ImageInfo> deleteList = new ArrayList<>();
        final List<ImageInfo> allList = toImageInfoList();
        CollectionUtils.diff(result, allList, deleteList, new Comparator<ImageInfo>() {
            @Override
            public int compare(ImageInfo lhs, ImageInfo rhs) {
                return TextUtils.equals(lhs.getPath(), rhs.getPath()) ? 0 : 1;
            }
        });

        if (!CollectionUtils.isEmpty(deleteList)) {
            for (ImageInfo item : deleteList) {
                IoUtils.deleteFile(item.getPath());
                mPhotoPathList.remove(item.getPath());
            }
            mGridAdapter.notifyDataSetChanged();
        }
    }

    private List<String> loadPathList(long foreignId) {
        List<String> result = bizService().listAttachments(foreignId, mSource, AttachmentType.PHOTO);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    private List<ImageInfo> toImageInfoList() {
        final List<ImageInfo> result = new ArrayList<>();
        for (String item : mPhotoPathList) {
            final ImageInfo imageInfo = new ImageInfo();
            imageInfo.setId(item);
            imageInfo.setPath(item);
            result.add(imageInfo);
        }
        return result;
    }

    private static IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    private class GridAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private DisplayImageOptions mImageOptions;

        GridAdapter() {
            mInflater = LayoutInflater.from(mActivity);
            mImageOptions = ImageUtils.getRoundCornerImageOptions(-1);
        }

        @Override
        public int getCount() {
            final int size = mPhotoPathList.size();
            return hasBtn() ? size + 1 : size;
        }

        @Override
        public Object getItem(int position) {
            if (hasBtn() && position == getCount() - 1) {
                return null;
            }
            return mPhotoPathList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.frag_attach_photo_item, null);
            }

            final ImageView iv = (ImageView) convertView.findViewById(R.id.iv_photo);
            final View btn = convertView.findViewById(R.id.ll_insert_photo);
            if (hasBtn() && position == getCount() - 1) {
                iv.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                    }
                });
            }
            else {
                iv.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                btn.setOnClickListener(null);
                final String item = mPhotoPathList.get(position);
                ImageUtils.loadImage(mActivity, item, iv, mImageOptions);
            }

            return convertView;
        }

        private boolean hasBtn() {
            return mEnable && mPhotoPathList.size() < LockConfig.PHOTO_COUNT_MAX;
        }

    }

}
