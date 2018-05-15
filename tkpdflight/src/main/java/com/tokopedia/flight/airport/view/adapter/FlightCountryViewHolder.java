package com.tokopedia.flight.airport.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightCountryAirportViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightCountryViewHolder extends AbstractViewHolder<FlightCountryAirportViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_country;

    private TextView countryTextView;
    private RecyclerView airportRecyclerView;

    private FlightAirportClickListener flightAirportClickListener;

    public FlightCountryViewHolder(View itemView, FlightAirportClickListener flightAirportClickListener) {
        super(itemView);
        countryTextView = (TextView) itemView.findViewById(R.id.country);
        airportRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_airports);
        this.flightAirportClickListener = flightAirportClickListener;
    }

    @Override
    public void bind(FlightCountryAirportViewModel country) {
        countryTextView.setText(country.getCountryName());
        List<Visitable> visitables = new ArrayList<>();
        visitables.addAll(country.getAirports());
        FlightAirportAdapter airportAdapter = new FlightAirportAdapter(new FlightAirportAdapterTypeFactory(flightAirportClickListener), visitables);
        LinearLayoutManager flightSimpleAdapterLayoutManager
                = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        airportRecyclerView.setLayoutManager(flightSimpleAdapterLayoutManager);
        airportRecyclerView.setHasFixedSize(true);
        airportRecyclerView.setNestedScrollingEnabled(false);
        airportRecyclerView.setAdapter(airportAdapter);
        airportAdapter.notifyDataSetChanged();
    }
}
