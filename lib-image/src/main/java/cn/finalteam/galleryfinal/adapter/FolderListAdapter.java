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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.finalteam.galleryfinal.utils.ImageUtils;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.InageFolderInfo;
import cn.finalteam.galleryfinal.model.ImageInfo;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:09
 */
public class FolderListAdapter extends CommonBaseAdapter<FolderListAdapter.FolderViewHolder, InageFolderInfo> {

    private InageFolderInfo mSelectFolder;

    public FolderListAdapter(Activity activity, List<InageFolderInfo> list) {
        super(activity, list);
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_folder_list_item, parent);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        InageFolderInfo inageFolderInfo = mList.get(position);

        String path = "";
        ImageInfo imageInfo = inageFolderInfo.getCoverPhoto();
        if (imageInfo != null) {
            path = imageInfo.getPath();
        }
        ImageUtils.loadImage(mActivity, path, holder.mIvCover, ImageUtils.getImageOptions(R.drawable.ic_gf_default_photo));

        holder.mTvFolderName.setText(inageFolderInfo.getFolderName());
        int size = 0;
        if ( inageFolderInfo.getPhotoList() != null ) {
            size = inageFolderInfo.getPhotoList().size();
        }
        holder.mTvPhotoCount.setText(mActivity.getString(R.string.folder_photo_size, size));

        if (mSelectFolder == inageFolderInfo || (mSelectFolder == null && position == 0)) {
            TypedValue typedValue = new TypedValue();
//            mActivity.getTheme().resolveAttribute(R.attr.colorTheme, typedValue, true);
            int colorTheme = typedValue.data;
//            holder.mIvFolderCheck.setImageDrawable(createCheckIcon(colorTheme, R.drawable.ic_folder_check));
            holder.mIvFolderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.mIvFolderCheck.setVisibility(View.GONE);
        }
    }

    public void setSelectFolder(InageFolderInfo inageFolderInfo) {
        this.mSelectFolder = inageFolderInfo;
    }

    public InageFolderInfo getSelectFolder() {
        return mSelectFolder;
    }

    static class FolderViewHolder extends CommonBaseAdapter.ViewHolder {
        ImageView mIvCover;
        ImageView mIvFolderCheck;
        TextView mTvFolderName;
        TextView mTvPhotoCount;

        public FolderViewHolder(View view) {
            super(view);
            mIvCover = (ImageView) view.findViewById(R.id.iv_cover);
            mTvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
            mTvPhotoCount = (TextView) view.findViewById(R.id.tv_photo_count);
            mIvFolderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
        }
    }
}
