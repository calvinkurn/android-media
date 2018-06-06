package com.tokopedia.flight.cancellation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationReasonAndProofActivity;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationRefundDetailActivity;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationViewHolder;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationFragment extends BaseListFragment<FlightCancellationViewModel, FlightCancellationAdapterTypeFactory>
        implements FlightCancellationContract.View, FlightCancellationViewHolder.FlightCancellationListener {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";

    private static final int REFUND_STEPS_NUMBER = 2;
    public static final int REQUEST_REFUND_CANCELLATION = 1;
    public static final int REQUEST_REASON_AND_PROOF_CANCELLATION = 2;

    private String invoiceId;
    private List<FlightCancellationViewModel> flightCancellationViewModelList;
    private FlightCancellationWrapperViewModel flightCancellationWrapperViewModel;
    List<FlightCancellationJourney> flightCancellationJourneyList;

    @Inject
    FlightCancellationPresenter flightCancellationPresenter;

    private LinearLayout btnContainer;
    private AppCompatButton btnSubmit;
    private AppCompatTextView txtCancellationSteps;

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
        View view = inflater.inflate(R.layout.fragment_flight_cancellation, container, false);

        btnContainer = view.findViewById(R.id.btn_container);
        txtCancellationSteps = view.findViewById(R.id.txt_cancellation_steps);
        btnSubmit = view.findViewById(R.id.button_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightCancellationPresenter.onNextButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initialVariable();

        flightCancellationPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    protected BaseListAdapter<FlightCancellationViewModel, FlightCancellationAdapterTypeFactory> createAdapterInstance() {
        BaseListAdapter<FlightCancellationViewModel, FlightCancellationAdapterTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_flight_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    private void initialVariable() {
        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
        flightCancellationJourneyList = getArguments().getParcelableArrayList(EXTRA_CANCEL_JOURNEY);
        flightCancellationWrapperViewModel = new FlightCancellationWrapperViewModel();
        flightCancellationWrapperViewModel.setInvoice(invoiceId);
        flightCancellationWrapperViewModel.setCancellationReasonAndAttachment(new FlightCancellationReasonAndAttachmentViewModel());
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
        getAdapter().clearAllElements();
        showLoading();
        flightCancellationPresenter.onViewCreated();
    }

    @Override
    protected FlightCancellationAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightCancellationAdapterTypeFactory(this);
    }

    @Override
    public void renderCancelableList() {
        hideLoading();
        hideFullLoading();
        renderList(flightCancellationViewModelList);

        if (flightCancellationViewModelList.size() > 0) {
            txtCancellationSteps.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
        } else {
            txtCancellationSteps.setVisibility(View.GONE);
            btnContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFlightCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        this.flightCancellationViewModelList = flightCancellationViewModelList;
    }

    @Override
    public void setSelectedCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        this.flightCancellationWrapperViewModel.setGetCancellations(flightCancellationViewModelList);
    }

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public List<FlightCancellationJourney> getFlightCancellationJourney() {
        return flightCancellationJourneyList;
    }

    @Override
    public List<FlightCancellationViewModel> getCurrentFlightCancellationViewModel() {
        return flightCancellationViewModelList;
    }

    @Override
    public List<FlightCancellationViewModel> getSelectedCancellationViewModel() {
        return flightCancellationWrapperViewModel.getGetCancellations();
    }

    @Override
    public void showShouldChooseAtLeastOnePassengerError() {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                getString(R.string.flight_cancellation_should_choose_at_least_one_passenger_error));
    }

    @Override
    public void hideFullLoading() {
        btnContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFullLoading() {
        btnContainer.setVisibility(View.GONE);
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();
        txtCancellationSteps.setVisibility(View.GONE);
        btnContainer.setVisibility(View.GONE);
        super.showGetListError(throwable);
    }

    @Override
    public void onPassengerChecked(FlightCancellationPassengerViewModel passengerViewModel, int position) {
        flightCancellationPresenter.checkPassenger(passengerViewModel, position);
    }

    @Override
    public void onPassengerUnchecked(FlightCancellationPassengerViewModel passengerViewModel, int position) {
        flightCancellationPresenter.uncheckPassenger(passengerViewModel, position);
    }

    @Override
    public boolean shouldCheckAll() {
        if (flightCancellationViewModelList.size() == 1 &&
                flightCancellationViewModelList.get(0).getPassengerViewModelList().size() == 1) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void navigateToRefundCancellationPage() {
        startActivityForResult(
                FlightCancellationRefundDetailActivity.getCallingIntent(getActivity(),
                        flightCancellationWrapperViewModel, REFUND_STEPS_NUMBER),
                REQUEST_REFUND_CANCELLATION
        );
    }

    @Override
    public void navigateToReasonAndProofPage() {
        startActivityForResult(
                FlightCancellationReasonAndProofActivity.getCallingIntent(
                        getActivity(), flightCancellationWrapperViewModel
                ),
                REQUEST_REASON_AND_PROOF_CANCELLATION
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_REFUND_CANCELLATION:
                if (resultCode == RESULT_OK) {
                    closeCancellationPage();
                }
                break;
            case REQUEST_REASON_AND_PROOF_CANCELLATION:
                if (resultCode == RESULT_OK) {
                    closeCancellationPage();
                }
                break;
        }
    }

    @Override
    public void onRetryClicked() {
        txtCancellationSteps.setVisibility(View.VISIBLE);
        showLoading();
        flightCancellationPresenter.onViewCreated();
    }

    private void closeCancellationPage() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }
}
