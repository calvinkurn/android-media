package com.tokopedia.seller.shopscore.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Created by sebastianuskh on 2/23/17.
 */

public class ShopScoreMainProgressBar extends RoundCornerProgressBar {
    private int progressWidth;

    public ShopScoreMainProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopScoreMainProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth, int radius, int padding, int colorProgress, boolean isReverse) {
        super.drawProgress(layoutProgress, max, progress, totalWidth, radius, padding, colorProgress, isReverse);
        float ratio = max / progress;
        progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
    }

    public int getProgressWidth() {
        return progressWidth;
    }
}
