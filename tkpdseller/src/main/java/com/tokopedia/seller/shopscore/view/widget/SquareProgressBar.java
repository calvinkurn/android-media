package com.tokopedia.seller.shopscore.view.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Created by sebastianuskh on 2/27/17.
 */
public class SquareProgressBar extends RoundCornerProgressBar {
    public SquareProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth, int radius, int padding, int colorProgress, boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, 0, 0, 0, 0, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }
}
