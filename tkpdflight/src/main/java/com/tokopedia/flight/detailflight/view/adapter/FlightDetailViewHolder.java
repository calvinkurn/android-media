package com.tokopedia.flight.detailflight.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailViewHolder extends BaseViewHolder<Route> {

    private ImageView imageAirline;
    private TextView airlineName;
    private TextView airlineCode;
    private TextView refundableInfo;
    private ImageView infoRefundable;
    private TextView departureTime;
    private TextView departureDate;
    private ImageView departureCircleImage;
    private TextView departureAirportName;
    private TextView departureAirportDesc;
    private TextView flightTime;
    private TextView arrivalTime;
    private TextView arrivalDate;
    private ImageView arrivalCircleImage;
    private TextView arrivalAirportName;
    private TextView arrivalAirportDesc;
    private TextView transitInfo;

    public FlightDetailViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void bindObject(Route flightSearchSingleRouteDB) {

    }
}
