package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
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
        void deletePassenger(String passengerId);
    }

    private ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;

    private Context context;
    private TextView txtPassengerName;
    private ImageView imgEdit, imgDelete;

    public FlightBookingListPassengerViewHolder(View itemView, final ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        super(itemView);
        txtPassengerName = itemView.findViewById(R.id.tv_passenger_name);
        imgDelete = itemView.findViewById(R.id.image_passenger_delete);
        imgEdit = itemView.findViewById(R.id.image_passenger_edit);
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
        this.context = itemView.getContext();
    }

    @Override
    public void bind(final FlightBookingPassengerViewModel flightBookingPassengerViewModel) {

        txtPassengerName.setText(String.format(
                "%s. %s %s",
                flightBookingPassengerViewModel.getPassengerTitle(),
                flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName()
        ));

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerCheckedSavedPassenger.deletePassenger(flightBookingPassengerViewModel.getPassengerId());
            }
        });
    }
}
