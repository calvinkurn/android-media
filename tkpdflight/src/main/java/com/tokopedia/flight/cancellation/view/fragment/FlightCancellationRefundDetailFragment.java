package com.tokopedia.flight.cancellation.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationRefundDetailContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationRefundDetailPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightCancellationRefundDetailFragment extends BaseDaggerFragment implements FlightCancellationRefundDetailContract.View{

    private static final String PARAM_CANCELLATION = "PARAM_CANCELLATION";
    private FlightCancellationWrapperViewModel wrapperViewModel;

    public static FlightCancellationRefundDetailFragment newInstance(FlightCancellationWrapperViewModel wrapperViewModel){
        FlightCancellationRefundDetailFragment fragment = new FlightCancellationRefundDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_CANCELLATION, wrapperViewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    FlightCancellationRefundDetailPresenter presenter;

    public FlightCancellationRefundDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            wrapperViewModel = getArguments().getParcelable(PARAM_CANCELLATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_cancellation_refund_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.initialize();
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
    public void showLoading() {

    }

    @Override
    public FlightCancellationWrapperViewModel getCancellationViewModel() {
        return null;
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showErrorFetchEstimateRefund(String message) {

    }

    @Override
    public void renderTotalRefund(String totalRefund) {

    }

    @Override
    public void renderTotalPrice(String totalPrice) {

    }

    @Override
    public void showFullPageContainer() {

    }

    @Override
    public void hideFullPageContainer() {

    }
}
