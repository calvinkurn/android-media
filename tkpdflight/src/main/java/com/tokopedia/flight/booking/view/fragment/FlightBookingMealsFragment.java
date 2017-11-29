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
import com.tokopedia.flight.booking.view.adapter.FlightBookingMealsAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsFragment extends BaseListFragment<FlightBookingAmenityViewModel> implements BaseListAdapter.OnBaseListV2AdapterListener<FlightBookingAmenityViewModel> {

    public static final String EXTRA_SELECTED_MEALS = "EXTRA_SELECTED_MEALS";
    public static final String EXTRA_LIST_MEALS = "EXTRA_LIST_MEALS";

    private List<FlightBookingAmenityViewModel> flightBookingAmenityViewModels;
    private FlightBookingAmenityMetaViewModel selectedMeals;

    public static FlightBookingMealsFragment createInstance(ArrayList<FlightBookingAmenityViewModel> flightBookingAmenityViewModels,
                                                            FlightBookingAmenityMetaViewModel selectedMeals) {
        FlightBookingMealsFragment flightBookingMealsFragment = new FlightBookingMealsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_MEALS, flightBookingAmenityViewModels);
        bundle.putParcelable(EXTRA_SELECTED_MEALS, selectedMeals);
        flightBookingMealsFragment.setArguments(bundle);
        return flightBookingMealsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        flightBookingAmenityViewModels = getArguments().getParcelableArrayList(EXTRA_LIST_MEALS);
        selectedMeals = getArguments().getParcelable(EXTRA_SELECTED_MEALS);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_booking_meal, container, false);
        Button buttonSubmit = (Button) view.findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultAndFinish();
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

    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_MEALS, selectedMeals);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected BaseListAdapter<FlightBookingAmenityViewModel> getNewAdapter() {
        FlightBookingMealsAdapter flightBookingMealsAdapter = new FlightBookingMealsAdapter(getActivity(), this);
        flightBookingMealsAdapter.setSelectedViewModels(selectedMeals.getAmenities());
        return flightBookingMealsAdapter;
    }

    @Override
    public void onItemClicked(FlightBookingAmenityViewModel flightBookingAmenityViewModel) {
        List<FlightBookingAmenityViewModel> viewModels = new ArrayList<>();
        viewModels.add(flightBookingAmenityViewModel);
        selectedMeals.setAmenities(viewModels);
        ((FlightBookingMealsAdapter) getAdapter()).setSelectedViewModels(selectedMeals.getAmenities());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightBookingAmenityViewModels, flightBookingAmenityViewModels.size());
    }

}
