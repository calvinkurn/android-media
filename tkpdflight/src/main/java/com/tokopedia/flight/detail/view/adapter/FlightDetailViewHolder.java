package com.tokopedia.flight.detail.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.data.cloud.model.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailViewHolder extends BaseViewHolder<Route> {

    private ImageView imageAirline;
    private TextView airlineName;
    private TextView airlineCode;
    private TextView refundableInfo;
    private ImageView infoRefundableImage;
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
        imageAirline = (ImageView) itemView.findViewById(R.id.airline_icon);
        airlineName = (TextView) itemView.findViewById(R.id.airline_name);
        airlineCode = (TextView) itemView.findViewById(R.id.airline_code);
        refundableInfo = (TextView) itemView.findViewById(R.id.airline_refundable_info);
        infoRefundableImage = (ImageView) itemView.findViewById(R.id.airline_image_refundable_info);
        departureTime = (TextView) itemView.findViewById(R.id.departure_time);
        departureDate = (TextView) itemView.findViewById(R.id.departure_date);
        departureCircleImage = (ImageView) itemView.findViewById(R.id.departure_time_circle);
        departureAirportName = (TextView) itemView.findViewById(R.id.departure_airport_name);
        departureAirportDesc = (TextView) itemView.findViewById(R.id.departure_desc_airport_name);
        flightTime = (TextView) itemView.findViewById(R.id.flight_time);
        arrivalTime = (TextView) itemView.findViewById(R.id.arrival_time);
        arrivalDate = (TextView) itemView.findViewById(R.id.arrival_date);
        arrivalCircleImage = (ImageView) itemView.findViewById(R.id.arrival_time_circle);
        arrivalAirportName = (TextView) itemView.findViewById(R.id.arrival_airport_name);
        arrivalAirportDesc = (TextView) itemView.findViewById(R.id.arrival_desc_airport_name);
        transitInfo = (TextView) itemView.findViewById(R.id.transit_info);
    }

    @Override
    public void bindObject(Route route) {
        airlineName.setText(route.getAirline());
        airlineCode.setText(route.getFlightNumber());
        refundableInfo.setVisibility(route.getRefundable() ? View.VISIBLE : View.GONE);
        infoRefundableImage.setVisibility(route.getRefundable() ? View.VISIBLE : View.GONE);
        departureTime.setText(route.getDepartureTimestamp());
        departureDate.setText(route.getDepartureTimestamp());
        setColorCircle(route);
        departureAirportName.setText(route.getDepartureAirport());
        departureAirportDesc.setText(route.getDepartureAirport());
        flightTime.setText(route.getDuration());
        arrivalTime.setText(route.getArrivalTimestamp());
        arrivalDate.setText(route.getArrivalTimestamp());
        arrivalAirportName.setText(route.getArrivalAirport());
        arrivalAirportDesc.setText(route.getArrivalAirport());
        transitInfo.setText(route.getLayover());
    }

    //set color circle to green if position holder is on first index
    private void setColorCircle(Route route) {
        if(getAdapterPosition() == 0){
            departureCircleImage.setEnabled(true);
        }
    }

    //set color circle to red if position holder is on last index
    public void bindLastPosition(boolean lastItemPosition) {
        if(lastItemPosition){
            arrivalCircleImage.setEnabled(false);
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    public void bindTransitInfo(boolean isTransit){
        if(isTransit && getAdapterPosition() == 0){
            transitInfo.setVisibility(View.VISIBLE);
        }else{
            transitInfo.setVisibility(View.GONE);
        }
    }
}
