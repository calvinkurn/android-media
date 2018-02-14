package com.tokopedia.flight.search.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.view.FlightMultiAirlineView;
import com.tokopedia.flight.search.util.DurationUtil;
import com.tokopedia.flight.search.view.adapter.FilterSearchAdapterTypeFactory;
import com.tokopedia.flight.search.view.model.Duration;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FlightSearchViewHolder extends AbstractViewHolder<FlightSearchViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_search;

    TextView tvDeparture;
    TextView tvArrival;
    TextView tvAirline;
    FlightMultiAirlineView flightMultiAirlineView;
    TextView tvPrice;
    TextView tvDuration;
    TextView airlineRefundableInfo;
    TextView savingPrice;
    TextView arrivalAddDay;
    View containerDetail;
    private FilterSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener;

    public FlightSearchViewHolder(View itemView, FilterSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener) {
        super(itemView);
        tvDeparture = (TextView) itemView.findViewById(R.id.departure_time);
        tvArrival = (TextView) itemView.findViewById(R.id.arrival_time);
        tvAirline = (TextView) itemView.findViewById(R.id.tv_airline);
        flightMultiAirlineView = (FlightMultiAirlineView) itemView.findViewById(R.id.view_multi_airline);
        airlineRefundableInfo = (TextView) itemView.findViewById(R.id.airline_refundable_info);
        tvPrice = (TextView) itemView.findViewById(R.id.total_price);
        tvDuration = (TextView) itemView.findViewById(R.id.flight_time);
        savingPrice = (TextView) itemView.findViewById(R.id.saving_price);
        arrivalAddDay = (TextView) itemView.findViewById(R.id.arrival_add_day);
        containerDetail = itemView.findViewById(R.id.container_detail);
        this.onFlightSearchListener = onFlightSearchListener;
    }

    @Override
    public void bind(final FlightSearchViewModel flightSearchViewModel) {
        tvDeparture.setText(String.format("%s %s", flightSearchViewModel.getDepartureTime(), flightSearchViewModel.getDepartureAirport()));
        tvArrival.setText(String.format("%s %s", flightSearchViewModel.getArrivalTime(), flightSearchViewModel.getArrivalAirport()));
        tvPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(flightSearchViewModel.getFare().getAdultNumeric()));
        setDuration(flightSearchViewModel);
        setAirline(flightSearchViewModel);
        View.OnClickListener detailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFlightSearchListener.onDetailClicked(flightSearchViewModel, getAdapterPosition());
            }
        };
        tvPrice.setOnClickListener(detailClickListener);
        containerDetail.setOnClickListener(detailClickListener);

        setRefundableInfo(flightSearchViewModel);
        setSavingPrice(flightSearchViewModel);
        setArrivalAddDay(flightSearchViewModel);
    }

    private void setArrivalAddDay(FlightSearchViewModel flightSearchViewModel) {
        if (flightSearchViewModel.getAddDayArrival() > 0) {
            arrivalAddDay.setVisibility(View.VISIBLE);
            arrivalAddDay.setText(itemView.getContext().getString(R.string.flight_label_duration_add_day, flightSearchViewModel.getAddDayArrival()));
        } else {
            arrivalAddDay.setVisibility(View.GONE);
        }
    }

    void setDuration(FlightSearchViewModel flightSearchViewModel) {
        Duration duration = DurationUtil.convertFormMinute(flightSearchViewModel.getDurationMinute());
        String durationString = DurationUtil.getReadableString(itemView.getContext(), duration);
        if (flightSearchViewModel.getTotalTransit() > 0) {
            tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_transit,
                    durationString, String.valueOf(flightSearchViewModel.getTotalTransit())));
        } else {
            tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_direct,
                    durationString));
        }
    }

    private void setSavingPrice(FlightSearchViewModel flightSearchViewModel) {
        if (TextUtils.isEmpty(flightSearchViewModel.getBeforeTotal())) {
            savingPrice.setVisibility(View.GONE);
        } else {
            savingPrice.setVisibility(View.VISIBLE);
            savingPrice.setText(MethodChecker.fromHtml(getString(R.string.flight_label_saving_price_html, flightSearchViewModel.getBeforeTotal())));
        }
    }

    private void setAirline(FlightSearchViewModel flightSearchViewModel) {
        if (flightSearchViewModel.getAirlineList().size() > 1) {
            List<FlightAirlineDB> flightAirlineDBs = flightSearchViewModel.getAirlineList();
            if (flightAirlineDBs != null && flightAirlineDBs.size() > 0) {
                List<String> airlineLogoList = new ArrayList<>();
                for (int i = 0, sizei = flightAirlineDBs.size(); i < sizei; i++) {
                    FlightAirlineDB flightAirlineDB = flightAirlineDBs.get(i);
                    airlineLogoList.add(flightAirlineDB.getLogo());
                }
                flightMultiAirlineView.setAirlineLogos(airlineLogoList);
            } else {
                flightMultiAirlineView.setAirlineLogos(null);
            }
            tvAirline.setText(R.string.flight_label_multi_maskapai);
        } else if (flightSearchViewModel.getAirlineList().size() == 1) {
            flightMultiAirlineView.setAirlineLogo(flightSearchViewModel.getAirlineList().get(0).getLogo());
            tvAirline.setText(flightSearchViewModel.getAirlineList().get(0).getName());
        }
    }

    private void setRefundableInfo(FlightSearchViewModel flightSearchViewModel) {
        /*if (flightSearchViewModel.isRefundable() == RefundableEnum.NOT_REFUNDABLE) {
            airlineRefundableInfo.setVisibility(View.GONE);
        } else {*/
        airlineRefundableInfo.setVisibility(View.VISIBLE);
        airlineRefundableInfo.setText(flightSearchViewModel.isRefundable().getValueRes());
//        }
    }
}
