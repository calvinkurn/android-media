package com.tokopedia.flight.booking.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.widget.CardWithActionView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingFragment extends BaseDaggerFragment {

    private AppCompatTextView tvTimeFinishOrderIndicator;
    private CardWithActionView cwaDepartureInfoView;
    private CardWithActionView cwaReturnInfoView;
    private RecyclerView rvPassengers;
    private AppCompatButton buttonSubmit;

    public static FlightBookingFragment newInstance() {
        return new FlightBookingFragment();
    }


    public FlightBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_booking, container, false);
        tvTimeFinishOrderIndicator = (AppCompatTextView) view.findViewById(R.id.tv_time_finish_order_indicator);
        cwaDepartureInfoView = (CardWithActionView) view.findViewById(R.id.cwa_departure_info);
        cwaReturnInfoView = (CardWithActionView) view.findViewById(R.id.cwa_return_info);
        rvPassengers = (RecyclerView) view.findViewById(R.id.rv_passengers);
        buttonSubmit = (AppCompatButton) view.findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

}
