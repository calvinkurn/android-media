package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightBookingAmenityAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityFragment extends BaseListFragment<FlightBookingAmenityViewModel> implements BaseListAdapter.OnBaseListV2AdapterListener<FlightBookingAmenityViewModel> {

    public static final String EXTRA_SELECTED_LUGGAGE = "EXTRA_SELECTED_LUGGAGE";
    public static final String EXTRA_LIST_LUGGAGE = "EXTRA_LIST_LUGGAGE";
    private ArrayList<FlightBookingAmenityViewModel> flightBookingLuggageViewHolders;
    private FlightBookingAmenityMetaViewModel selectedLuggage;

    public static FlightBookingAmenityFragment createInstance(ArrayList<FlightBookingAmenityMetaViewModel> flightBookingLuggageViewModels,
                                                              FlightBookingAmenityMetaViewModel selectedLuggage) {
        FlightBookingAmenityFragment flightBookingAmenityFragment = new FlightBookingAmenityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_LUGGAGE, flightBookingLuggageViewModels);
        bundle.putParcelable(EXTRA_SELECTED_LUGGAGE, selectedLuggage);
        flightBookingAmenityFragment.setArguments(bundle);
        return flightBookingAmenityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        flightBookingLuggageViewHolders = getArguments().getParcelableArrayList(EXTRA_LIST_LUGGAGE);
        selectedLuggage = getArguments().getParcelable(EXTRA_SELECTED_LUGGAGE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<FlightBookingAmenityViewModel> getNewAdapter() {
        FlightBookingAmenityAdapter flightBookingAmenityAdapter = new FlightBookingAmenityAdapter(getActivity(), this);
        flightBookingAmenityAdapter.setSelectedLuggage(selectedLuggage.getAmenities());
        return flightBookingAmenityAdapter;
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightBookingLuggageViewHolders, flightBookingLuggageViewHolders.size());
    }

    @Override
    public void onItemClicked(FlightBookingAmenityViewModel flightBookingLuggageViewModel) {
        List<FlightBookingAmenityViewModel> viewModels = new ArrayList<>();
        viewModels.add(flightBookingLuggageViewModel);
        selectedLuggage.setAmenities(viewModels);
        ((FlightBookingAmenityAdapter) getAdapter()).setSelectedLuggage(selectedLuggage.getAmenities());
        getAdapter().notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_luggage, container, false);
        Button button = (Button) view.findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SELECTED_LUGGAGE, selectedLuggage);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
