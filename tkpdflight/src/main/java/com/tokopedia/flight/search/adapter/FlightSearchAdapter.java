package com.tokopedia.flight.search.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchAdapter extends BaseListV2Adapter<FlightSearchViewModel> {

    public FlightSearchAdapter(OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightSearchAdapter(@Nullable List<FlightSearchViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener);
    }

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
            tvPrice.setText(flightSearchViewModel.getTotal());
            tvDuration.setText(flightSearchViewModel.getDuration());
            //tvAirline.setText(flightSearchViewModel.getAirline());
        }
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_search);
        return new FlightSearchViewHolder(view);
    }
}
