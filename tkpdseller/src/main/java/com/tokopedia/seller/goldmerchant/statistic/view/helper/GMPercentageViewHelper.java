package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
 * Created by normansyahputa on 7/10/17.
 */

public class GMPercentageViewHelper extends BaseGMViewHelper<Double> {

    private final int arrowDown;
    private final int arrowUp;
    private final int greyColor;

    private Drawable icRectagleDown;

    private Drawable icRectagleUp;
    private View itemView;

    private TextView percentageText;
    private ImageView arrowIconImage;

    public GMPercentageViewHelper(Context context) {
        super(context);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(context,
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(context,
                R.drawable.ic_rectangle_up);

        arrowDown = ResourcesCompat.getColor(context.getResources(), R.color.arrow_down, null);
        arrowUp = ResourcesCompat.getColor(context.getResources(), R.color.arrow_up, null);
        greyColor = ResourcesCompat.getColor(context.getResources(), R.color.grey_400, null);
    }

    public void initView(View itemView) {
        this.itemView = itemView; // save view

        percentageText = (TextView) itemView.findViewById(R.id.percentage);
        arrowIconImage = (ImageView) itemView.findViewById(R.id.arrow_icon);
    }

    @Override
    public void bind(Double percentage) {
        // image for arrow is here
        boolean isDefault;
        if (percentage == 0) {
            arrowIconImage.setVisibility(View.GONE);
            percentageText.setTextColor(arrowUp);
            isDefault = true;
        } else if (percentage < 0) {// down here
            if (percentage == GMStatConstant.NoDataAvailable * 100) {
                arrowIconImage.setVisibility(View.GONE);
                percentageText.setTextColor(greyColor);
                isDefault = false;
            } else {
                arrowIconImage.setVisibility(View.VISIBLE);
                arrowIconImage.setImageDrawable(icRectagleDown);
                percentageText.setTextColor(arrowDown);
                isDefault = true;
            }
        } else {// up here
            arrowIconImage.setVisibility(View.VISIBLE);
            arrowIconImage.setImageDrawable(icRectagleUp);
            percentageText.setTextColor(arrowUp);
            isDefault = true;
        }

        if (isDefault) {
            percentageText.setText(String.format(PERCENTAGE_FORMAT, KMNumbers.formatString(percentage).replace("-", "")));
        } else {
            percentageText.setText(R.string.no_data);
        }
    }
}
