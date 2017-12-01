package com.tokopedia.flight.booking.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingAmenityViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityAdapter extends BaseListAdapter<FlightBookingAmenityViewModel> implements FlightBookingAmenityViewHolder.ListenerCheckedLuggage {

    private List<FlightBookingAmenityViewModel> selectedLuggage;

    public FlightBookingAmenityAdapter(Context context, OnBaseListV2AdapterListener<FlightBookingAmenityViewModel> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
        selectedLuggage = new ArrayList<>();
    }

    public void setSelectedLuggage(List<FlightBookingAmenityViewModel> selectedLuggage) {
        this.selectedLuggage = selectedLuggage;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        FlightBookingAmenityViewHolder flightBookingAmenityViewHolder = new FlightBookingAmenityViewHolder(
                getLayoutView(parent, R.layout.item_flight_booking_amenity));
        flightBookingAmenityViewHolder.setListenerCheckedLuggage(this);
        return flightBookingAmenityViewHolder;
    }

    @Override
    public boolean isItemChecked(FlightBookingAmenityViewModel selectedItem) {
        return selectedLuggage.contains(selectedItem);
    }
}
