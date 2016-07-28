/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.finalteam.galleryfinal.adapter.FolderListAdapter;
import cn.finalteam.galleryfinal.adapter.ImageListAdapter;
import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.model.InageFolderInfo;
import cn.finalteam.galleryfinal.utils.MediaScanner;
import cn.finalteam.galleryfinal.utils.PhotoTools;
import cn.finalteam.toolsfinal.DateUtils;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Desction:图片选择器
 * Author:pengjianbo
 * Date:15/10/10 下午3:54
 */
public class ImageSelectActivity extends ImageBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final int HANLDER_TAKE_PHOTO_EVENT = 1000;
    private final int HANDLER_REFRESH_LIST_EVENT = 1002;

    private GridView mGvPhotoList;
    private ListView mLvFolderList;
    private LinearLayout mLlFolderPanel;
    private TextView mTvFolderTitle;
    private LinearLayout mLlTitle;
    private TextView mTvEmptyView;
    private TextView mTvIndicator;
    private View mBtnPreview;
    private View mBtnSave;

    private List<InageFolderInfo> mAllPhotoFolderList;
    private FolderListAdapter mFolderListAdapter;

    private List<ImageInfo> mCurPhotoList;
    private ImageListAdapter mImageListAdapter;

    private MediaScanner mMediaScanner;
    protected static String mPhotoTargetFolder;

    //是否需要刷新相册
    private boolean mHasRefreshGallery = false;
    private Map<String, ImageInfo> mSelectPhotoMap = new HashMap<>();

    private ImageHandler.ImageSelectConfig mSelectConfig;

    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANLDER_TAKE_PHOTO_EVENT) {
                ImageInfo imageInfo = (ImageInfo) msg.obj;
                takeRefreshGallery(imageInfo);
                refreshSelectCount();
            } else if (msg.what == HANDLER_REFRESH_LIST_EVENT) {
                refreshSelectCount();
                mImageListAdapter.notifyDataSetChanged();
                mFolderListAdapter.notifyDataSetChanged();
                if (mAllPhotoFolderList.get(0).getPhotoList() == null ||
                        mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
                    mTvEmptyView.setText(R.string.no_photo);
                }

                mGvPhotoList.setEnabled(true);
                mLlTitle.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gf_activity_image_select);

        if (savedInstanceState != null) {
            finish();
            return;
        }

        mPhotoTargetFolder = null;
        mSelectConfig = ImageHandler.getInstance().getSelectConfig();
        mSelectPhotoMap = fromList(mSelectConfig.getInitList());
        mMediaScanner = new MediaScanner(this);

        findViews();
        setListener();

        setTitle(null);
        setFooterEnabled();

        mAllPhotoFolderList = new ArrayList<>();
        mFolderListAdapter = new FolderListAdapter(this, mAllPhotoFolderList);
        mLvFolderList.setAdapter(mFolderListAdapter);

        mCurPhotoList = new ArrayList<>();
        mImageListAdapter = new ImageListAdapter(this, mCurPhotoList, mSelectPhotoMap, mScreenWidth, mSelectConfig);
        mGvPhotoList.setAdapter(mImageListAdapter);

        if (!mSelectConfig.isMutiSelect()) {
            findViewById(R.id.rl_bottom).setVisibility(View.GONE);
        }

        mGvPhotoList.setEmptyView(mTvEmptyView);

        refreshSelectCount();
        getPhotos();
    }

    private void findViews() {
        mGvPhotoList = (GridView) findViewById(R.id.gv_photo_list);
        mLvFolderList = (ListView) findViewById(R.id.lv_folder_list);
        mTvFolderTitle = (TextView) findViewById(R.id.tv_title_folder);
        mLlFolderPanel = (LinearLayout) findViewById(R.id.ll_folder_panel);
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
        mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
        mTvIndicator = (TextView) findViewById(R.id.tv_indicator);
        mBtnPreview = findViewById(R.id.btn_preview);
        mBtnSave = findViewById(R.id.btn_save);
    }

    private void setListener() {
        mLlTitle.setOnClickListener(this);
        mLvFolderList.setOnItemClickListener(this);
        mGvPhotoList.setOnItemClickListener(this);
        mBtnPreview.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
    }

    protected void deleteSelect(String photoId) {
        try {
            Iterator<Map.Entry<String, ImageInfo>> entries = mSelectPhotoMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, ImageInfo> entry = entries.next();
                if (entry.getValue() != null && entry.getValue().getId() == photoId) {
                    entries.remove();
                }
            }
            setFooterEnabled();
        } catch (Exception e) {
        }

        refreshAdapter();
    }

    private void refreshAdapter() {
        mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 100);
    }

    /**
     * 解决在5.0手机上刷新Gallery问题，从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
     * adapter的getCount与list.size不一致，所以我这里用了延迟刷新数据
     *
     * @param imageInfo
     */
    private void takeRefreshGallery(ImageInfo imageInfo) {
        setFooterEnabled();
        mCurPhotoList.add(0, imageInfo);
        mImageListAdapter.notifyDataSetChanged();

        //添加到集合中
        List<ImageInfo> imageInfoList = mAllPhotoFolderList.get(0).getPhotoList();
        if (imageInfoList == null) {
            imageInfoList = new ArrayList<>();
        }
        imageInfoList.add(0, imageInfo);
        mAllPhotoFolderList.get(0).setPhotoList(imageInfoList);

        if (mFolderListAdapter.getSelectFolder() != null) {
            InageFolderInfo inageFolderInfo = mFolderListAdapter.getSelectFolder();
            List<ImageInfo> list = inageFolderInfo.getPhotoList();
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(0, imageInfo);
            if (list.size() == 1) {
                inageFolderInfo.setCoverPhoto(imageInfo);
            }
            mFolderListAdapter.getSelectFolder().setPhotoList(list);
        } else {
            String folderA = new File(imageInfo.getPath()).getParent();
            for (int i = 1; i < mAllPhotoFolderList.size(); i++) {
                InageFolderInfo folderInfo = mAllPhotoFolderList.get(i);
                String folderB = null;
                if (!StringUtils.isEmpty(imageInfo.getPath())) {
                    folderB = new File(imageInfo.getPath()).getParent();
                }
                if (!TextUtils.isEmpty(folderA) && TextUtils.equals(folderA, folderB)) {
                    List<ImageInfo> list = folderInfo.getPhotoList();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(0, imageInfo);
                    folderInfo.setPhotoList(list);
                    if (list.size() == 1) {
                        folderInfo.setCoverPhoto(imageInfo);
                    }
                }
            }
        }

        mFolderListAdapter.notifyDataSetChanged();
    }

    protected void takeResult(ImageInfo imageInfo) {
        mSelectPhotoMap.put(imageInfo.getPath(), imageInfo);

        if (!mSelectConfig.isMutiSelect()) {
            mBtnSave.performClick();
        } else {
            Message message = mHanlder.obtainMessage();
            message.obj = imageInfo;
            message.what = HANLDER_TAKE_PHOTO_EVENT;
            mHanlder.sendMessageDelayed(message, 1);
        }
    }

    protected void preview() {
        List<ImageInfo> photoList = getSelectImages();
        ImageHandler.ImagePreviewConfig config = new ImageHandler.ImagePreviewConfig()
                .setCanEdit(true)
                .setInitList(photoList);
        ImageHandler.getInstance().preview(this, config, new ImageHandler.ImagePreviewCallback() {
            @Override
            public void onImagePreviewFinished(List<ImageInfo> result) {
                mBtnSave.performClick();
            }
        });
    }

    protected void save() {
        List<ImageInfo> photoList = getSelectImages();
        if (ImageHandler.getInstance().getSelectCallback() != null) {
            ImageHandler.getInstance().getSelectCallback().onImageSelectFinished(photoList);
        }

        Intent it = new Intent();
        it.putExtra("data", (Serializable) photoList);
        setResult(RESULT_OK, it);
        finish();
    }

    private List<ImageInfo> getSelectImages() {
        return new ArrayList<>(mSelectPhotoMap.values());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_title) {
            if (mLlFolderPanel.getVisibility() == View.VISIBLE) {
                mLlFolderPanel.setVisibility(View.GONE);
            } else {
                mLlFolderPanel.setVisibility(View.VISIBLE);
            }
        }
        else if (id == R.id.btn_preview) {
            if (mSelectPhotoMap.size() > 0) {
                preview();
            }
        }
        else if (id == R.id.btn_save) {
            save();
        }
