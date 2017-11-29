package com.tokopedia.flight.review.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingreviewPriceViewHolder extends BaseViewHolder<SimpleViewModel> {

    private TextView priceLabel;
    private TextView priceAmount;

    @Override
    public void bindObject(SimpleViewModel simpleViewModel) {
        priceAmount.setText(simpleViewModel.getDescription());
        priceLabel.setText(simpleViewModel.getLabel());
    }

    public FlightBookingreviewPriceViewHolder(View layoutView) {
        super(layoutView);
        priceAmount = (TextView) layoutView.findViewById(R.id.price_amount);
        priceLabel = (TextView) layoutView.findViewById(R.id.price_label);
    }
}
