package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.view.FlightExpandableOptionArrow;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderViewHolder extends BaseViewHolder<FlightOrderJourney> {

    private TextView flightCounter;
    private FlightExpandableOptionArrow journeyView;
    private RecyclerView recyclerViewFlightJourney;
    private FlightDetailAdapter flightDetailAdapter;
    private FlightOrderJourney flightOrderJourney;

    public FlightDetailOrderViewHolder(View layoutView) {
        super(layoutView);
        flightCounter = layoutView.findViewById(R.id.counter_flight);
        journeyView = layoutView.findViewById(R.id.title_journey_flight);
        recyclerViewFlightJourney = layoutView.findViewById(R.id.recycler_view_flight_detail_journey);
        FlightDetailRouteTypeFactory detailRouteTypeFactory = new FlightDetailAdapterTypeFactory(
                new FlightDetailAdapterTypeFactory.OnFlightDetailListener() {
                    @Override
                    public int getItemCount() {
                        if (flightOrderJourney != null) {
                            return flightOrderJourney.getRouteViewModels().size();
                        }
                        return 0;
                    }
                });
        flightDetailAdapter = new FlightDetailAdapter(detailRouteTypeFactory, new ArrayList<Visitable>());
        recyclerViewFlightJourney.setAdapter(flightDetailAdapter);
    }

    @Override
    public void bindObject(FlightOrderJourney flightOrderJourney) {
        this.flightOrderJourney = flightOrderJourney;
        flightCounter.setText(itemView.getContext().getString(R.string.flight_label_detail_counter, getAdapterPosition() + 1));
        journeyView.setTitleText(itemView.getContext().getString(R.string.flight_label_detail_format,
                flightOrderJourney.getDepartureCity(), flightOrderJourney.getDepartureAiportId(), flightOrderJourney.getArrivalCity(), flightOrderJourney.getArrivalAirportId()));
        List<Visitable> visitables = new ArrayList<>();
        visitables.addAll(this.flightOrderJourney.getRouteViewModels());
        flightDetailAdapter.addElement(visitables);
        flightDetailAdapter.notifyDataSetChanged();
    }
}
