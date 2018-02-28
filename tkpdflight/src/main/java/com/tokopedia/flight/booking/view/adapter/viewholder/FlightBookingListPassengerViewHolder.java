package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;

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

    private Context context;
    private TextView txtPassengerName;
    private ImageView imgPassengerType, imgEdit, imgDelete;

    public FlightBookingListPassengerViewHolder(View itemView, ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        super(itemView);
        txtPassengerName = itemView.findViewById(R.id.tv_passenger_name);
        imgPassengerType = itemView.findViewById(R.id.image_passenger_type);
        imgDelete = itemView.findViewById(R.id.image_passenger_delete);
        imgEdit = itemView.findViewById(R.id.image_passenger_edit);
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
        this.context = itemView.getContext();
    }

    @Override
    public void bind(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {

        txtPassengerName.setText(String.format(
                "%d. %s. %s %s",
                flightBookingPassengerViewModel.getPassengerLocalId(),
                flightBookingPassengerViewModel.getPassengerTitle(),
                flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName()
        ));

        imgPassengerType.setImageDrawable(VectorDrawableCompat.create(context.getResources(), flightBookingPassengerViewModel.getPassengerDrawable(), context.getTheme()));
    }
}
