package cn.finalteam.galleryfinal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wm on 15/12/10.
 */
public final class ImageUtils {

    public static void loadImage(Context ctx, String uri, ImageView view, DisplayImageOptions options) {
        loadImage(ctx, uri, view, options, null);
    }

    public static void loadImage(Context ctx, String uri, ImageView view, DisplayImageOptions options, ImageLoadingListener listener) {
        initImageLoader(ctx);
        ImageLoader.getInstance().displayImage(getUri(uri), view, options, listener);
    }

    public static Drawable getRoundDrawable(Context ctx, int iconId, int margin) {
        Bitmap bm = ((BitmapDrawable) (ctx.getResources().getDrawable(iconId))).getBitmap();
        return new RoundedBitmapDisplayer.RoundedDrawable(bm, 255, margin);
    }

    public static Drawable getRoundDrawable(Context ctx, Bitmap bm, int margin) {
        return new RoundedBitmapDisplayer.RoundedDrawable(bm, 255, margin);
    }

    public static DisplayImageOptions getImageOptions(int iconId) {
        return getDisplayImageOptions(iconId, null);
    }

    public static DisplayImageOptions getRoundImageOptions(int iconId) {
        return getDisplayImageOptions(iconId, new RoundedBitmapDisplayer(255));
    }

    public static DisplayImageOptions getRoundImageOptions(Drawable icon) {
        return getDisplayImageOptions(icon, new RoundedBitmapDisplayer(255));
    }

    public static DisplayImageOptions getRoundCornerImageOptions(int iconId) {
        return getDisplayImageOptions(iconId, new RoundedBitmapDisplayer(20));
    }

