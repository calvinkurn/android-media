package com.tokopedia.flight.flightsearch.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportComponent;
import com.tokopedia.flight.airport.di.FlightAirportModule;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.flightsearch.adapter.FlightSearchAdapter;
import com.tokopedia.flight.flightsearch.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.flightsearch.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.flightsearch.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.flightsearch.presenter.FlightSearchPresenter;
import com.tokopedia.flight.flightsearch.view.FlightSearchView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchFragment extends BaseListFragment<FlightSearchSingleRouteDB> implements FlightSearchView {

    @Inject
    public FlightSearchPresenter flightSearchPresenter;

    public static FlightSearchFragment newInstance() {
        Bundle args = new Bundle();
        FlightSearchFragment fragment = new FlightSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        DaggerFlightSearchComponent.builder()
                .flightComponent(((FlightModuleRouter)getActivity().getApplication()).getFlightComponent() )
                .build()
                .inject(this);
        flightSearchPresenter.attachView(this);
    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        return new FlightSearchAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

//        String jsonString = loadJSONFromAsset();
//        Type flightSearchType = new TypeToken<DataResponse<List<FlightSearchData>>>() {}.getType();
//        DataResponse<FlightSearchData> data = new Gson().fromJson(jsonString, flightSearchType);
        return view;
    }

//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getContext().getAssets().open("flight_search_dummy.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }


    @Override
    public void onResume() {
        super.onResume();
        flightSearchPresenter.searchFlight();
    }

    @Override
    public void onPause() {
        super.onPause();
        flightSearchPresenter.detachView();
    }

    protected int getFragmentLayout() {
        return com.tokopedia.abstraction.R.layout.fragment_base_list;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(FlightSearchSingleRouteDB flightSearchSingleRouteDB) {

    }
}
