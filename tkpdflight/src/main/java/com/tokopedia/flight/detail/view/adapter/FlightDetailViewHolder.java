package com.tokopedia.flight.detail.view.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.abstraction.utils.DateFormatUtils;
import com.tokopedia.abstraction.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.data.cloud.model.response.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailViewHolder extends BaseViewHolder<Route> {

    public static final String FORMAT_DATE_API = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_DATE = "EEEE, dd LLLL yyyy";
    private ImageView imageAirline;
    private TextView airlineName;
    private TextView airlineCode;
    private TextView refundableInfo;
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
        airlineName.setText(route.getAirlineName());
        airlineCode.setText(String.format("%s - %s", route.getAirline(), route.getFlightNumber()));
        setRefundableInfo(route);
        departureTime.setText(DateFormatUtils.formatDate(FORMAT_DATE_API, FORMAT_TIME, route.getDepartureTimestamp()));
        departureDate.setText(DateFormatUtils.formatDate(FORMAT_DATE_API, FORMAT_DATE, route.getDepartureTimestamp()));
        setColorCircle(route);
        departureAirportName.setText(String.format("%s (%s)", route.getDepartureAirportCity(), route.getDepartureAirport()));
        departureAirportDesc.setText(route.getDepartureAirportName());
        flightTime.setText(route.getDuration());
        arrivalTime.setText(DateFormatUtils.formatDate(FORMAT_DATE_API, FORMAT_TIME, route.getArrivalTimestamp()));
        arrivalDate.setText(DateFormatUtils.formatDate(FORMAT_DATE_API, FORMAT_DATE, route.getArrivalTimestamp()));
        arrivalAirportName.setText(String.format("%s (%s)", route.getArrivalAirportCity(), route.getArrivalAirport()));
        arrivalAirportDesc.setText(route.getArrivalAirportName());
        transitInfo.setText(itemView.getContext().getString(R.string.flight_label_transit, route.getArrivalAirportCity(),route.getLayover()));
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.getAirlineLogo(),R.drawable.ic_airline_default);
    }

    private void setRefundableInfo(Route route) {
        if(route.getRefundable()){
            refundableInfo.setText(R.string.flight_label_refundable_info);
            refundableInfo.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.tkpd_main_green));
            refundableInfo.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_rect_stroke_flight_green));
            refundableInfo.setVisibility(View.VISIBLE);
        }else{
            refundableInfo.setText(R.string.flight_label_non_refundable_info);
            refundableInfo.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.grey_200));
            refundableInfo.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_rect_stroke_flight_grey));
            refundableInfo.setVisibility(View.GONE);
        }
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
    public void bindTransitInfo(int sizeInfo){
        if(sizeInfo > 0 && getAdapterPosition() < sizeInfo - 1){
            transitInfo.setVisibility(View.VISIBLE);
        }else{
            transitInfo.setVisibility(View.GONE);
        }
    }
}
