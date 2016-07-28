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

package cn.finalteam.galleryfinal.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.ImageHandler;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.utils.ImageUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:59
 */
public class ImageListAdapter extends CommonBaseAdapter<ImageListAdapter.PhotoViewHolder, ImageInfo> {

    private Map<String, ImageInfo> mSelectList;
    private int mScreenWidth;
    private ImageHandler.ImageSelectConfig mGalleryConfig;
    private int mRowWidth;

    private DisplayImageOptions mImageOptions;

    public ImageListAdapter(Activity activity, List<ImageInfo> list, Map<String, ImageInfo> selectList, int screenWidth, ImageHandler.ImageSelectConfig config) {
        super(activity, list);
        this.mSelectList = selectList;
        this.mScreenWidth = screenWidth;
        this.mGalleryConfig = config;
        this.mRowWidth = mScreenWidth/3;
        mImageOptions = ImageUtils.getImageOptions(R.drawable.ic_gf_default_photo);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_image_list_item, parent);
//        setHeight(view);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        ImageInfo imageInfo = mList.get(position);

        String path = "";
        if (imageInfo != null) {
            path = imageInfo.getPath();
        }

        ImageUtils.loadImage(mActivity, path, holder.mIvThumb, mImageOptions);

        boolean checked = mGalleryConfig.isMutiSelect() && mSelectList.get(imageInfo.getPath()) != null;
        holder.setChecked(checked);
    }


//    private void setHeight(final View convertView) {
//        int height = mScreenWidth / 3 - 8;
//        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
//    }

    public static class PhotoViewHolder extends CommonBaseAdapter.ViewHolder {

        public ImageView mIvThumb;
        public ImageView mIvCheck;
        public View mIvIndicator;

        public PhotoViewHolder(View view) {
            super(view);
            mIvThumb = (ImageView) view.findViewById(R.id.iv_thumb);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
            mIvIndicator = view.findViewById(R.id.iv_check_indicator);
        }

        public void setChecked(boolean checked) {
            if (checked) {
                mIvCheck.setVisibility(View.VISIBLE);
                mIvIndicator.setVisibility(View.VISIBLE);
            } else {
                mIvCheck.setVisibility(View.GONE);
                mIvIndicator.setVisibility(View.GONE);
            }
        }

    }
}
