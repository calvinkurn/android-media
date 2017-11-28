package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageViewHolder extends BaseViewHolder<FlightBookingLuggageViewModel> {

    public interface ListenerCheckedLuggage {
        boolean isItemChecked(FlightBookingLuggageViewModel selectedItem);
    }

    private ListenerCheckedLuggage listenerCheckedLuggage;

    private TextView weight;
    private TextView price;
    private ImageView imageChecked;

    public FlightBookingLuggageViewHolder(View itemView) {
        super(itemView);
        weight = (TextView) itemView.findViewById(R.id.weight);
        price = (TextView) itemView.findViewById(R.id.price);
        imageChecked = (ImageView) itemView.findViewById(R.id.image_checked);
    }

    @Override
    public void bindObject(FlightBookingLuggageViewModel flightBookingLuggageViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedLuggage != null) {
            isItemChecked = listenerCheckedLuggage.isItemChecked(flightBookingLuggageViewModel);
        }

        weight.setText(flightBookingLuggageViewModel.getWeightFmt());
        price.setText(flightBookingLuggageViewModel.getPriceFmt());
        if (isItemChecked) {
            imageChecked.setVisibility(View.VISIBLE);
            weight.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.tkpd_main_green));
            price.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.tkpd_main_green));
        } else {
            imageChecked.setVisibility(View.INVISIBLE);
            weight.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black_seventy_percent_));
            price.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black_seventy_percent_));
        }
    }

    public void setListenerCheckedLuggage(ListenerCheckedLuggage listenerCheckedLuggage) {
        this.listenerCheckedLuggage = listenerCheckedLuggage;
    }
}
