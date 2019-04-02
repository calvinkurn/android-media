package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.tkpdpdp.R;

import java.util.List;

/**
 * @author kris on 11/3/16. Tokopedia
 */

public class ProductVideoHorizontalScroll extends HorizontalScrollView {

    private LinearLayout placeHolder;

    private List<YoutubeThumbnailViewHolder> youtubeThumbnailList;

    public ProductVideoHorizontalScroll(Context context) {
        super(context);
        initView(context);
    }

    public ProductVideoHorizontalScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProductVideoHorizontalScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.horizontal_scroll_youtube_view, this, true);
        placeHolder = (LinearLayout) findViewById(R.id.youtube_video_place_holder);
    }

    public void renderData(VideoData data, YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess loadInProcess) {
        if(YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getContext().getApplicationContext())
                .equals(YouTubeInitializationResult.SUCCESS)) {
            for (int i = 0; i < data.getVideo().size(); i++) {
                placeHolder.addView(new YoutubeThumbnailViewHolder(getContext(), data, i,loadInProcess));
            }
        } else {
            for(int i = 0; i < data.getVideo().size(); i++) {
                placeHolder.addView(new YoutubeWebViewThumbnail(getContext(),
                        data.getVideo().get(i).getUrl()));
            }
        }
    }

    public void clearYoutubeVideo() {
        if(placeHolder.getChildCount() > 0) placeHolder.removeAllViews();
    }

    public void destroyAllOnGoingYoutubeProcess() {
        if (youtubeThumbnailList != null) {
            for (int i = 0; i < youtubeThumbnailList.size(); i++) {
                youtubeThumbnailList.get(i).destroyReleaseProcess();
            }
        }

    }
}