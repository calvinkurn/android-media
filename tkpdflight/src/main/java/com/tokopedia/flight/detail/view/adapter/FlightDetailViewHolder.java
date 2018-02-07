package com.tokopedia.flight.detail.view.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailViewHolder extends AbstractViewHolder<FlightDetailRouteViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_detail;

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
    private View containerPNR;
    private TextView pnrCode;
    private ImageView copyPnr;
    private FlightDetailAdapterTypeFactory.OnFlightDetailListener onFlightDetailListener;

    public FlightDetailViewHolder(View itemView, FlightDetailAdapterTypeFactory.OnFlightDetailListener onFlightDetailListener) {
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
        containerPNR = itemView.findViewById(R.id.container_pnr);
        pnrCode = itemView.findViewById(R.id.pnr_code);
        copyPnr = itemView.findViewById(R.id.copy_pnr);
        this.onFlightDetailListener = onFlightDetailListener;
    }

    @Override
    public void bind(FlightDetailRouteViewModel route) {
        airlineName.setText(route.getAirlineName());
        airlineCode.setText(String.format("%s - %s", route.getAirlineCode(), route.getFlightNumber()));
        setRefundableInfo(route);
        departureTime.setText(FlightDateUtil.formatDate(FlightDateUtil.FORMAT_DATE_API_DETAIL, FlightDateUtil.FORMAT_TIME_DETAIL, route.getDepartureTimestamp()));
        departureDate.setText(FlightDateUtil.formatDate(FlightDateUtil.FORMAT_DATE_API_DETAIL, FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL, route.getDepartureTimestamp()));
        setColorCircle();
        departureAirportName.setText(String.format("%s (%s)", route.getDepartureAirportCity(), route.getDepartureAirportCode()));
        departureAirportDesc.setText(route.getDepartureAirportName());
        flightTime.setText(route.getDuration());
        arrivalTime.setText(FlightDateUtil.formatDate(FlightDateUtil.FORMAT_DATE_API_DETAIL, FlightDateUtil.FORMAT_TIME_DETAIL, route.getArrivalTimestamp()));
        arrivalDate.setText(FlightDateUtil.formatDate(FlightDateUtil.FORMAT_DATE_API_DETAIL, FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL, route.getArrivalTimestamp()));
        arrivalAirportName.setText(String.format("%s (%s)", route.getArrivalAirportCity(), route.getArrivalAirportCode()));
        arrivalAirportDesc.setText(route.getArrivalAirportName());
        transitInfo.setText(itemView.getContext().getString(R.string.flight_label_transit, route.getArrivalAirportCity(), route.getLayover()));
        setPNR(route.getPnr());
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.getAirlineLogo(), R.drawable.ic_airline_default);
        if (onFlightDetailListener != null) {
            bindLastPosition(onFlightDetailListener.getItemCount() == getAdapterPosition());
            bindTransitInfo(onFlightDetailListener.getItemCount());
        }
    }

    private void setPNR(String pnr) {
        if (!TextUtils.isEmpty(pnr)) {
            containerPNR.setVisibility(View.VISIBLE);
            pnrCode.setText(pnr);
            copyPnr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(R.string.flight_label_order_id), pnrCode.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                        @Override
                        public void onPrimaryClipChanged() {
                            Toast.makeText(itemView.getContext(), R.string.flight_label_copy_clipboard, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            containerPNR.setVisibility(View.GONE);
        }
    }

    private void setRefundableInfo(FlightDetailRouteViewModel route) {
        if (route.isRefundable()) {
            refundableInfo.setText(R.string.flight_label_refundable_info);
            refundableInfo.setVisibility(View.VISIBLE);
        } else {
            refundableInfo.setText(R.string.flight_label_non_refundable_info);
            refundableInfo.setVisibility(View.GONE);
        }
    }

    //set color circle to green if position holder is on first index
    private void setColorCircle() {
        if (getAdapterPosition() == 0) {
            departureCircleImage.setEnabled(true);
        }
    }

    //set color circle to red if position holder is on last index
    public void bindLastPosition(boolean lastItemPosition) {
        if (lastItemPosition) {
            arrivalCircleImage.setEnabled(false);
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    public void bindTransitInfo(int sizeInfo) {
        if (sizeInfo > 0 && getAdapterPosition() < sizeInfo - 1) {
            transitInfo.setVisibility(View.VISIBLE);
        } else {
            transitInfo.setVisibility(View.GONE);
        }
    }
}
