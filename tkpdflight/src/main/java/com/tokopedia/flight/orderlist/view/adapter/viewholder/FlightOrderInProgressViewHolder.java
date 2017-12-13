package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderInProcessViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderInProgressViewHolder extends FlightOrderBaseViewHolder<FlightOrderInProcessViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_in_progress;
    private final FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvOrderDate;
    private AppCompatTextView tvOrderId;
    private AppCompatTextView tvDepartureCity;
    private AppCompatTextView tvArrivalCity;
    private AppCompatTextView tvDepartureTime;
    private AppCompatTextView tvRebooking;
    private FlightOrderInProcessViewModel item;

    public FlightOrderInProgressViewHolder(View itemView, FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener) {
        super(itemView);
        this.adapterInteractionListener = adapterInteractionListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        tvTitle = (AppCompatTextView) view.findViewById(R.id.tv_title);
        tvOrderDate = (AppCompatTextView) view.findViewById(R.id.tv_order_date);
        tvOrderId = (AppCompatTextView) view.findViewById(R.id.tv_order_id);
        tvDepartureCity = (AppCompatTextView) view.findViewById(R.id.tv_departure_city);
        tvArrivalCity = (AppCompatTextView) view.findViewById(R.id.tv_arrival_city);
        tvDepartureTime = (AppCompatTextView) view.findViewById(R.id.tv_departure_time);
        tvRebooking = (AppCompatTextView) view.findViewById(R.id.tv_rebooking);
        tvRebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterInteractionListener.onReBookingClicked(item);
            }
        });
    }


    @Override
    public void bind(FlightOrderInProcessViewModel element) {
        this.item = element;
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(FlightDateUtil.formatToUi(element.getCreateTime()));
        tvOrderId.setText(String.format("%s %s", itemView.getContext().getString(R.string.flight_order_order_id_prefix), element.getId()));
        if (element.getOrderJourney().size() > 0) {
            //TODO : set Cemter icon
            FlightOrderJourney orderJourney = element.getOrderJourney().get(0);
            tvDepartureCity.setText(getAirportTextForView(
                    orderJourney.getDepartureAiportId(),
                    orderJourney.getDepartureCityCode(),
                    orderJourney.getDepartureCity()));
            tvArrivalCity.setText(getAirportTextForView(
                    orderJourney.getDepartureAiportId(),
                    orderJourney.getDepartureCityCode(),
                    orderJourney.getDepartureCity()));
            tvDepartureTime.setText(FlightDateUtil.formatToUi(orderJourney.getDepartureTime()));
        }
    }

    @Override
    protected void onHelpOptionClicked() {
        adapterInteractionListener.onHelpOptionClicked(item.getId());
    }

    @Override
    protected void onDetailOptionClicked() {
        adapterInteractionListener.onFailedOrderDetailClicked(item);
    }
}