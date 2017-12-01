package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingPhoneCodeViewHolder extends BaseViewHolder<FlightBookingPhoneCodeViewModel> {

    private TextView countryPhonCode;
    private TextView countryName;

    public FlightBookingPhoneCodeViewHolder(View itemView) {
        super(itemView);
        countryPhonCode = (TextView) itemView.findViewById(R.id.country_phone_code);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
    }

    @Override
    public void bindObject(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        countryPhonCode.setText(itemView.getContext().getString(R.string.flight_booking_phone_code_label, flightBookingPhoneCodeViewModel.getCountryPhoneCode()));
        countryName.setText(flightBookingPhoneCodeViewModel.getCountryName());
    }
}
