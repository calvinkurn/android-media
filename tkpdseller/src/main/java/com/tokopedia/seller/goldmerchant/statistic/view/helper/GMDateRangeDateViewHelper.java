package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMDateRangeDateViewModel;

/**
 * Created by normansyahputa on 7/10/17.
 */

public class GMDateRangeDateViewHelper extends BaseGMViewHelper<GMDateRangeDateViewModel> {

    private ImageView dateRangePercentageDotImage;
    private TextView dateRangePercentageDotText;

    @DrawableRes
    private int circleGreen = R.drawable.circle_green;
    @DrawableRes
    private int circleGrey = R.drawable.circle_grey;

    public GMDateRangeDateViewHelper(@Nullable Context context) {
        super(context);

    }

    @Override
    public void initView(@Nullable View itemView) {
        dateRangePercentageDotImage = (ImageView) itemView.findViewById(R.id.date_range_percentage_dot_image);
        dateRangePercentageDotText = (TextView) itemView.findViewById(R.id.date_range_percentage_dot_text);
    }

    @Override
    public void bind(@Nullable GMDateRangeDateViewModel data) {
        // currently not used
        String dateRangeFormatString = context.getString(
                R.string.gold_merchant_date_range_format_text,
                data.getStartDate().getModel2(),
                data.getEndDate().getModel2());

        dateRangePercentageDotText.setText(dateRangeFormatString);
    }

    public void setDrawable(@DrawableRes int drawableRes) {
        dateRangePercentageDotImage.setImageResource(drawableRes);
    }
}
