package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightBookingSavedPassengerAdapterTypeFactory;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingSavedPassengerViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightBookingSavedPassengerFragment extends BaseListFragment<FlightBookingPassengerViewModel, FlightBookingSavedPassengerAdapterTypeFactory>
        implements FlightBookingSavedPassengerViewHolder.ListenerCheckedSavedPassenger {

    public static final String EXTRA_SELECTED_PASSENGER = "EXTRA_SELECTED_PASSENGER";

    private FlightBookingPassengerViewModel selectedPassenger;

    public static FlightBookingSavedPassengerFragment createInstance(FlightBookingPassengerViewModel selectedPassenger) {
        FlightBookingSavedPassengerFragment flightBookingSavedPassengerFragment = new FlightBookingSavedPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SELECTED_PASSENGER, selectedPassenger);
        flightBookingSavedPassengerFragment.setArguments(bundle);
        return flightBookingSavedPassengerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        selectedPassenger = getArguments().getParcelable(EXTRA_SELECTED_PASSENGER);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // karena layout yang di butuhkan sama, maka digunakan layout yg sama dengan booking luggage
        View view = inflater.inflate(R.layout.fragment_booking_luggage, container, false);
        Button button = (Button) view.findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SELECTED_PASSENGER, selectedPassenger);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public boolean isItemChecked(FlightBookingPassengerViewModel selectedItem) {
        return false;
    }

    @Override
    public void resetItemCheck() {

    }

    @Override
    public void onItemClicked(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {

    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected FlightBookingSavedPassengerAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightBookingSavedPassengerAdapterTypeFactory(this);
    }
}
