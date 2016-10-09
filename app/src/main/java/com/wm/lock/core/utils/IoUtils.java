package com.wm.lock.core.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IoUtils {

    /**
     * 得到指定文件路径的父目录
     *
     * @param filePath 绝对路径
     * @return
     */
    public static String getFolder(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return null;
        }
        int i = filePath.lastIndexOf("/");
        int j = filePath.lastIndexOf("\\");
        int index = i > j ? i : j;
        return filePath.substring(0, index);
    }

    /**
     * 删除指定文件夹及其下文件
     *
     * @param folder
     * @return
     */
    public static boolean deleteFiles(File folder) {
        if (folder == null || !folder.exists()) {
            return true;
        } else if (folder.isFile()) {
            File parent = folder.getParentFile();
            boolean result = folder.delete();
            if (result && null != parent && parent.listFiles().length == 0) {
                return parent.delete();
            }
            return result;
        } else {
            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                return folder.delete();
            }

            for (File file : files) {
                if (!deleteFiles(file)) {
                    return false;
                }
            }
            return folder.delete();
        }
    }

    /**
     * 删除指定文件夹及其下文件
     *
     * @param folder
     * @return
     */
    public static boolean deleteFiles(String folderPath) {
        File folder = new File(IoUtils.getFolder(folderPath));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return deleteFiles(folder);
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 把路径转换成Uri，格式示例：file:///+path
     *
     * @param path
     * @return
     */
    public static String getUri(String path) {
        try {
            return path.contains("file:") ? path : String.valueOf(Uri.fromFile(new File(path)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从Uri获取实际路径
     *
     * @param uri
     * @return
     */
    public static String getPath(Uri uri) {
        try {
            return uri.getPath();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从Uri获取实际路径，去掉file:///
     *
     * @param uri
     * @return
     */
    public static String getPath(String uri) {
        return getPath(Uri.parse(uri));
    }

    public static void createFolder(String folderPath) {
        File folder = new File(IoUtils.getFolder(folderPath));
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 保存图片到指定路径
     *
     * @param bitmap
     * @param path
     */
    public static boolean saveBitmapToPath(Bitmap bitmap, String path) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return false;
    }

}
