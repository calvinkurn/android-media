package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.adapter.FlightReviewCancellationAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by furqan on 29/03/18.
 */

public class FlightReviewCancellationFragment extends BaseListFragment<FlightCancellationViewModel, FlightReviewCancellationAdapterTypeFactory> {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";

    private AppCompatButton btnSubmit;

    private String invoiceId;
    private List<FlightCancellationViewModel> flightCancellationPassData;

    public static FlightReviewCancellationFragment createInstance(String invoiceId,
                                                            List<FlightCancellationJourney> flightCancellationPassData) {
        FlightReviewCancellationFragment fragment = new FlightReviewCancellationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putParcelableArrayList(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) flightCancellationPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_review, container, false);
        btnSubmit = view.findViewById(R.id.button_submit);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
        flightCancellationPassData = getArguments().getParcelableArrayList(EXTRA_CANCEL_JOURNEY);

        renderReviewList();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightCancellationComponent.class).inject(this);
    }

    @Override
    public void onItemClicked(FlightCancellationViewModel flightCancellationViewModel) {

    }

    @Override
    public void loadData(int page) {
    }

    @Override
    protected FlightReviewCancellationAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightReviewCancellationAdapterTypeFactory();
    }

    private void renderReviewList() {
        renderList(flightCancellationPassData);
    }
}
