package com.tokopedia.flight.review.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapter;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.review.view.presenter.FlightBookingReviewContract;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapter;
import com.tokopedia.flight.review.view.presenter.FlightBookingReviewPresenter;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPriceAdapter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/9/17.
 */

public class FlightBookingReviewFragment extends BaseDaggerFragment implements FlightBookingReviewContract.View {

    public static final String EXTRA_DATA_REVIEW = "EXTRA_DATA_REVIEW";

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
    FlightBookingReviewModel flightBookingReviewModel;

    public static FlightBookingReviewFragment createInstance(FlightBookingReviewModel flightBookingReviewModel){
        FlightBookingReviewFragment flightBookingReviewFragment = new FlightBookingReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DATA_REVIEW, flightBookingReviewModel);
        flightBookingReviewFragment.setArguments(bundle);
        return flightBookingReviewFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class)
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightBookingReviewModel = getArguments().getParcelable(EXTRA_DATA_REVIEW);
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

        initView();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightBookingReviewPresenter.submitData();
            }
        });
        reviewDetailReturnFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlightDetailActivity.createIntent(getActivity(), flightBookingReviewModel.getDetailViewModelListReturn()));
            }
        });
        reviewDetailDepartureFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlightDetailActivity.createIntent(getActivity(), flightBookingReviewModel.getDetailViewModelListDeparture()));
            }
        });
        return view;
    }

    void initView() {
        FlightDetailAdapter departureFlightAdapter = new FlightDetailAdapter(getContext());
        departureFlightAdapter.addData(flightBookingReviewModel.getDetailViewModelListDeparture().getRouteList());
        recyclerViewDepartureFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDepartureFlight.setAdapter(departureFlightAdapter);
        FlightDetailAdapter returnFlightAdapter = new FlightDetailAdapter(getContext());
        returnFlightAdapter.addData(flightBookingReviewModel.getDetailViewModelListReturn().getRouteList());
        recyclerViewReturnFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReturnFlight.setAdapter(returnFlightAdapter);
        FlightBookingReviewPassengerAdapter flightBookingReviewPassengerAdapter = new FlightBookingReviewPassengerAdapter(getContext());
        flightBookingReviewPassengerAdapter.addData(flightBookingReviewModel.getDetailPassengers());
        recyclerViewDataPassenger.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDataPassenger.setAdapter(flightBookingReviewPassengerAdapter);
        FlightBookingReviewPriceAdapter flightBookingReviewPriceAdapter = new FlightBookingReviewPriceAdapter(getContext());
        flightBookingReviewPriceAdapter.addData(flightBookingReviewModel.getFlightReviewFares());
        recyclerViewDetailPrice.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDetailPrice.setAdapter(flightBookingReviewPriceAdapter);

        reviewTotalPrice.setText(flightBookingReviewModel.getTotalPrice());
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
