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

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.toolsfinal.FileUtils;

public final class ImageHandler {

    private ImageOperationProvider mProvider;

    private Context mCtx;

    private ImageSelectConfig mSelectConfig;
    private ImageSelectCallback mSelectCallback;

    private ImagePreviewConfig mPreviewConfig;
    private ImagePreviewCallback mPreviewCallback;

    private ImageTakeConfig mTakeConfig;
    private ImageTakeCallback mTakeCallback;

    private ImageEditConfig mEditConfig;
    private ImageEditCallback mEditCallback;

    private ImageGuideConfig mGuideConfig;
    private ImageGuideCallback mGuideCallback;

    private ImageHandler() {

    }

    private static class InstanceHolder {
        static ImageHandler instance = new ImageHandler();
    }

    public static ImageHandler getInstance() {
        return InstanceHolder.instance;
    }

    public void setOperationProvider(ImageOperationProvider provider) {
        mProvider = provider;
    }

    public void destory() {
        cleanTempFile();
        mCtx = null;

        mSelectConfig = null;
        mSelectCallback = null;

        mPreviewConfig = null;
        mPreviewCallback = null;

        mTakeConfig = null;
        mTakeCallback = null;

        mEditConfig = null;
        mEditCallback = null;

        mGuideConfig = null;
        mGuideCallback = null;
    }

    /**
     * 选择图片
     */
    public void select(Context ctx, ImageSelectConfig config, ImageSelectCallback callback) {
        this.mCtx = ctx;
        this.mSelectConfig = config;
        this.mSelectCallback = callback;

        Intent intent = new Intent(mCtx, ImageSelectActivity.class);
        mCtx.startActivity(intent);
    }

    /**
     * 预览图片
     */
    public void preview(Context ctx, ImagePreviewConfig config, ImagePreviewCallback callback) {
        this.mCtx = ctx;
        this.mPreviewConfig = config;
        this.mPreviewCallback = callback;
        mCtx.startActivity(new Intent(mCtx, ImagePreviewActivity.class));
    }

    /**
     * 导航图片
     */
    public void guide(Context ctx, ImageGuideConfig config, ImageGuideCallback callback) {
        this.mCtx = ctx;
        this.mGuideConfig = config;
        this.mGuideCallback = callback;
        mCtx.startActivity(new Intent(mCtx, ImageGuideActivity.class));
    }

    /**
     * 拍照
     */
    public void takePhoto(Context ctx, ImageTakeConfig config, ImageTakeCallback callback) {
        this.mCtx = ctx;
        this.mTakeConfig = config;
        this.mTakeCallback = callback;
        mCtx.startActivity(new Intent(mCtx, ImageTakePhotoActivity.class));
    }

    /**
     * 编辑图片（裁减、旋转、美化等）
     */
    public void edit(Context ctx, ImageEditConfig config, ImageEditCallback callback) {
        this.mCtx = ctx;
        this.mEditConfig = config;
        this.mEditCallback = callback;
        mCtx.startActivity(new Intent(mCtx, ImageClipActivity.class));
    }

    public Context getCtx() {
        return mCtx;
    }

    public ImageSelectConfig getSelectConfig() {
        return mSelectConfig;
    }

    public ImageSelectCallback getSelectCallback() {
        return mSelectCallback;
    }

    public ImagePreviewConfig getPreviewConfig() {
        return mPreviewConfig;
    }

    public ImagePreviewCallback getPreviewCallback() {
        return mPreviewCallback;
    }

    public ImageTakeConfig getTakeConfig() {
        return mTakeConfig;
    }
    public ImageTakeCallback getTakeCallback() {
        return mTakeCallback;
    }

    public ImageEditConfig getEditConfig() {
        return mEditConfig;
    }

    public ImageEditCallback getEditCallback() {
        return mEditCallback;
    }

    public ImageGuideConfig getGuideConfig() {
        return mGuideConfig;
    }

    public ImageGuideCallback getGuideCallback() {
        return mGuideCallback;
    }

    public ImageOperationProvider getOperationProvider() {
        if (mProvider == null) {
            mProvider = getDefaultProvider();
        }
        return mProvider;
    }

    /**
     * 清楚缓存文件
     */
    public void clearCacheFile() {
        //清楚裁剪冗余图片
        FileUtils.deleteFile(Consts.PHOTO_EDIT_TEMP_DIR);
    }

