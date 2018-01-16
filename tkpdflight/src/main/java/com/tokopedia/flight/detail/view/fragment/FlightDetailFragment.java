package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapterTypeFactory;
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFragment extends BaseListFragment<FlightDetailRouteViewModel, FlightDetailRouteTypeFactory> implements FlightDetailAdapterTypeFactory.OnFlightDetailListener {

    public static final String EXTRA_FLIGHT_DETAIL_MODEL = "EXTRA_FLIGHT_DETAIL_MODEL";
    private FlightDetailViewModel flightDetailViewModel;

    public static FlightDetailFragment createInstance(FlightDetailViewModel flightDetailViewModel) {
        FlightDetailFragment flightDetailFragment = new FlightDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_DETAIL_MODEL, flightDetailViewModel);
        flightDetailFragment.setArguments(bundle);
        return flightDetailFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_detail, container, false);
    }

    @Override
    public void loadData(int page) {
        renderList(flightDetailViewModel.getRouteList());
    }

    @Override
    protected FlightDetailRouteTypeFactory getAdapterTypeFactory() {
        return new FlightDetailAdapterTypeFactory(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightDetailViewModel = getArguments().getParcelable(EXTRA_FLIGHT_DETAIL_MODEL);
    }


    @Override
    public int getItemCount() {
        return getAdapter().getItemCount();
    }

    @Override
    public void onItemClicked(FlightDetailRouteViewModel flightDetailRouteViewModel) {

    }
}