//        else if (id == R.id.iv_clear) {
//            mSelectPhotoMap.clear();
//            mImageListAdapter.notifyDataSetChanged();
//            refreshSelectCount();
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        if (parentId == R.id.lv_folder_list) {
            folderItemClick(position);
        } else {
            photoItemClick(view, position);
            setFooterEnabled();
        }
    }

    private void folderItemClick(int position) {
        mLlFolderPanel.setVisibility(View.GONE);
        mCurPhotoList.clear();
        InageFolderInfo inageFolderInfo = mAllPhotoFolderList.get(position);
        if (inageFolderInfo.getPhotoList() != null) {
            mCurPhotoList.addAll(inageFolderInfo.getPhotoList());
        }
        mImageListAdapter.notifyDataSetChanged();

        if (position == 0) {
            mPhotoTargetFolder = null;
        } else {
            ImageInfo imageInfo = inageFolderInfo.getCoverPhoto();
            if (imageInfo != null && !StringUtils.isEmpty(imageInfo.getPath())) {
                mPhotoTargetFolder = new File(imageInfo.getPath()).getParent();
            } else {
                mPhotoTargetFolder = null;
            }
        }
        mTvFolderTitle.setText(inageFolderInfo.getFolderName());
        mFolderListAdapter.setSelectFolder(inageFolderInfo);
        mFolderListAdapter.notifyDataSetChanged();

        if (mCurPhotoList.size() == 0) {
            mTvEmptyView.setText(R.string.no_photo);
        }
    }

    private void photoItemClick(View view, int position) {
        ImageInfo info = mCurPhotoList.get(position);
        if (!mSelectConfig.isMutiSelect()) {
            mSelectPhotoMap.clear();
            mSelectPhotoMap.put(info.getPath(), info);
            mBtnSave.performClick();
            return;
        }
        boolean checked = false;
        if (mSelectPhotoMap.get(info.getPath()) == null) {
            if (mSelectConfig.isMutiSelect() && mSelectPhotoMap.size() == mSelectConfig.getMaxSize()) {
                toast(getString(R.string.select_max_tips));
                return;
            } else {
                mSelectPhotoMap.put(info.getPath(), info);
                checked = true;
            }
        } else {
            mSelectPhotoMap.remove(info.getPath());
            checked = false;
        }
        refreshSelectCount();

        ImageListAdapter.PhotoViewHolder holder = (ImageListAdapter.PhotoViewHolder) view.getTag();
        if (holder != null) {
            holder.setChecked(checked);
        } else {
            mImageListAdapter.notifyDataSetChanged();
        }
    }

    public void refreshSelectCount() {
        mTvIndicator.setText(getString(R.string.selected, mSelectPhotoMap.size(), mSelectConfig.getMaxSize()));
    }

    private void setFooterEnabled(boolean enabled) {
        mBtnSave.setEnabled(enabled);
        mBtnPreview.setEnabled(enabled);
    }

    private void setFooterEnabled() {
        boolean enabled = mSelectPhotoMap != null && !mSelectPhotoMap.isEmpty();
        setFooterEnabled(enabled);
    }

    /**
     * 获取所有图片
     */
    private void getPhotos() {
        mTvEmptyView.setText(R.string.waiting);
        mGvPhotoList.setEnabled(false);
        mLlTitle.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();

                mAllPhotoFolderList.clear();
                List<InageFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(ImageSelectActivity.this);
                mAllPhotoFolderList.addAll(allFolderList);

                mCurPhotoList.clear();
                if (allFolderList.size() > 0) {
                    if (allFolderList.get(0).getPhotoList() != null) {
                        mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
                    }
                }

                refreshAdapter();
            }
        }.start();
    }

    private Map<String, ImageInfo> fromList(List<ImageInfo> list) {
        Map<String, ImageInfo> result = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return result;
        }
        for (ImageInfo item : list) {
            if (item != null && !TextUtils.isEmpty(item.getPath())) {
                result.put(item.getPath(), item);
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        if (mLlFolderPanel.getVisibility() == View.VISIBLE) {
            mLlTitle.performClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasRefreshGallery) {
            mHasRefreshGallery = false;
            getPhotos();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoTargetFolder = null;
        mSelectPhotoMap.clear();
        mMediaScanner.unScanFile();
        System.gc();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.takephoto) {
            if (mLlFolderPanel.getVisibility() == View.VISIBLE) {
                mLlTitle.performClick();
                return super.onOptionsItemSelected(item);
            }
            //判断是否达到多选最大数量
            if (mSelectConfig.isMutiSelect() && mSelectPhotoMap.size() == mSelectConfig.getMaxSize()) {
                toast(getString(R.string.select_max_tips));
                return super.onOptionsItemSelected(item);
            }
            takePhoto();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mSelectConfig.isShowCamera()) {
            getMenuInflater().inflate(R.menu.gf_select, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    private void updateGallery(String filePath) {
        mMediaScanner.scanFile(filePath, "image/jpeg");
    }

    protected void takePhoto() {
        final String folder = Environment.getExternalStorageDirectory() + "/DCIM/" + Consts.TAKE_PHOTO_FOLDER;
        final String fileName = "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg";
        final String path = folder + fileName;
        ImageHandler.ImageTakeConfig config = new ImageHandler.ImageTakeConfig()
                .setPath(path);
        ImageHandler.getInstance().takePhoto(this, config, new ImageHandler.ImageTakeCallback() {
            @Override
            public void onImageTaked(Intent data) {
                final ImageInfo info = new ImageInfo();
                info.setId(getRandom(10000, 99999) + "");
                info.setPath(path);
                updateGallery(path);
                takeResult(info);
            }
        });
    }

}
