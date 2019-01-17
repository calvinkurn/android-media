package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tokopedia.tkpdpdp.R;



/**
 * Created by kris on 11/22/16. Tokopedia
 */

public class YoutubeWebViewThumbnail extends RelativeLayout{

    private ImageView thumbnail;


    public YoutubeWebViewThumbnail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public YoutubeWebViewThumbnail(Context context) {
        super(context);
    }

    public YoutubeWebViewThumbnail(Context context, String videoId) {
        super(context);
        initView(context, videoId);
    }

    public YoutubeWebViewThumbnail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView (Context context, String videoId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.youtube_thumbnail_webview, this, true);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        Glide.with(getContext())
                .load("http://img.youtube.com/vi/"+videoId+"/1.jpg").into(thumbnail);

        setOnClickListener(onThumnailClickedListener(videoId));
    }

    private OnClickListener onThumnailClickedListener(final String videoId) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + videoId)));
                } catch (Throwable throwable){
                    Toast.makeText(getContext(), R.string.no_support_application, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

}