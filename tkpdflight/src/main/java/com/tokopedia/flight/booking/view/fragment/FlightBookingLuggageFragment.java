package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightBookingLuggageAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageFragment extends BaseListV2Fragment<FlightBookingLuggageViewModel> implements BaseListV2Adapter.OnBaseListV2AdapterListener<FlightBookingLuggageViewModel> {

    public static final String EXTRA_SELECTED_LUGGAGE = "EXTRA_SELECTED_LUGGAGE";
    public static final String EXTRA_LIST_LUGGAGE = "EXTRA_LIST_LUGGAGE";
    private ArrayList<FlightBookingLuggageViewModel> flightBookingLuggageViewHolders;
    private String selectedLuggage;

    public static FlightBookingLuggageFragment createInstance(ArrayList<FlightBookingLuggageViewModel> flightBookingLuggageViewModels,
                                                              String selectedLuggage){
        FlightBookingLuggageFragment flightBookingLuggageFragment = new FlightBookingLuggageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_LUGGAGE, flightBookingLuggageViewModels);
        bundle.putString(EXTRA_SELECTED_LUGGAGE, selectedLuggage);
        flightBookingLuggageFragment.setArguments(bundle);
        return flightBookingLuggageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightBookingLuggageViewHolders = getArguments().getParcelableArrayList(EXTRA_LIST_LUGGAGE);
        selectedLuggage = getArguments().getString(EXTRA_SELECTED_LUGGAGE);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListV2Adapter<FlightBookingLuggageViewModel> getNewAdapter() {
        FlightBookingLuggageAdapter flightBookingLuggageAdapter = new FlightBookingLuggageAdapter(this);
        flightBookingLuggageAdapter.setSelectedLuggage(selectedLuggage);
        return flightBookingLuggageAdapter;
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightBookingLuggageViewHolders, flightBookingLuggageViewHolders.size());
    }

    @Override
    public void onItemClicked(FlightBookingLuggageViewModel flightBookingLuggageViewModel) {
        ((FlightBookingLuggageAdapter) getAdapter()).setSelectedLuggage(flightBookingLuggageViewModel.getId());
        getAdapter().notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_LUGGAGE, flightBookingLuggageViewModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


}
