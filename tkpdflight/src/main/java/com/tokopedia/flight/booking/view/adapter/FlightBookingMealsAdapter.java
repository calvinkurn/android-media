package com.tokopedia.flight.booking.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingMealViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsAdapter extends BaseListAdapter<FlightBookingAmenityViewModel> implements FlightBookingMealViewHolder.ListenerCheckedLuggage {

    private List<FlightBookingAmenityViewModel> selectedFlightBookingViewModels;

    public FlightBookingMealsAdapter(Context context, OnBaseListV2AdapterListener<FlightBookingAmenityViewModel> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
        selectedFlightBookingViewModels = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        FlightBookingMealViewHolder flightBookingLuggageViewHolder = new FlightBookingMealViewHolder(
                getLayoutView(parent, R.layout.item_flight_booking_amenity));
        flightBookingLuggageViewHolder.setListenerCheckedLuggage(this);
        return flightBookingLuggageViewHolder;
    }

    @Override
    public boolean isItemChecked(FlightBookingAmenityViewModel selectedItem) {
        return selectedFlightBookingViewModels.contains(selectedItem);
    }

    public void setSelectedViewModels(List<FlightBookingAmenityViewModel> selectedFlightBookingViewModels) {
        this.selectedFlightBookingViewModels = selectedFlightBookingViewModels;
    }
}
