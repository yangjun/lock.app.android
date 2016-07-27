package com.wm.lock.core.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理工具类
 * @author Kevin
 *
 */
public class ImageUtils {

	public static void recycle(ImageView iv) {
		if (iv == null) {
			return;
		}
		Drawable drawable = iv.getDrawable();
		if (drawable != null) {
			iv.setImageDrawable(null);
			if (drawable instanceof BitmapDrawable) {
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}

	/**
	 * 处理图片的大小
	 * @param bitmap 传入的bitmap值
	 * @param w 需要的宽度
	 * @param h 需要的高度
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		float scale = Math.max(scaleWidth, scaleHeight);

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scale, scale);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        return Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

	}
	
	public static Bitmap decodeSampledBitmapFromInputStream(InputStream is,
	        int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(is, null, options);
	    // 调用上面定义的方法计算inSampleSize值
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeStream(is, null, options);
	}
	
	public static Bitmap decodeSampledBitmapFromInputStreamS(InputStream is,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(is, null, options);
	    // 调用上面定义的方法计算inSampleSize值
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeStream(is, null, options);
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    // 调用上面定义的方法计算inSampleSize值
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String pathName,
	        int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    final BitmapFactory.Options options = new BitmapFactory.Options();
//	    Bitmap bitmap = BitmapFactory.decodeFile(pathName);
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);
	    // 调用上面定义的方法计算inSampleSize值
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(pathName, options);
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	/**
	 * 处理图片质量
	 * @param fromPath 处理前图片的来源路径
	 * @param toPath 处理完图片保存路径
	 */
	public static boolean decodeSampledBitmapToPath(Context context, String fromPath, String toPath){
//		Bitmap bitmap = null;
//		if (fromPath.contains("content://")) {
//			ContentResolver cr = context.getContentResolver();
//			try {
//				bitmap = resizeImage(BitmapFactory.decodeStream(cr.openInputStream(Uri.parse(fromPath))), 135, 240);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}else {
//			bitmap = decodeSampledBitmapFromFile(fromPath, 100, 100);
//		}
		
		File folder = new File(toPath);
        if (!folder.getParentFile().exists()) {
            folder.getParentFile().mkdirs();
        }
		
        Bitmap bitmap = null;
        try {
			FileOutputStream out = new FileOutputStream(folder);
			bitmap = decodeSampledBitmapFromFile(fromPath, 100, 100);
			Boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			return isSuccess;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != bitmap && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}
	
	private static String getFolder(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return null;
        }
        int i = filePath.lastIndexOf("/");
        int j = filePath.lastIndexOf("\\");
        int index = i > j ? i : j;
        return filePath.substring(0, index);
    }
}
