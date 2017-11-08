package com.tokopedia.flight.booking.view.adapter;

import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageAdapter extends BaseListAdapter<FlightBookingLuggageViewModel> implements FlightBookingLuggageViewHolder.ListenerCheckedLuggage {

    private String selectedLuggage = "";

    public void setSelectedLuggage(String selectedLuggage) {
        this.selectedLuggage = selectedLuggage;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        FlightBookingLuggageViewHolder flightBookingLuggageViewHolder = new FlightBookingLuggageViewHolder(
                getLayoutView(parent, R.layout.item_flight_booking_luggage));
        flightBookingLuggageViewHolder.setListenerCheckedLuggage(this);
        return flightBookingLuggageViewHolder;
    }

    @Override
    public boolean isItemChecked(String selectedItem) {
        return selectedItem.equals(selectedLuggage);
    }
}
