package com.tokopedia.flight.booking.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityViewHolder extends BaseViewHolder<FlightBookingPhoneCodeViewModel> {

    private TextView countryName;

    @Override
    public void bindObject(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        countryName.setText(flightBookingPhoneCodeViewModel.getCountryName());
    }

    public FlightBookingNationalityViewHolder(View layoutView) {
        super(layoutView);
        countryName = (TextView) layoutView.findViewById(R.id.country_name);
    }
}
