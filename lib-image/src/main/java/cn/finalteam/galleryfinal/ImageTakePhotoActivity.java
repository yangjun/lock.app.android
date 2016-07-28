package cn.finalteam.galleryfinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.FileUtils;

/**
 * Created by wm on 15/12/10.
 */
public class ImageTakePhotoActivity extends ImageBaseActivity {

    private static final int TAKE_REQUEST_CODE = 1001;
    private ImageHandler.ImageTakeConfig mTakeConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null || !check()) {
            finish();
            return;
        }

        mTakeConfig = ImageHandler.getInstance().getTakeConfig();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = getOutputUri(mTakeConfig.getPath());
        if (uri != null) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(captureIntent, TAKE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (ImageHandler.getInstance().getTakeCallback() != null) {
                ImageHandler.getInstance().getTakeCallback().onImageTaked(data);
            }
            setResult(RESULT_OK, data);
        }
        finish();
    }

    private boolean check() {
        if (!DeviceUtils.existSDCard()) {
            toast(getString(R.string.empty_sdcard));
            return false;
        }
        return true;
    }

    private boolean createFolder(String path) {
        return FileUtils.makeFolders(path);
    }

    private Uri getOutputUri(String path) {
        if (TextUtils.isEmpty(mTakeConfig.getPath())) {
            return null;
        }

        boolean suc = createFolder(path);
        if (suc) {
            return Uri.fromFile(new File(path));
        }
        return null;
    }

}
