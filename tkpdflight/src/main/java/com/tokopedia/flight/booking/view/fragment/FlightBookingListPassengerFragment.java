package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightBookingListPassengerAdapterTypeFactory;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.booking.view.presenter.FlightBookingListPassengerContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingListPassengerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightBookingListPassengerFragment extends BaseListFragment<FlightBookingPassengerViewModel, FlightBookingListPassengerAdapterTypeFactory>
        implements FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger, FlightBookingListPassengerContract.View {

    public static final String EXTRA_SELECTED_PASSENGER = "EXTRA_SELECTED_PASSENGER";
    public static final int IS_SELECTING = 1;
    public static final int IS_NOT_SELECTING = 0;

    private String selectedPassengerId;
    private FlightBookingPassengerViewModel selectedPassenger;
    @Inject
    FlightBookingListPassengerPresenter presenter;
    List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList;
    TextView txtNewPassenger;

    public static FlightBookingListPassengerFragment createInstance(FlightBookingPassengerViewModel selectedPassenger) {
        FlightBookingListPassengerFragment flightBookingListPassengerFragment = new FlightBookingListPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SELECTED_PASSENGER, selectedPassenger);
        flightBookingListPassengerFragment.setArguments(bundle);
        return flightBookingListPassengerFragment;
    }

    public FlightBookingListPassengerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedPassenger = getArguments().getParcelable(EXTRA_SELECTED_PASSENGER);
        selectedPassengerId = (selectedPassenger.getPassengerId() != null) ? selectedPassenger.getPassengerId() : "";
        flightBookingPassengerViewModelList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_list_passenger, container, false);
        txtNewPassenger = view.findViewById(R.id.txt_new_passenger);
        txtNewPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectNewPassenger();
            }
        });
        return view;
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
    public boolean isItemChecked(FlightBookingPassengerViewModel selectedItem) {
        return presenter.isPassengerSame(selectedItem);
    }

    @Override
    public void resetItemCheck() {

    }

    @Override
    public void onItemClicked(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        selectedPassenger = flightBookingPassengerViewModel;
        presenter.selectPassenger(selectedPassenger);
    }

    @Override
    public void loadData(int page) {
    }

    @Override
    protected FlightBookingListPassengerAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightBookingListPassengerAdapterTypeFactory(this);
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
        renderList(flightBookingPassengerViewModelList);
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassenger() {
        return selectedPassenger;
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

    private void onSelectNewPassenger() {
        presenter.selectPassenger(null);
    }
}
