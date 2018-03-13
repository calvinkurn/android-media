package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingListPassengerAdapterTypeFactory;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingNewPassengerViewHolder;
import com.tokopedia.flight.booking.view.presenter.FlightBookingListPassengerContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingListPassengerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingNewPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.view.activity.FlightPassengerUpdateActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightBookingListPassengerFragment extends BaseListFragment<FlightBookingPassengerViewModel, FlightBookingListPassengerAdapterTypeFactory>
        implements FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger, FlightBookingNewPassengerViewHolder.ListenerClickedNewPassenger,
        FlightBookingListPassengerContract.View {

    public static final String EXTRA_SELECTED_PASSENGER = "EXTRA_SELECTED_PASSENGER";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    public static final int IS_SELECTING = 1;
    public static final int IS_NOT_SELECTING = 0;

    private String selectedPassengerId;
    private String requestId;
    private FlightBookingPassengerViewModel selectedPassenger;
    @Inject
    FlightBookingListPassengerPresenter presenter;
    List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList;

    public static FlightBookingListPassengerFragment createInstance(FlightBookingPassengerViewModel selectedPassenger,
                                                                    String requestId) {
        FlightBookingListPassengerFragment flightBookingListPassengerFragment = new FlightBookingListPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SELECTED_PASSENGER, selectedPassenger);
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        flightBookingListPassengerFragment.setArguments(bundle);
        return flightBookingListPassengerFragment;
    }

    public FlightBookingListPassengerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedPassenger = getArguments().getParcelable(EXTRA_SELECTED_PASSENGER);
        requestId = getArguments().getString(EXTRA_REQUEST_ID);
        selectedPassengerId = (selectedPassenger.getPassengerId() != null) ? selectedPassenger.getPassengerId() : "";
        flightBookingPassengerViewModelList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking_list_passenger, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.onViewCreated();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
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
    public void onItemClicked(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        presenter.selectPassenger(flightBookingPassengerViewModel);
    }

    @Override
    public void loadData(int page) {
        showLoading();
    }

    @Override
    protected FlightBookingListPassengerAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightBookingListPassengerAdapterTypeFactory(this, this);
    }

    @Override
    public List<FlightBookingPassengerViewModel> getPassengerViewModelList() {
        return flightBookingPassengerViewModelList;
    }

    @Override
    public void setPassengerViewModelList(List<FlightBookingPassengerViewModel> passengerViewModelList) {
        this.flightBookingPassengerViewModelList = passengerViewModelList;
    }

    @Override
    public void renderPassengerList() {
        hideLoading();
        super.isLoadingInitialData = true;
        renderList(flightBookingPassengerViewModelList);

        if (flightBookingPassengerViewModelList.size() > 0) {
            addNewPassengerElement();
        }
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassenger() {
        return selectedPassenger;
    }

    @Override
    public void setCurrentPassanger(FlightBookingPassengerViewModel selectedPassenger) {
        this.selectedPassenger = selectedPassenger;
    }

    @Override
    public String getSalutationString(int resId) {
        return getString(resId);
    }

    @Override
    public void onSelectPassengerSuccess(FlightBookingPassengerViewModel selectedPassenger) {
        if (selectedPassenger != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_SELECTED_PASSENGER, selectedPassenger);
            getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
            getActivity().setResult(Activity.RESULT_OK, null);
        }
        getActivity().finish();
    }

    @Override
    public String getSelectedPassengerId() {
        return selectedPassengerId;
    }

    @Override
    public void showPassengerSelectedError(String passengerType) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), String.format(getString(R.string.flight_booking_list_passenger_selected_error), passengerType));
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void onGetListError(Throwable throwable) {
        super.showGetListError(throwable);
    }

    private void onSelectNewPassenger() {
        presenter.selectPassenger(null);
    }

    @Override
    public void deletePassenger(String passengerId) {
        presenter.deletePassenger(passengerId);
    }

    @Override
    public void editPassenger(String passengerId) {
        startActivity(
                FlightPassengerUpdateActivity.getCallingIntent(getActivity(), passengerId)
        );
    }

    private void addNewPassengerElement() {
        getAdapter().addElement(new FlightBookingNewPassengerViewModel(
                getString(R.string.flight_list_passenger_add_passenger_label)
        ));

        getAdapter().notifyDataSetChanged();
    }

    @Override
    public View.OnClickListener onNewPassengerClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectNewPassenger();
            }
        };
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        return new FlightBookingNewPassengerViewModel(
                getString(R.string.flight_list_passenger_add_passenger_label)
        );
    }
}
