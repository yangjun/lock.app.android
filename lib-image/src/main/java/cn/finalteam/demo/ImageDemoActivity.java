package cn.finalteam.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.ImageBaseActivity;
import cn.finalteam.galleryfinal.ImageHandler;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.ImageInfo;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import cn.finalteam.galleryfinal.widget.preview.ImageBannerGroup;

public class ImageDemoActivity extends ImageBaseActivity {

    private HorizontalListView mLvPhoto;
    private List<ImageInfo> mPhotoList;
    private ImageDemoAdapter mImageDemoAdapter;
    private Button mOpenGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gf_demo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLvPhoto = (HorizontalListView) findViewById(R.id.lv_photo);
        mPhotoList = new ArrayList<>();
        mImageDemoAdapter = new ImageDemoAdapter(this, mPhotoList);
        mLvPhoto.setAdapter(mImageDemoAdapter);

        // 广告
        final List<ImageInfo> testList = getTestList();
        ImageBannerGroup bannerGroup = (ImageBannerGroup) findViewById(R.id.ibg);
        bannerGroup.setPages(testList)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageTransformer(ConvenientBanner.Transformer.AccordionTransformer);
        bannerGroup.setOnItemClickListener(new ImageBannerGroup.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                ImageHandler.ImagePreviewConfig config = new ImageHandler.ImagePreviewConfig()
                        .setInitList(testList)
                        .setCanEdit(false)
                        .setCurrent(pos);
                ImageHandler.getInstance().preview(ImageDemoActivity.this, config, null);
            }
        });

        // 相册选择
        mOpenGallery = (Button) findViewById(R.id.btn_open_gallery);
        mOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageHandler.ImageSelectConfig config = new ImageHandler.ImageSelectConfig()
                        .setMaxSize(3)
                        .setShowCamera(true)
                        .setInitList(mPhotoList);
                ImageHandler.getInstance().select(ImageDemoActivity.this, config, new ImageHandler.ImageSelectCallback() {
                    @Override
                    public void onImageSelectFinished(List<ImageInfo> result) {
                        if (result != null) {
                            mPhotoList.clear();
                            mPhotoList.addAll(result);
                            mImageDemoAdapter.notifyDataSetChanged();
                            ImageHandler.getInstance().destory();
                        }
                    }
                });
            }
        });

        // splash
        findViewById(R.id.btn_splash).setOnClickListener(new View.OnClickListener() {
            ImageHandler.ImageGuideConfig config = new ImageHandler.ImageGuideConfig()
                    .setCanSkip(true)
                    .setDrawables(new int[] {R.drawable.ic_delete_photo, R.drawable.ic_gf_camera})
                    .setText("进入主页");
            @Override
            public void onClick(View view) {
                ImageHandler.getInstance().guide(ImageDemoActivity.this, config, new ImageHandler.ImageGuideCallback() {
                    @Override
                    public void onImageGuideFinished() {
                        Toast.makeText(ImageDemoActivity.this, "进入主页...", Toast.LENGTH_LONG).show();
                        ImageHandler.getInstance().destory();
                    }
                });
            }
        });
    }

    private List<ImageInfo> getTestList() {
        List<ImageInfo> list = new ArrayList<ImageInfo>();

        ImageInfo item1 = new ImageInfo("1", "http://pic2.ooopic.com/01/03/51/25b1OOOPIC19.jpg");
        item1.setTitle("浙江小伙醉驾飚车至120码 发朋友圈炫技");
        list.add(item1);

        ImageInfo item2 = new ImageInfo("1", "http://img2.3lian.com/img2007/19/33/005.jpg");
        item2.setTitle("教育部拟推多校划片:一个小区对应多所小学");
        list.add(item2);

        ImageInfo item3 = new ImageInfo("1", "http://pic.nipic.com/2007-11-09/200711912453162_2.jpg");
        item3.setTitle("回归iPhone 4风格史上最美iPhone 7设计");
        list.add(item3);

        return list;
    }

}
