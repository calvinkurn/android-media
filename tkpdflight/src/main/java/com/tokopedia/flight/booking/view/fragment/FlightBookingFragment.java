package com.tokopedia.flight.booking.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.activity.FlightBookingPassengerActivity;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.widget.CardWithActionView;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingFragment extends BaseDaggerFragment implements FlightBookingContract.View, FlightBookingPassengerAdapter.OnClickListener {

    private static final int REQUEST_CODE_PASSENGER = 1;
    private AppCompatTextView tvTimeFinishOrderIndicator;
    private CardWithActionView cwaDepartureInfoView;
    private CardWithActionView cwaReturnInfoView;
    private RecyclerView rvPassengers;
    private AppCompatButton buttonSubmit;

    @Inject
    FlightBookingPresenter presenter;

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
                presenter.onButtonSubmitClicked();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class).inject(this);
    }

    @Override
    public void onChangePassengerData(FlightBookingPassengerViewModel viewModel) {
        startActivityForResult(FlightBookingPassengerActivity.getCallingIntent(getActivity(), viewModel), REQUEST_CODE_PASSENGER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PASSENGER:

                    break;
            }
        }
    }
}
