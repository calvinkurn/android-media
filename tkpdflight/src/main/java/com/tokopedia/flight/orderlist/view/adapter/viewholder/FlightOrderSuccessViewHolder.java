package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderSuccessViewHolder extends FlightOrderBaseViewHolder<FlightOrderSuccessViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_success;
    private final FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvOrderDate;
    private AppCompatTextView tvOrderId;
    private AppCompatTextView tvDepartureCity;
    private AppCompatTextView tvMainButton;
    private AppCompatTextView tvArrivalCity;
    private AppCompatTextView tvDepartureTime;
    private FlightOrderSuccessViewModel item;

    public FlightOrderSuccessViewHolder(FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener, View itemView) {
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
        tvMainButton = (AppCompatTextView) view.findViewById(R.id.tv_main_button);
    }


    @Override
    public void bind(final FlightOrderSuccessViewModel element) {
        this.item = element;
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(FlightDateUtil.formatToUi(element.getCreateTime()));
        tvOrderId.setText(String.format("%s %s", itemView.getContext().getString(R.string.flight_order_order_id_prefix), element.getId()));
        tvDepartureCity.setText(getAirportTextForView(
                element.getOrderJourney().getDepartureAiportId(),
                element.getOrderJourney().getDepartureCityCode(),
                element.getOrderJourney().getDepartureCity()));
        tvArrivalCity.setText(getAirportTextForView(
                element.getOrderJourney().getDepartureAiportId(),
                element.getOrderJourney().getDepartureCityCode(),
                element.getOrderJourney().getDepartureCity()));
        tvDepartureTime.setText(FlightDateUtil.formatToUi(element.getOrderJourney().getDepartureTime()));
        tvMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    protected void onHelpOptionClicked() {
        adapterInteractionListener.onHelpOptionClicked(item.getId());
    }

    @Override
    protected void onDetailOptionClicked() {
        adapterInteractionListener.onSuccessOrderDetailClicked(item);
    }
}
