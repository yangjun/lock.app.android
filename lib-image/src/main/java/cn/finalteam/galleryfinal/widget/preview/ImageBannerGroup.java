package cn.finalteam.galleryfinal.widget.preview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;

import java.util.List;

import cn.finalteam.galleryfinal.utils.ImageUtils;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.ImageInfo;

/**
 * Created by wm on 15/12/11.
 */
public class ImageBannerGroup<T> extends ConvenientBanner<T> {

    private int mDefualtIcon = R.drawable.ic_gf_default_photo;
    private OnItemClickListener mOnItemClickListener;

    public ImageBannerGroup(Context context) {
        super(context);
    }

    public ImageBannerGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ConvenientBanner setPages(CBViewHolderCreator holderCreator, List<T> datas) {
        if (holderCreator == null) {
            holderCreator = getDefaultCreator();
        }
        return super.setPages(holderCreator, datas);
    }

    public ConvenientBanner setPages(List<T> datas) {
        return setPages(null, datas);
    }

    public void setDefaultIcon(int iconId) {
        this.mDefualtIcon = iconId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private CBViewHolderCreator getDefaultCreator() {
        return new CBViewHolderCreator<ImageHolderView>() {
            @Override
            public ImageHolderView createHolder() {
                return new ImageHolderView(mDefualtIcon, mOnItemClickListener);
            }
        };
    }

    static class ImageHolderView implements CBPageAdapter.Holder<ImageInfo>{

        private ImageView iv;
        private TextView tvTitle;
        private View mView;
        private int defaultIcon;
        private OnItemClickListener itemClickListener;

        ImageHolderView(int defaultIcon, OnItemClickListener listener) {
            this.defaultIcon = defaultIcon;
            this.itemClickListener = listener;
        }

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.gf_widget_banner_item, null);
            iv = (ImageView) view.findViewById(R.id.iv);
            tvTitle = (TextView) view.findViewById(R.id.tv_t);
            mView = view;
            return view;
        }

        @Override
        public void UpdateUI(Context context, final int position, ImageInfo data) {
            ImageUtils.loadImage(context, data.getPath(), iv, ImageUtils.getImageOptions(defaultIcon));
            String title = data.getTitle();
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(GONE);
            } else {
                tvTitle.setVisibility(VISIBLE);
                tvTitle.setText(title);
            }

            if (itemClickListener != null) {
                mView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(position);
                    }
                });
            }
        }
    }

    public static interface OnItemClickListener {
        public void onItemClick(int pos);
    }

}
