package cn.finalteam.galleryfinal;

import android.os.Bundle;
import android.view.View;

import cn.finalteam.galleryfinal.widget.guide.ImageGuideGroup;

/**
 * Created by wm on 15/12/10.
 */
public class ImageGuideActivity extends ImageBaseActivity {

    private ImageHandler.ImageGuideConfig mTakeConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            finish();
            return;
        }

        setContentView(R.layout.gf_activity_guide);

        mTakeConfig = ImageHandler.getInstance().getGuideConfig();
        ImageGuideGroup guideGroup = (ImageGuideGroup) findViewById(R.id.gg);
        guideGroup.setGuideIcons(mTakeConfig.getDrawables());
        guideGroup.setText(mTakeConfig.getText());
        guideGroup.setCanSkip(mTakeConfig.isCanSkip());
        guideGroup.setOnStartAppClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ImageHandler.getInstance().getGuideCallback().onImageGuideFinished();
            }
        });
    }

}
