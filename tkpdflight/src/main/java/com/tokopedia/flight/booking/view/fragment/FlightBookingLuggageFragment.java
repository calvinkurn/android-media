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
import com.tokopedia.flight.booking.view.adapter.FlightBookingLuggageAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageFragment extends BaseListFragment<FlightBookingLuggageViewModel> implements BaseListAdapter.OnBaseListV2AdapterListener<FlightBookingLuggageViewModel> {

    public static final String EXTRA_SELECTED_LUGGAGE = "EXTRA_SELECTED_LUGGAGE";
    public static final String EXTRA_LIST_LUGGAGE = "EXTRA_LIST_LUGGAGE";
    private ArrayList<FlightBookingLuggageViewModel> flightBookingLuggageViewHolders;
    private FlightBookingLuggageMetaViewModel selectedLuggage;

    public static FlightBookingLuggageFragment createInstance(ArrayList<FlightBookingLuggageViewModel> flightBookingLuggageViewModels,
                                                              FlightBookingLuggageMetaViewModel selectedLuggage) {
        FlightBookingLuggageFragment flightBookingLuggageFragment = new FlightBookingLuggageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_LUGGAGE, flightBookingLuggageViewModels);
        bundle.putParcelable(EXTRA_SELECTED_LUGGAGE, selectedLuggage);
        flightBookingLuggageFragment.setArguments(bundle);
        return flightBookingLuggageFragment;
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
    protected BaseListAdapter<FlightBookingLuggageViewModel> getNewAdapter() {
        FlightBookingLuggageAdapter flightBookingLuggageAdapter = new FlightBookingLuggageAdapter(this);
        flightBookingLuggageAdapter.setSelectedLuggage(selectedLuggage.getLuggages());
        return flightBookingLuggageAdapter;
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightBookingLuggageViewHolders, flightBookingLuggageViewHolders.size());
    }

    @Override
    public void onItemClicked(FlightBookingLuggageViewModel flightBookingLuggageViewModel) {
        List<FlightBookingLuggageViewModel> viewModels = new ArrayList<>();
        viewModels.add(flightBookingLuggageViewModel);
        selectedLuggage.setLuggages(viewModels);
        ((FlightBookingLuggageAdapter) getAdapter()).setSelectedLuggage(selectedLuggage.getLuggages());
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
