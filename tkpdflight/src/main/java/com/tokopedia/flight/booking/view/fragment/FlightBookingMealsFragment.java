package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightBookingMealsAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsFragment extends BaseListFragment<FlightBookingMealViewModel> implements BaseListAdapter.OnBaseListV2AdapterListener<FlightBookingMealViewModel> {

    public static final String EXTRA_SELECTED_MEALS = "EXTRA_SELECTED_MEALS";
    public static final String EXTRA_LIST_MEALS = "EXTRA_LIST_MEALS";

    private List<FlightBookingMealViewModel> flightBookingMealViewModels;
    private FlightBookingMealMetaViewModel selectedMeals;

    public static FlightBookingMealsFragment createInstance(ArrayList<FlightBookingMealViewModel> flightBookingMealViewModels,
                                                            FlightBookingMealMetaViewModel selectedMeals) {
        FlightBookingMealsFragment flightBookingMealsFragment = new FlightBookingMealsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_MEALS, flightBookingMealViewModels);
        bundle.putParcelable(EXTRA_SELECTED_MEALS, selectedMeals);
        flightBookingMealsFragment.setArguments(bundle);
        return flightBookingMealsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        flightBookingMealViewModels = getArguments().getParcelableArrayList(EXTRA_LIST_MEALS);
        selectedMeals = getArguments().getParcelable(EXTRA_SELECTED_MEALS);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_flight_booking_meals, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_reset) {
            ((BaseListCheckableAdapter) getAdapter()).resetCheckedItemSet();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setResultAndFinish() {
        Intent intent = new Intent();
        selectedMeals.setMealViewModels(getAdapter().getData());
        intent.putExtra(EXTRA_SELECTED_MEALS, selectedMeals);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected BaseListAdapter<FlightBookingMealViewModel> getNewAdapter() {
        FlightBookingMealsAdapter flightBookingMealsAdapter = new FlightBookingMealsAdapter(this);
        flightBookingMealsAdapter.setListChecked(selectedMeals.getMealViewModels());
        return flightBookingMealsAdapter;
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightBookingMealViewModels, flightBookingMealViewModels.size());
    }

    @Override
    public void onItemClicked(FlightBookingMealViewModel flightBookingMealViewModel) {
        // no op
    }

}
