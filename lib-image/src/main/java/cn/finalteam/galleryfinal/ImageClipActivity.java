package cn.finalteam.galleryfinal;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wm.clipimage.ClipImageLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.utils.ImageUtils;

/**
 * Created by wm on 15/9/1.
 */
public class ImageClipActivity extends ImageBaseActivity {

    private ClipImageLayout mClipImageLayout;
    private ImageHandler.ImageEditConfig mEditConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            finish();
            return;
        }

        setContentView(R.layout.gf_activity_image_clip);

        mEditConfig = ImageHandler.getInstance().getEditConfig();
        if (check()) {
            setTitle(getString(R.string.photo_crop));
            mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
            mClipImageLayout.setImageUri(Uri.fromFile(new File(mEditConfig.getItem().getPath())));
            mClipImageLayout.setMaxWidth(mEditConfig.getMaxWidth());
        }
    }

    private boolean check() {
        File file = new File(mEditConfig.getItem().getPath());
        if (!file.exists()) {
            cropFail(new RuntimeException("original file not found"));
            return false;
        }

        String savePath = mEditConfig.getSavePath();
        if (!ImageUtils.createFolder(savePath)) {
            cropFail(new RuntimeException("save path is unCollrectly"));
            return false;
        }
        return true;
    }

    private void cropSuccess() {
        ImageInfo item = mEditConfig.getItem();
        item.setPath(mEditConfig.getSavePath());
        ImageHandler.getInstance().getEditCallback().onImageEditSuccess(item);
        setResult(RESULT_OK);
        finish();
    }

    private void cropFail(Exception e) {
        ImageHandler.getInstance().getEditCallback().onImageEditFail(e);
        setResult(RESULT_CANCELED);
        finish();
    }

    private void save() {
        Bitmap bitmap = mClipImageLayout.clip();
        BufferedOutputStream bos = null;
        try {
            File myCaptureFile = new File(mEditConfig.getSavePath());
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            cropSuccess();
        } catch (Exception e) {
            cropFail(e);
        } finally {
            try {
                if (null != bitmap && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (null != bos) {
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.finish) {
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gf_clip, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
