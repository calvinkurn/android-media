package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerViewHolder extends AbstractViewHolder<FlightBookingPassengerViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_booking_saved_passenger;

    public interface ListenerCheckedSavedPassenger {
        boolean isItemChecked(FlightBookingPassengerViewModel selectedItem);
        void resetItemCheck();
    }

    private ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;

    private TextView txtPassengerName, txtPassengerId;
    private ImageView imgCheck;

    public FlightBookingListPassengerViewHolder(View itemView, ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        super(itemView);
        txtPassengerName = itemView.findViewById(R.id.tv_passenger_name);
        txtPassengerId = itemView.findViewById(R.id.tv_passenger_id);
        imgCheck = itemView.findViewById(R.id.image_checked);
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
    }

    @Override
    public void bind(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedSavedPassenger != null) {
            isItemChecked = listenerCheckedSavedPassenger.isItemChecked(flightBookingPassengerViewModel);
        }

        txtPassengerName.setText(String.format(
                "%s %s",
                flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName()
        ));
        // gone for now, later will use for passport ID
        // txtPassengerId.setText("No. "); // " + flightBookingPassengerViewModel.getPassengerLocalId());
        txtPassengerId.setVisibility(View.GONE);
        if (isItemChecked) {
            imgCheck.setVisibility(View.VISIBLE);
            txtPassengerName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.tkpd_main_green));
            txtPassengerId.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.tkpd_main_green));
        } else {
            imgCheck.setVisibility(View.INVISIBLE);
            txtPassengerName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.font_black_primary_70));
            txtPassengerId.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.font_black_primary_70));
        }
    }
}
