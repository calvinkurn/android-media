package com.tokopedia.flight.passenger.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingNewPassengerViewModel;

/**
 * @author by furqan on 07/03/18.
 */

public class FlightBookingNewPassengerViewHolder extends AbstractViewHolder<FlightBookingNewPassengerViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_booking_new_passenger;

    public interface ListenerClickedNewPassenger {
        View.OnClickListener onNewPassengerClicked();
    }

    TextView txtTitle;
    private ListenerClickedNewPassenger listenerClickedNewPassenger;

    public FlightBookingNewPassengerViewHolder(View itemView, ListenerClickedNewPassenger listenerClickedNewPassenger) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txt_new_passenger);
        this.listenerClickedNewPassenger = listenerClickedNewPassenger;
    }

    @Override
    public void bind(FlightBookingNewPassengerViewModel element) {
        txtTitle.setText(element.getTitle());
        txtTitle.setOnClickListener(listenerClickedNewPassenger.onNewPassengerClicked());
    }
}
