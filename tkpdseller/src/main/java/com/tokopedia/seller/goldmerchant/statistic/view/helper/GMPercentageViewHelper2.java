package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.GMStatConstant;
import com.tokopedia.seller.gmstat.utils.KMNumbers;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.PERCENTAGE_FORMAT;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMPercentageViewHelper2 extends BaseGMViewHelper implements PercentageUtil {
    private final int arrowDown;
    private final int arrowUp;
    private final int greyColor;

    private Drawable icRectagleDown;

    private Drawable icRectagleUp;

    public GMPercentageViewHelper2(@Nullable Context context) {
        super(context);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(context,
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(context,
                R.drawable.ic_rectangle_up);

        arrowDown = ResourcesCompat.getColor(context.getResources(), R.color.arrow_down, null);
        arrowUp = ResourcesCompat.getColor(context.getResources(), R.color.arrow_up, null);
        greyColor = ResourcesCompat.getColor(context.getResources(), R.color.grey_400, null);
    }

    @Override
    public void calculatePercentage(double percentage, ImageView ivArrowIcon, TextView tvPercentage) {
        // image for arrow is here
        boolean isDefault;
        if (percentage == 0) {
            ivArrowIcon.setVisibility(View.GONE);
            tvPercentage.setTextColor(arrowUp);
            isDefault = true;
        } else if (percentage < 0) {// down here
            if (percentage == GMStatConstant.NoDataAvailable * 100) {
                ivArrowIcon.setVisibility(View.GONE);
                tvPercentage.setTextColor(greyColor);
                isDefault = false;
            } else {
                ivArrowIcon.setVisibility(View.VISIBLE);
                ivArrowIcon.setImageDrawable(icRectagleDown);
                tvPercentage.setTextColor(arrowDown);
                isDefault = true;
            }
        } else {// up here
            ivArrowIcon.setVisibility(View.VISIBLE);
            ivArrowIcon.setImageDrawable(icRectagleUp);
            tvPercentage.setTextColor(arrowUp);
            isDefault = true;
        }

        if (isDefault) {
            tvPercentage.setText(String.format(PERCENTAGE_FORMAT, KMNumbers.formatString(percentage).replace("-", "")));
        } else {
            tvPercentage.setText(R.string.no_data);
        }
    }

    @Override
    public void initView(@Nullable View itemView) {

    }

    @Override
    public void bind(@Nullable Object data) { /* remain empty */ }
}
