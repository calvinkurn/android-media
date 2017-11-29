package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

/**
 * Created by alvarisi on 11/27/17.
 */

public class FlightBookingMealViewHolder extends BaseViewHolder<FlightBookingAmenityViewModel> {

    public interface ListenerCheckedLuggage {
        boolean isItemChecked(FlightBookingAmenityViewModel selectedItem);
    }

    private ListenerCheckedLuggage listenerCheckedLuggage;

    private TextView title;
    private ImageView imageChecked;

    public FlightBookingMealViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.tv_title);
        imageChecked = (ImageView) itemView.findViewById(R.id.image_checked);
    }

    @Override
    public void bindObject(FlightBookingAmenityViewModel flightBookingLuggageViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedLuggage != null) {
            isItemChecked = listenerCheckedLuggage.isItemChecked(flightBookingLuggageViewModel);
        }

        title.setText(flightBookingLuggageViewModel.getTitle());
        if (isItemChecked) {
            imageChecked.setVisibility(View.VISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.tkpd_main_green));
        } else {
            imageChecked.setVisibility(View.INVISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black_seventy_percent_));
        }
    }

    public void setListenerCheckedLuggage(ListenerCheckedLuggage listenerCheckedLuggage) {
        this.listenerCheckedLuggage = listenerCheckedLuggage;
    }
}
