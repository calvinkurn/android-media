package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightBookingMealsAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsFragment extends BaseListFragment<FlightBookingMealViewModel> implements BaseMultipleCheckListAdapter.CheckedCallback {

    public static final String EXTRA_SELECTED_MEALS = "EXTRA_SELECTED_MEALS";
    public static final String EXTRA_LIST_MEALS = "EXTRA_LIST_MEALS";

    private Button buttonSubmit;

    private List<FlightBookingMealViewModel> flightBookingMealViewModels;
    private List<String> selectedMeals;

    public static FlightBookingMealsFragment createInstance(ArrayList<FlightBookingMealViewModel> flightBookingMealViewModels,
                                                            ArrayList<String> selectedMeals){
        FlightBookingMealsFragment flightBookingMealsFragment = new FlightBookingMealsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_LIST_MEALS, flightBookingMealViewModels);
        bundle.putStringArrayList(EXTRA_SELECTED_MEALS, selectedMeals);
        flightBookingMealsFragment.setArguments(bundle);
        return flightBookingMealsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        flightBookingMealViewModels = getArguments().getParcelableArrayList(EXTRA_LIST_MEALS);
        selectedMeals = getArguments().getStringArrayList(EXTRA_SELECTED_MEALS);
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

        if(itemId == R.id.menu_reset){
            ((FlightBookingMealsAdapter)adapter).resetCheckedItemSet();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_flight_booking_meal;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultAndFinish();
            }
        });
    }

    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_MEALS, new ArrayList<>(((FlightBookingMealsAdapter)adapter).getListChecked()));
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected BaseListAdapter<FlightBookingMealViewModel> getNewAdapter() {
        FlightBookingMealsAdapter flightBookingMealsAdapter = new FlightBookingMealsAdapter();
        flightBookingMealsAdapter.setCheckedCallback(this);
        for(String selectedMeal : selectedMeals) {
            flightBookingMealsAdapter.setChecked(selectedMeal, true);
        }
        return flightBookingMealsAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        onSearchLoaded(flightBookingMealViewModels, flightBookingMealViewModels.size());
    }

    @Override
    public void onItemClicked(FlightBookingMealViewModel flightBookingMealViewModel) {

    }

    @Override
    public void onItemChecked(Object o, boolean checked) {

    }
}
