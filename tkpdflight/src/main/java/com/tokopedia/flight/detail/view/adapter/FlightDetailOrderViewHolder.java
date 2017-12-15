package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.expandable.ExpandableOptionArrow;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderViewHolder extends BaseViewHolder<FlightOrderJourney> {

    private TextView flightCounter;
    private ExpandableOptionArrow journeyView;
    private RecyclerView recyclerViewFlightJourney;
    private FlightDetailAdapter flightDetailAdapter;

    @Override
    public void bindObject(FlightOrderJourney flightOrderJourney) {
        flightCounter.setText(itemView.getContext().getString(R.string.flight_label_detail_counter, getAdapterPosition() + 1));
        flightDetailAdapter.addData(flightOrderJourney.getRouteViewModels());
        flightDetailAdapter.notifyDataSetChanged();
    }

    public FlightDetailOrderViewHolder(View layoutView) {
        super(layoutView);
        flightCounter = layoutView.findViewById(R.id.counter_flight);
        journeyView = layoutView.findViewById(R.id.title_journey_flight);
        recyclerViewFlightJourney = layoutView.findViewById(R.id.recycler_view_flight_detail_journey);

        flightDetailAdapter = new FlightDetailAdapter(layoutView.getContext());
        recyclerViewFlightJourney.setAdapter(flightDetailAdapter);
    }
}
