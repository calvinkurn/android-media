package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.flight.R;

/**
 * @author  by alvarisi on 11/17/17.
 */

public class FlightBookingPassengerFacilityAdapter extends RecyclerView.Adapter<FlightBookingPassengerFacilityAdapter.ViewHolder> {
    public FlightBookingPassengerFacilityAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_booking_passenger, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
