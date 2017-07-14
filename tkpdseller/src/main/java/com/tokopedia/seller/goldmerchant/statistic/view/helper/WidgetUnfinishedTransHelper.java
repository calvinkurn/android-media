package com.tokopedia.seller.goldmerchant.statistic.view.helper;

import android.support.v7.widget.AppCompatImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.UnFinishedTransViewHelperModel;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class WidgetUnfinishedTransHelper {
    private final AppCompatImageView unFinishedTransDesc;
    private final TextView unFinishedDescLeftText;
    private final TextView unFinishedDescRightText;

    public WidgetUnfinishedTransHelper(RelativeLayout parent) {
        unFinishedTransDesc = (AppCompatImageView) parent.findViewById(R.id.unfinished_transaction_description_image);
        unFinishedDescLeftText = (TextView) parent.findViewById(R.id.unfinished_transaction_description_left_text);
        unFinishedDescRightText = (TextView) parent.findViewById(R.id.unfinished_transaction_description_right_text);
    }

    public void bind(UnFinishedTransViewHelperModel unFinishedTransViewHelperModel) {
        unFinishedDescLeftText.setText(unFinishedTransViewHelperModel.leftText);
        unFinishedDescRightText.setText(unFinishedTransViewHelperModel.rightText);
        unFinishedTransDesc.setImageResource(unFinishedTransViewHelperModel.drawableRes);
    }
}
