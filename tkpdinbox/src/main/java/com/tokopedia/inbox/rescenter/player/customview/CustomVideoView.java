package com.tokopedia.inbox.rescenter.player.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

/**
 * Created by hangnadi on 3/8/17.
 */

public class CustomVideoView extends VideoView {
    private int mVideoWidth;
    private int mVideoHeight;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSize(int width, int height) {
        mVideoHeight = height;
        mVideoWidth = width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Log.i("@@@", "onMeasure");
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (mVideoWidth * height > width * mVideoHeight) {
                Log.i("hangnadi", "image too tall, correcting");
                height = width * mVideoHeight / mVideoWidth;
            } else if (mVideoWidth * height < width * mVideoHeight) {
                Log.i("hangnadi", "image too wide, correcting");
                width = height * mVideoWidth / mVideoHeight;
            } else {
                 Log.i("hangnadi", "aspect ratio is correct: " + width+"/"+height+"="+ mVideoWidth+"/"+mVideoHeight);
            }
        }
        Log.i("hangnadi", "setting size: " + width + 'x' + height);
        setMeasuredDimension(width, height);
    }
}
