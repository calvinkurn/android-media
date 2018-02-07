package com.tokopedia.flight.airport.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;
import com.tokopedia.flight.airport.service.GetAirportListService;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory;
import com.tokopedia.flight.airport.view.adapter.FlightAirportViewHolder;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerView;
import com.tokopedia.flight.common.di.component.FlightComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightAirportPickerFragment extends BaseSearchListFragment<FlightAirportDB, FlightAirportAdapterTypeFactory>
        implements FlightAirportPickerView, FlightAirportViewHolder.FilterTextListener {

    public static final String EXTRA_SELECTED_AIRPORT = "extra_selected_aiport";
    public static final String FLIGHT_AIRPORT = "flight_airport";
    private static final long DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(0);
    @Inject
    FlightAirportPickerPresenter flightAirportPickerPresenter;
    private boolean isFirstTime = true;

    public static FlightAirportPickerFragment getInstance() {
        return new FlightAirportPickerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_airport_picker, container, false);
        view.requestFocus();
        return view;
    }

    @Override
    public void loadData(int page) {
        if (isFirstTime) searchInputView.setVisibility(View.GONE);
        flightAirportPickerPresenter.getAirportList(searchInputView.getSearchText(), isFirstTime);
    }

    @Override
    protected FlightAirportAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightAirportAdapterTypeFactory(this);
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
    public void onItemClicked(FlightAirportDB flightAirportDB) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_AIRPORT, flightAirportDB);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onSearchSubmitted(String text) {
        flightAirportPickerPresenter.getAirportList(searchInputView.getSearchText(), isFirstTime);
    }

    @Override
    public void onSearchTextChanged(String text) {
        flightAirportPickerPresenter.getAirportList(text, isFirstTime);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flightAirportPickerPresenter.detachView();
    }

    @Override
    protected long getDelayTextChanged() {
        return DELAY_TEXT_CHANGED;
    }

    @Override
    public void updateAirportListOnBackground() {
        GetAirportListService.startService(getActivity(), ((FlightModuleRouter)getActivity().getApplication()).getLongConfig(FLIGHT_AIRPORT));
    }

    @Override
    public void renderList(@NonNull List<FlightAirportDB> list) {
        if (isFirstTime) {
            flightAirportPickerPresenter.checkAirportVersion(((FlightModuleRouter) getActivity().getApplication()).getLongConfig(FLIGHT_AIRPORT));
            searchInputView.setVisibility(View.VISIBLE);
            isFirstTime = false;
        }
        super.renderList(list);
    }

    @Override
    public void showGetAirportListLoading() {
        showLoading();
    }

    @Override
    public void hideGetAirportListLoading() {
        hideLoading();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public String getFilterText() {
        return searchInputView.getSearchText();
    }
}