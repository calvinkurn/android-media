package com.tokopedia.flight.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/9/17.
 */

public class FlightBookingReviewFragment extends BaseDaggerFragment implements FlightBookingReviewContract.View {

    private TextView reviewTime;
    private TextView reviewDetailDepartureFlight;
    private RecyclerView recyclerViewDepartureFlight;
    private TextView reviewDetailReturnFlight;
    private RecyclerView recyclerViewReturnFlight;
    private RecyclerView recyclerViewDataPassenger;
    private RecyclerView recyclerViewDetailPrice;
    private TextView reviewTotalPrice;
    private Button buttonSubmit;

    @Inject
    FlightBookingReviewPresenter flightBookingReviewPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class)
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_review, container, false);
        reviewTime = (TextView) view.findViewById(R.id.review_time);
        reviewDetailDepartureFlight = (TextView) view.findViewById(R.id.review_detail_departure_flight);
        recyclerViewDepartureFlight = (RecyclerView) view.findViewById(R.id.recycler_view_departure_flight);
        reviewDetailReturnFlight = (TextView) view.findViewById(R.id.review_detail_return_flight);
        recyclerViewReturnFlight = (RecyclerView) view.findViewById(R.id.recycler_view_return_flight);
        recyclerViewDataPassenger = (RecyclerView) view.findViewById(R.id.recycler_view_data_passenger);
        recyclerViewDetailPrice = (RecyclerView) view.findViewById(R.id.recycler_view_detail_price);
        reviewTotalPrice = (TextView) view.findViewById(R.id.total_price);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);

        FlightDetailAdapter departureFlightAdapter = new FlightDetailAdapter(getContext());
        recyclerViewDepartureFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDepartureFlight.setAdapter(departureFlightAdapter);
        FlightDetailAdapter returnFlightAdapter = new FlightDetailAdapter(getContext());
        recyclerViewReturnFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReturnFlight.setAdapter(returnFlightAdapter);
        FlightBookingReviewPassengerAdapter flightBookingReviewPassengerAdapter = new FlightBookingReviewPassengerAdapter(getContext());
        recyclerViewDataPassenger.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDataPassenger.setAdapter(flightBookingReviewPassengerAdapter);
        FlightBookingReviewPriceAdapter flightBookingReviewPriceAdapter = new FlightBookingReviewPriceAdapter(getContext());
        recyclerViewDetailPrice.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDetailPrice.setAdapter(flightBookingReviewPriceAdapter);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightBookingReviewPresenter.submitData();
            }
        });
        return view;
    }

    private void checkVoucherCode(String voucherCode) {
        flightBookingReviewPresenter.checkVoucherCode(voucherCode);
    }

    @Override
    public void onErrorCheckVoucherCode(Throwable e) {

    }

    @Override
    public void onSuccessCheckVoucherCode() {

    }

    @Override
    public void onErrorSubmitData(Throwable e) {

    }

    @Override
    public void onSuccessSubmitData() {

    }
}
