package com.tokopedia.flight.search.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchAdapter extends BaseListAdapter<FlightSearchViewModel> {

    public class FlightSearchViewHolder extends BaseViewHolder<FlightSearchViewModel> {

        TextView tvDeparture;
        TextView tvArrival;
        TextView tvAirline;
        AppCompatTextView tvPrice;
        AppCompatTextView tvDuration;

        public FlightSearchViewHolder(View itemView) {
            super(itemView);
            tvDeparture = (TextView) itemView.findViewById(R.id.tv_departure);
            tvArrival = (TextView) itemView.findViewById(R.id.tv_arrival);
            tvAirline = (TextView) itemView.findViewById(R.id.tv_airline);
            tvPrice = (AppCompatTextView) itemView.findViewById(R.id.tv_total_price);
            tvDuration = (AppCompatTextView) itemView.findViewById(R.id.tv_duration);
        }

        @Override
        public void bindObject(FlightSearchViewModel flightSearchViewModel) {
            tvDeparture.setText(flightSearchViewModel.getDepartureAirport());
            tvArrival.setText(flightSearchViewModel.getArrivalAirport());
            tvAirline.setText(flightSearchViewModel.getAirline());
            tvPrice.setText(flightSearchViewModel.getTotal());
            tvDuration.setText(flightSearchViewModel.getDuration());
        }
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_search);
        return new FlightSearchViewHolder(view);
    }
}
