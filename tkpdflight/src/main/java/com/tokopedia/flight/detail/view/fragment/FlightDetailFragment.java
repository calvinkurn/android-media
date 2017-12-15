package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.utils.MethodChecker;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapter;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFragment extends BaseListFragment<FlightDetailRouteViewModel> implements BaseListAdapter.OnBaseListV2AdapterListener<FlightDetailRouteViewModel> {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightDetailViewModel = getArguments().getParcelable(EXTRA_FLIGHT_DETAIL_MODEL);
    }

    @Override
    protected BaseListAdapter<FlightDetailRouteViewModel> getNewAdapter() {
        return new FlightDetailAdapter(getContext(), this);
    }

    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightDetailViewModel.getRouteList(), flightDetailViewModel.getRouteList().size());
    }

    @Override
    public void onItemClicked(FlightDetailRouteViewModel flightSearchData) {

    }


}