    public static void clearCache() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
        }
    }

    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 保存Bitmap到文件
     * @param bitmap
     * @param target
     */
    public static void saveBitmap(Bitmap bitmap, File target) {
        if (target.exists()) {
            target.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static Bitmap rotateBitmap(String path, int orientation, int screenWidth, int screenHeight) {
//        Bitmap bitmap = null;
//        final int maxWidth = screenWidth / 2;
//        final int maxHeight = screenHeight / 2;
//        try {
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(path, options);
//            int sourceWidth, sourceHeight;
//            if (orientation == 90 || orientation == 270) {
//                sourceWidth = options.outHeight;
//                sourceHeight = options.outWidth;
//            } else {
//                sourceWidth = options.outWidth;
//                sourceHeight = options.outHeight;
//            }
//            boolean compress = false;
//            if (sourceWidth > maxWidth || sourceHeight > maxHeight) {
//                float widthRatio = (float) sourceWidth / (float) maxWidth;
//                float heightRatio = (float) sourceHeight / (float) maxHeight;
//
//                options.inJustDecodeBounds = false;
//                if (new File(path).length() > 512000) {
//                    float maxRatio = Math.max(widthRatio, heightRatio);
//                    options.inSampleSize = (int) maxRatio;
//                    compress = true;
//                }
//                bitmap = BitmapFactory.decodeFile(path, options);
//            } else {
//                bitmap = BitmapFactory.decodeFile(path);
//            }
//            if (orientation > 0) {
//                Matrix matrix = new Matrix();
//                //matrix.postScale(sourceWidth, sourceHeight);
//                matrix.postRotate(orientation);
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            }
//            sourceWidth = bitmap.getWidth();
//            sourceHeight = bitmap.getHeight();
//            if ((sourceWidth > maxWidth || sourceHeight > maxHeight) && compress) {
//                float widthRatio = (float) sourceWidth / (float) maxWidth;
//                float heightRatio = (float) sourceHeight / (float) maxHeight;
//                float maxRatio = Math.max(widthRatio, heightRatio);
//                sourceWidth = (int) ((float) sourceWidth / maxRatio);
//                sourceHeight = (int) ((float) sourceHeight / maxRatio);
//                Bitmap bm = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true);
//                bitmap.recycle();
//                return bm;
//            }
//        } catch (Exception e) {
//        }
//        return bitmap;
//    }

    public static boolean createFolder(String path) {
        try {
            String folderPath = getFolder(path);
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getFolder(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return null;
        }
        int i = filePath.lastIndexOf("/");
        int j = filePath.lastIndexOf("\\");
        int index = i > j ? i : j;
        return filePath.substring(0, index);
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 将Bitmap保存到指定文件中，并进行压缩
     *
     * @param bmp
     *            Bitmap
     * @param path
     *            要保存的路径
     * @param quality
     *            压缩比
     */
    public static void save(Bitmap bmp, String path, int quality) throws Exception {
        FileOutputStream fos = null;
        try {
            createFolder(path);
            fos = new FileOutputStream(path);
            if (bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos)) {
                fos.flush();
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap toBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * decodeStream最大的秘密在于其直接调用JNI>>nativeDecodeAsset()来完成decode，
     * 无需再使用java层的createBitmap，从而节省了java层的空间
     *
     * @param filePath
     *            文件路径
     */
    public static Bitmap getLocalBitmap(String filePath) throws Exception {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(new File(filePath));
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 转换Drawable-->Bitmap
     */
    public static Bitmap bitmapFromDrawable(Drawable drawable) {
        if (null != drawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
    }

    /**
     * 转换Bitmap-->Drawable
     */
    public static Drawable drawableFromBitmap(Bitmap bitmap) {
        BitmapDrawable drawable = null;
        if (null != bitmap) {
            drawable = new BitmapDrawable(bitmap);
        }
        return drawable;
    }

    public static Bitmap zoom(Bitmap bm, int widthLimit, int heightLimit) {
        if (bm == null) {
            return null;
        }

        Bitmap newbm = null;
        try {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) widthLimit) / width;
            float scaleHeight = ((float) heightLimit) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        } finally {
            release(newbm);
        }
        return newbm;
    }

    public static Bitmap zoom(String path, int widthLimit, int heightLimit, boolean considerRotate) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);//此时返回bm为空
        final int degree = getBitmapDegree(path);

        float be = 0.0f;
        if (considerRotate && (degree == 90 || degree == 270)) {
            be = getZoomLevel(newOpts.outHeight, newOpts.outWidth, widthLimit, heightLimit);
        } else {
            be = getZoomLevel(newOpts.outWidth, newOpts.outHeight, widthLimit, heightLimit);
        }

        // 先压缩到较小比例
        newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = (int) be;
        newOpts.inJustDecodeBounds = false;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(path, newOpts);

        if (considerRotate) {
            bitmap = rotateByDegree(bitmap, degree);
        }

        // 再压缩到指定比例
        be = getZoomLevel(bitmap.getWidth(), bitmap.getHeight(), widthLimit, heightLimit);
        if (be > 1.0f) {
            be = 1.0f / be;
            Matrix matrix = new Matrix();
            matrix.postScale(be, be);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    private static float getZoomLevel(int w, int h, int wMax, int hMax) {
        float result = 1.0f;
        // 如果宽度大的话根据宽度固定大小缩放
        if (hMax == 0 || (w >= h && w > wMax)) {
            result = (float) (w * 1.0 / wMax);
        }
        // 如果高度高的话根据宽度固定大小缩放
        else if (wMax == 0 || (w <= h && h > hMax)) {
            result = (float) (h * 1.0 / hMax);
        }
        return result;
    }

    public static void zoomAndSave(String originalPath,
                                   String newPath,
                                   int maxWidth,
                                   int maxHeight,
                                   int quality) throws Exception {
        Bitmap bitmap = null;
        try {
            bitmap = zoom(originalPath, maxWidth, maxHeight, true);
            save(bitmap, newPath, quality);
        } finally {
            release(bitmap);
        }
    }

    public static void release(Bitmap bm) {
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateByDegree(Bitmap bm, int degree) {
        if (degree > 0) {
            // 根据旋转角度，生成旋转矩阵
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        return bm;
    }

    private static DisplayImageOptions getDisplayImageOptions(int iconId, BitmapDisplayer displayer) {
        DisplayImageOptions.Builder builder = getBaseBuilder();
        if (displayer != null) {
            builder.displayer(displayer);
        }
        if (iconId > 0) {
            builder.showImageOnLoading(iconId)
                    .showImageOnFail(iconId)
                    .showImageForEmptyUri(iconId);
        }
        return builder.build();
    }

    private static DisplayImageOptions getDisplayImageOptions(Drawable icon, BitmapDisplayer displayer) {
        DisplayImageOptions.Builder builder = getBaseBuilder();
        if (displayer != null) {
            builder.displayer(displayer);
        }
        if (icon != null) {
            builder.showImageOnLoading(icon)
                    .showImageOnFail(icon)
                    .showImageForEmptyUri(icon);
        }
        return builder.build();
    }

    private static DisplayImageOptions.Builder getBaseBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true);
        return builder;
    }

    private static void initImageLoader(Context ctx) {
        if (!ImageLoader.getInstance().isInited()) {
            //ImageLoader
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx.getApplicationContext())
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .threadPoolSize(5)
                    .memoryCache(new WeakMemoryCache())
                    .denyCacheImageMultipleSizesInMemory()
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.FIFO)
                    .writeDebugLogs() // Remove for release app
                    .build();
            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config);
        }
    }

    private static String getUri(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        else if (uri.startsWith("http")) {
            return uri;
        }
        else {
            return "file://" + uri;
        }
    }

}
