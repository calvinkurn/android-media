package com.tokopedia.flight.booking.view.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageAdapter extends BaseListV2Adapter<FlightBookingLuggageViewModel> implements FlightBookingLuggageViewHolder.ListenerCheckedLuggage {

    private String selectedLuggage = "";

    public FlightBookingLuggageAdapter(OnBaseListV2AdapterListener<FlightBookingLuggageViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightBookingLuggageAdapter(@Nullable List<FlightBookingLuggageViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightBookingLuggageViewModel> onBaseListV2AdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener);
    }

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
