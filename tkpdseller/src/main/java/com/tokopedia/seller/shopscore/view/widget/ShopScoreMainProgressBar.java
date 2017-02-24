package com.tokopedia.seller.shopscore.view.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Created by sebastianuskh on 2/23/17.
 */

public class ShopScoreMainProgressBar extends RoundCornerProgressBar {
    private float limit;
    private ShopScoreMainProgressBarListener parent;

    public ShopScoreMainProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopScoreMainProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initListener() {
        if (getParent() instanceof ShopScoreMainProgressBarListener) {
            parent = (ShopScoreMainProgressBarListener) getParent();
        } else {
            throw new RuntimeException("implement ShopScoreMainProgressBarListener to its parent");
        }
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initListener();
    }

    public void setLimit(float limit) {
        this.limit = limit;
        parent.updateLimitViewOutside(calculateProgressWidth(limit));
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        parent.updateLimitViewOutside(calculateProgressWidth(limit));
    }

    private int calculateProgressWidth(float progress) {
        float ratio = getMax() / progress;
        return (int) ((getLayoutWidth() - (getPadding() * 2)) / ratio);
    }
}
