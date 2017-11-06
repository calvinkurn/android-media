package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapter;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFragment extends BaseListFragment<Route> {

    public static final String EXTRA_FLIGHT_SEARCH_MODEL = "EXTRA_FLIGHT_SEARCH_MODEL";
    private FlightSearchViewModel flightSearchViewModel;
    private TextView priceTotal;

    public static FlightDetailFragment createInstance(FlightSearchViewModel flightSearchViewModel) {
        FlightDetailFragment flightDetailFragment = new FlightDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_SEARCH_MODEL, flightSearchViewModel);
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

    @Override
    protected void initView(View view) {
        super.initView(view);
        priceTotal = (TextView) view.findViewById(R.id.flight_price_total);
        priceTotal.setText(flightSearchViewModel.getTotal());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_flight_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightSearchViewModel = getArguments().getParcelable(EXTRA_FLIGHT_SEARCH_MODEL);
    }

    @Override
    protected BaseListAdapter<Route> getNewAdapter() {
        return new FlightDetailAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        onSearchLoaded(flightSearchViewModel.getRouteList(), flightSearchViewModel.getRouteList().size());
    }

    @Override
    public void onItemClicked(Route flightSearchData) {

    }
}
