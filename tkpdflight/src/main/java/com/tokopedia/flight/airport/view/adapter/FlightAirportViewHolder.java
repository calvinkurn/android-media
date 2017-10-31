package com.tokopedia.flight.airport.view.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportViewHolder extends BaseViewHolder<FlightAirportDB> {

    private TextView city;
    private TextView airport;

    public FlightAirportViewHolder(View itemView) {
        super(itemView);
        city = (TextView) itemView.findViewById(R.id.city);
        airport = (TextView) itemView.findViewById(R.id.airport);
    }

    @Override
    public void bindObject(FlightAirportDB flightAirportDB) {
        city.setText(itemView.getContext().getString(R.string.flight_label_city, flightAirportDB.getCityName(), flightAirportDB.getCountryName()));
        if(!TextUtils.isEmpty(flightAirportDB.getAirportId())) {
            airport.setText(itemView.getContext().getString(R.string.flight_label_airport, flightAirportDB.getAirportId(), flightAirportDB.getAirportName()));
        }else{
            airport.setText(itemView.getContext().getString(R.string.flight_labe_all_airport));
        }
    }
}
