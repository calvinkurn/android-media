package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationFragment extends BaseListFragment<FlightCancellationViewModel, FlightCancellationAdapterTypeFactory>
        implements FlightCancellationContract.View {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";

    private String invoiceId;
    private List<FlightCancellationViewModel> flightCancellationViewModelList;

    @Inject
    FlightCancellationPresenter flightCancellationPresenter;

    public static FlightCancellationFragment createInstance(String invoiceId,
                                                            List<FlightCancellationJourney> flightCancellationJourneyList) {
        FlightCancellationFragment fragment = new FlightCancellationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putParcelableArrayList(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) flightCancellationJourneyList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_cancellation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);

        flightCancellationPresenter.attachView(this);
        flightCancellationPresenter.onViewCreated();
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
    public void onItemClicked(FlightCancellationViewModel viewModel) {

    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected FlightCancellationAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightCancellationAdapterTypeFactory();
    }

    @Override
    public void renderCancelableList() {
        renderList(getCurrentFlightCancellationViewModel());
    }

    @Override
    public void setFlightCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        this.flightCancellationViewModelList = flightCancellationViewModelList;
    }

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public List<FlightCancellationJourney> getFlightCancellationJourney() {
        return getArguments().getParcelable(EXTRA_CANCEL_JOURNEY);
    }

    @Override
    public List<FlightCancellationViewModel> getCurrentFlightCancellationViewModel() {
        return flightCancellationViewModelList;
    }
}
