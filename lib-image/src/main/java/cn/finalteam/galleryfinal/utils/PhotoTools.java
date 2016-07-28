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

package cn.finalteam.galleryfinal.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.Consts;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.model.InageFolderInfo;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:26
 */
public class PhotoTools {

    /**
     * 获取所有图片
     * @param context
     * @return
     */
    public static List<InageFolderInfo> getAllPhotoFolder(Context context) {
        List<InageFolderInfo> allFolderList = new ArrayList<>();
        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA
        };
        final ArrayList<InageFolderInfo> allPhotoFolderList = new ArrayList<>();
        HashMap<Integer, InageFolderInfo> bucketMap = new HashMap<>();
        Cursor cursor = null;
        //所有图片
        InageFolderInfo allInageFolderInfo = new InageFolderInfo();
        allInageFolderInfo.setFolderId(0);
        allInageFolderInfo.setFolderName(context.getResources().getString(R.string.all_photo));
        allInageFolderInfo.setPhotoList(new ArrayList<ImageInfo>());
        allPhotoFolderList.add(0, allInageFolderInfo);
        List<String> filterList = getFilterFolders();
        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                while (cursor.moveToNext()) {
                    int bucketId = cursor.getInt(bucketIdColumn);
                    String bucketName = cursor.getString(bucketNameColumn);
                    final int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    final int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    //int thumbImageColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                    final int imageId = cursor.getInt(imageIdColumn);
                    final String path = cursor.getString(dataColumn);
                    //final String thumb = cursor.getString(thumbImageColumn);
                    final ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setId(imageId + "");
                    imageInfo.setPath(path);
                    //imageInfo.setThumbPath(thumb);
                    if (StringUtils.isEmpty(imageInfo.getPath())) {
                        continue;
                    }
                    if ( allInageFolderInfo.getCoverPhoto() == null ) {
                        allInageFolderInfo.setCoverPhoto(imageInfo);
                    }
                    //添加到所有图片
                    allInageFolderInfo.getPhotoList().add(imageInfo);

                    //通过bucketId获取文件夹
                    InageFolderInfo inageFolderInfo = bucketMap.get(bucketId);

                    if (filterList != null && filterList.contains(bucketName)) {
                        continue;
                    }
                    if (inageFolderInfo == null) {
                        inageFolderInfo = new InageFolderInfo();
                        inageFolderInfo.setPhotoList(new ArrayList<ImageInfo>());
                        inageFolderInfo.setFolderId(bucketId);
                        inageFolderInfo.setFolderName(bucketName);
                        inageFolderInfo.setCoverPhoto(imageInfo);
                        bucketMap.put(bucketId, inageFolderInfo);
                        allPhotoFolderList.add(inageFolderInfo);
                    }
                    inageFolderInfo.getPhotoList().add(imageInfo);
                }
            }
        } catch (Exception ex) {
            Logger.e(ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        allFolderList.addAll(allPhotoFolderList);
        return allFolderList;
    }

    private static List<String> getFilterFolders() {
        List<String> result = new ArrayList<>();
        result.add(Consts.TAKE_PHOTO_FOLDER_NAME);
        return result;
    }

}
