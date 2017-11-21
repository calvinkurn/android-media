package com.tokopedia.flight.review;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingreviewPriceViewHolder extends BaseViewHolder {

    private TextView priceLabel;
    private TextView priceAmount;

    public FlightBookingreviewPriceViewHolder(View layoutView) {
        super(layoutView);
        priceAmount = (TextView) layoutView.findViewById(R.id.price_amount);
        priceLabel = (TextView) layoutView.findViewById(R.id.price_label);
    }

    @Override
    public void bindObject(Object o) {

    }
}