    private void cleanTempFile() {
        File file = new File(Consts.PHOTO_EDIT_TEMP_DIR);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    long t = f.lastModified();
                    long curTime = System.currentTimeMillis();
                    if (t == 0l && (curTime - t) > 86400000) {
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    private ImageOperationProvider getDefaultProvider() {
        return new ImageOperationProvider() {
            @Override
            public void notify(String msg) {
                Toast.makeText(mCtx, msg, Toast.LENGTH_LONG).show();
            }
        };
    }

    /***************************************************************/
    public static class ImageSelectConfig {
        private boolean canEdit = false;
        private boolean showCamera = true;
        private List<ImageInfo> initList;
        private int maxSize = 9;

        public boolean isMutiSelect() {
            return maxSize > 1;
        }

        public boolean isCanEdit() {
            return canEdit;
        }

        public ImageSelectConfig setCanEdit(boolean canEdit) {
            this.canEdit = canEdit;
            return this;
        }

        public boolean isShowCamera() {
            return showCamera;
        }

        public ImageSelectConfig setShowCamera(boolean showCamera) {
            this.showCamera = showCamera;
            return this;
        }

        public List<ImageInfo> getInitList() {
            return initList;
        }

        public ImageSelectConfig setInitList(List<ImageInfo> initList) {
            this.initList = initList;
            return this;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public ImageSelectConfig setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
    }

    public static interface ImageSelectCallback {
        public void onImageSelectFinished(List<ImageInfo> result);
    }
    /***************************************************************/


    /***************************************************************/
    public static class ImagePreviewConfig {
        private boolean canEdit = true;
        private List<ImageInfo> initList;
        private int current;
        private boolean isBackValid;

        public boolean isCanEdit() {
            return canEdit;
        }

        public ImagePreviewConfig setCanEdit(boolean canEdit) {
            this.canEdit = canEdit;
            return this;
        }

        public List<ImageInfo> getInitList() {
            return initList;
        }

        public ImagePreviewConfig setInitList(List<ImageInfo> initList) {
            this.initList = initList;
            return this;
        }

        public int getCurrent() {
            return current;
        }

        public ImagePreviewConfig setCurrent(int current) {
            this.current = current;
            return this;
        }

        public ImagePreviewConfig setIsBackValid(boolean isBackValid) {
            this.isBackValid = isBackValid;
            return this;
        }

        public boolean isBackValid() {
            return isBackValid;
        }
    }

    public static interface ImagePreviewCallback {
        public void onImagePreviewFinished(List<ImageInfo> result);
    }
    /***************************************************************/

    /***************************************************************/
    public static class ImageEditConfig {
        private ImageInfo item;
        private int maxWidth;
        private int maxHeight;
        private String savePath;

        public ImageInfo getItem() {
            return item;
        }

        public ImageEditConfig setItem(ImageInfo item) {
            this.item = item;
            return this;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public ImageEditConfig setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public ImageEditConfig setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public String getSavePath() {
            return savePath;
        }

        public ImageEditConfig setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }
    }

    public static interface ImageEditCallback {
        public void onImageEditSuccess(ImageInfo result);
        public void onImageEditFail(Exception e);
    }
    /***************************************************************/


    /***************************************************************/
    public static class ImageTakeConfig {
        private String path;

        public String getPath() {
            return path;
        }

        public ImageTakeConfig setPath(String path) {
            this.path = path;
            return this;
        }
    }

    public static interface ImageTakeCallback {
        public void onImageTaked(Intent data);
    }
    /***************************************************************/


    /***************************************************************/
    public static class ImageGuideConfig {
        private int[] drawables;
        private boolean canSkip;
        private String text;

        public int[] getDrawables() {
            return drawables;
        }

        public ImageGuideConfig setDrawables(int[] drawables) {
            this.drawables = drawables;
            return this;
        }

        public boolean isCanSkip() {
            return canSkip;
        }

        public ImageGuideConfig setCanSkip(boolean canSkip) {
            this.canSkip = canSkip;
            return this;
        }

        public String getText() {
            return text;
        }

        public ImageGuideConfig setText(String text) {
            this.text = text;
            return this;
        }
    }

    public static interface ImageGuideCallback {
        public void onImageGuideFinished();
    }
    /***************************************************************/

    public static interface ImageOperationProvider {
        public void notify(String msg);
    }

}
