package com.tokopedia.seller.lib.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.utils.GoldMerchantDateUtils;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMDateRangeDateViewModel;

/**
 * Created by normansyahputa on 7/11/17.
 */

public class GMDateRangeView extends RelativeLayout {
    private ImageView dateRangePercentageDotImage;
    private TextView dateRangePercentageDotText;

    @DrawableRes
    private int circleGreen = R.drawable.circle_green;
    @DrawableRes
    private int circleGrey = R.drawable.circle_grey;

    private String[] monthNamesAbrev;

    public GMDateRangeView(Context context) {
        super(context);
        init();
    }

    public GMDateRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GMDateRangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_date_range_percentage, this);
        dateRangePercentageDotImage = (ImageView) view.findViewById(R.id.date_range_percentage_dot_image);
        dateRangePercentageDotText = (TextView) view.findViewById(R.id.date_range_percentage_dot_text);
        dateRangePercentageDotImage.setVisibility(View.GONE);

        monthNamesAbrev = getContext().getResources().getStringArray(R.array.lib_date_picker_month_entries);
    }

    public void bind(@Nullable GMDateRangeDateViewModel data) {
        if (data == null) {
            dateRangePercentageDotText.setVisibility(View.GONE);
            dateRangePercentageDotImage.setVisibility(View.GONE);
        } else {
            String startDate = GoldMerchantDateUtils.getDateWithoutYear(GoldMerchantDateUtils.getDateFormatForInput(data.getStartDate()), monthNamesAbrev);
            String endDate = GoldMerchantDateUtils.getDateWithYear(GoldMerchantDateUtils.getDateFormatForInput(data.getEndDate()), monthNamesAbrev);

            String dateRangeFormatString = getContext().getString(
                    R.string.gold_merchant_date_range_format_text,
                    startDate,
                    endDate);

            dateRangePercentageDotText.setText(dateRangeFormatString);
            dateRangePercentageDotText.setVisibility(View.VISIBLE);
            dateRangePercentageDotImage.setVisibility(View.VISIBLE);
        }
    }

    public void setDrawable(@DrawableRes int drawableRes) {
        dateRangePercentageDotImage.setImageResource(drawableRes);
    }
}
