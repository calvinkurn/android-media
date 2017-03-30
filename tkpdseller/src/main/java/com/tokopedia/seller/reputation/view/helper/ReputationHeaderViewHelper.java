package com.tokopedia.seller.reputation.view.helper;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper;

/**
 * Created by normansyahputa on 3/17/17.
 */

public class ReputationHeaderViewHelper extends GMStatHeaderViewHelper {

    public ReputationHeaderViewHelper(View itemView) {
        super(itemView, true);
        stopLoading();
    }

    public void setData(String startDate, String endDate) {
        calendarRange.setText(startDate + " - " + endDate);
        setImageIcon();
        stopLoading();
    }

    @Override
    protected void setImageIcon() {
        Drawable setDateNext = AppCompatDrawableManager.get().getDrawable(itemView.getContext()
                , R.drawable.ic_arrow_right);
        calendarArrowIcon.setImageDrawable(setDateNext);
        calendarIcon.setImageResource(R.mipmap.ic_icon_calendar_02);
    }
}
