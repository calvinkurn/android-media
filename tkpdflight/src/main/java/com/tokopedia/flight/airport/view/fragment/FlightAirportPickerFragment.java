package com.tokopedia.flight.airport.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerView;
import com.tokopedia.flight.common.di.component.FlightComponent;

import javax.inject.Inject;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightAirportPickerFragment extends BaseSearchListFragment<FlightAirportDB> implements FlightAirportPickerView{

    private static final String EXTRA_SELECTED_AIRPORT = "extra_selected_aiport";

    @Inject
    FlightAirportPickerPresenterImpl flightAirportPickerPresenter;

    public static FlightAirportPickerFragment getInstance() {
        return new FlightAirportPickerFragment();
    }

    @Override
    protected void initInjector() {
        DaggerFlightAirportComponent.builder()
                .flightAirportModule(new FlightAirportModule())
                .flightComponent(getComponent(FlightComponent.class))
                .build()
                .inject(this);
        flightAirportPickerPresenter.attachView(this);
    }

    @Override
    protected BaseListAdapter<FlightAirportDB> getNewAdapter() {
        return new FlightAirportAdapter();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_flight_airport_picker;
    }

    @Override
    public void onItemClicked(FlightAirportDB flightAirportDB) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_AIRPORT, flightAirportDB);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onSearchSubmitted(String text) {
        super.onSearchSubmitted(text);
        flightAirportPickerPresenter.getAirportList(text);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}