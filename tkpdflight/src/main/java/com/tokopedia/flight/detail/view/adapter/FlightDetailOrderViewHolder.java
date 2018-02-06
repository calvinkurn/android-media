package com.tokopedia.flight.detail.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.view.FlightExpandableOptionArrow;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderViewHolder extends AbstractViewHolder<FlightOrderJourney> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_detail_order;
    private TextView flightCounter;
    private View journeyView;
    private TextView titleJourney;
    private AppCompatImageView imageJourney;
    private RecyclerView recyclerViewFlightJourney;
    private TextView cekSyaratText;
    private View separatorLine;
    private FlightDetailAdapter flightDetailAdapter;
    private FlightOrderJourney flightOrderJourney;

    private ExpandableOnClickListener expandableOnClickListener;

    private boolean isFlightInfoShowed = true;

    public FlightDetailOrderViewHolder(final View layoutView, ExpandableOnClickListener expandableOnClickListener) {
        super(layoutView);
        this.expandableOnClickListener = expandableOnClickListener;

        flightCounter = layoutView.findViewById(R.id.counter_flight);
        journeyView = layoutView.findViewById(R.id.layout_expendable_flight);
        titleJourney = layoutView.findViewById(R.id.title_expendable_passenger);
        imageJourney = layoutView.findViewById(R.id.image_expendable_passenger);
        recyclerViewFlightJourney = layoutView.findViewById(R.id.recycler_view_flight_detail_journey);
        cekSyaratText = layoutView.findViewById(R.id.text_view_flight_cek_syarat);
        separatorLine = layoutView.findViewById(R.id.flight_detail_order_separator_line);
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

        journeyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageJourney.startAnimation(AnimationUtils.loadAnimation(layoutView.getContext(), R.anim.rotate_reverse));
                toggleFlightInfo();
            }
        });

    }

    @Override
    public void bind(FlightOrderJourney flightOrderJourney) {
        this.flightOrderJourney = flightOrderJourney;
        flightCounter.setText(itemView.getContext().getString(R.string.flight_label_detail_counter, getAdapterPosition() + 1));
        titleJourney.setText(itemView.getContext().getString(R.string.flight_label_detail_format,
                flightOrderJourney.getDepartureCity(), flightOrderJourney.getDepartureAiportId(), flightOrderJourney.getArrivalCity(), flightOrderJourney.getArrivalAirportId()));
        List<Visitable> visitables = new ArrayList<>();
        visitables.addAll(this.flightOrderJourney.getRouteViewModels());
        flightDetailAdapter.addElement(visitables);
        flightDetailAdapter.notifyDataSetChanged();
    }

    private void toggleFlightInfo() {
        if (isFlightInfoShowed) {
            hideFlightInfo();
        } else {
            showFlightInfo();
        }
    }

    private void hideFlightInfo() {
        isFlightInfoShowed = false;
        recyclerViewFlightJourney.setVisibility(View.GONE);
        cekSyaratText.setVisibility(View.GONE);
        separatorLine.setVisibility(View.GONE);
        imageJourney.setRotation(180);
    }

    private void showFlightInfo() {
        isFlightInfoShowed = true;
        recyclerViewFlightJourney.setVisibility(View.VISIBLE);
        cekSyaratText.setVisibility(View.VISIBLE);
        separatorLine.setVisibility(View.VISIBLE);
        imageJourney.setRotation(0);
        expandableOnClickListener.onCloseExpand(getAdapterPosition());
    }


}
