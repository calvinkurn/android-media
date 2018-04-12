package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationFragment extends BaseListFragment<FlightCancellationViewModel, FlightCancellationAdapterTypeFactory>
        implements FlightCancellationContract.View, FlightCancellationViewHolder.FlightCancellationListener {

    public static final String EXTRA_TOTAL_PRICE = "EXTRA_TOTAL_PRICE";
    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";

    private static final int REFUND_STEPS_NUMBER = 2;
    public static final int REQUEST_REVIEW_CANCELLATION = 1;
    public static final int REQUEST_REASON_AND_PROOF_CANCELLATION = 2;

    private String invoiceId;
    private List<FlightCancellationViewModel> flightCancellationViewModelList;
    private FlightCancellationWrapperViewModel flightCancellationWrapperViewModel;
    List<FlightCancellationJourney> flightCancellationJourneyList;

    @Inject
    FlightCancellationPresenter flightCancellationPresenter;

    private AppCompatButton btnSubmit;
    private AppCompatTextView txtCancellationSteps;

    public static FlightCancellationFragment createInstance(String invoiceId,
                                                            int totalPrice,
                                                            List<FlightCancellationJourney> flightCancellationJourneyList) {
        FlightCancellationFragment fragment = new FlightCancellationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putInt(EXTRA_TOTAL_PRICE, totalPrice);
        bundle.putParcelableArrayList(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) flightCancellationJourneyList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation, container, false);

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
        super.onViewCreated(view, savedInstanceState);

        initialVariable();

        flightCancellationPresenter.attachView(this);
        flightCancellationPresenter.onViewCreated();
    }

    private void initialVariable() {
        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
        flightCancellationJourneyList = getArguments().getParcelableArrayList(EXTRA_CANCEL_JOURNEY);
        flightCancellationWrapperViewModel = new FlightCancellationWrapperViewModel();
        flightCancellationWrapperViewModel.setInvoice(invoiceId);
        flightCancellationWrapperViewModel.setTotalPrice(
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(
                        getArguments().getInt(EXTRA_TOTAL_PRICE))
        );
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

    }

    @Override
    protected FlightCancellationAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightCancellationAdapterTypeFactory(this);
    }

    @Override
    public void renderCancelableList() {
        renderList(flightCancellationViewModelList);

        if (flightCancellationViewModelList.size() > 0) {
            txtCancellationSteps.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            txtCancellationSteps.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFlightCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        this.flightCancellationViewModelList = flightCancellationViewModelList;
    }

    @Override
    public void setSelectedCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        this.flightCancellationWrapperViewModel.setViewModels(flightCancellationViewModelList);
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
        return flightCancellationWrapperViewModel.getViewModels();
    }

    @Override
    public void showShouldChooseAtLeastOnePassengerError() {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                getString(R.string.flight_cancellation_should_choose_at_least_one_passenger_error));
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
                REQUEST_REVIEW_CANCELLATION
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
}
