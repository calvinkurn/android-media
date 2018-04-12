package com.tokopedia.flight.cancellation.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationReviewActivity;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationRefundDetailContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationRefundDetailPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightCancellationRefundDetailFragment extends BaseDaggerFragment implements FlightCancellationRefundDetailContract.View {

    private static final String PARAM_CANCELLATION = "PARAM_CANCELLATION";
    private static final String PARAM_STEP_NUMBER = "PARAM_STEP_NUMBER";

    private FlightCancellationWrapperViewModel wrapperViewModel;
    private int stepsNumber;

    public static FlightCancellationRefundDetailFragment newInstance(FlightCancellationWrapperViewModel wrapperViewModel,
                                                                     int stepNumber) {
        FlightCancellationRefundDetailFragment fragment = new FlightCancellationRefundDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_CANCELLATION, wrapperViewModel);
        args.putInt(PARAM_STEP_NUMBER, stepNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private AppCompatTextView tvStepTitle;
    private ProgressBar progressBar;
    private LinearLayout container;
    private AppCompatTextView tvTotalPrice;
    private AppCompatTextView tvTotalRefund;
    private AppCompatButton btnNext;

    @Inject
    FlightCancellationRefundDetailPresenter presenter;

    public FlightCancellationRefundDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wrapperViewModel = getArguments().getParcelable(PARAM_CANCELLATION);
            stepsNumber = getArguments().getInt(PARAM_STEP_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_refund_detail, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        container = (LinearLayout) view.findViewById(R.id.container);
        tvStepTitle = (AppCompatTextView) view.findViewById(R.id.tv_step_title);
        tvTotalPrice = (AppCompatTextView) view.findViewById(R.id.tv_total_price);
        tvTotalRefund = (AppCompatTextView) view.findViewById(R.id.tv_total_refund);
        btnNext = (AppCompatButton) view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(getNextButtonClickListener());

        tvStepTitle.setText(String.format(
                getString(R.string.flight_cancellation_step_3_header_title), stepsNumber));
    }

    private View.OnClickListener getNextButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlightCancellationReviewActivity.createIntent(getActivity(), wrapperViewModel.getInvoice(), wrapperViewModel));
            }
        };
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public FlightCancellationWrapperViewModel getCancellationViewModel() {
        return wrapperViewModel;
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorFetchEstimateRefund(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.onRetryFetchEstimate();
            }
        });
    }

    @Override
    public void renderTotalRefund(String totalRefund) {
        tvTotalRefund.setText(totalRefund);
    }

    @Override
    public void renderTotalPrice(String totalPrice) {
        tvTotalPrice.setText(totalPrice);
    }

    @Override
    public void showFullPageContainer() {
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullPageContainer() {
        container.setVisibility(View.GONE);
    }
}
