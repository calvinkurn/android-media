package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tokopedia.topads.sdk.imageutils.RecyclingImageView;

/**
 * @author by errysuprayogi on 3/27/17.
 */

public class SquareImageView extends RecyclingImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}