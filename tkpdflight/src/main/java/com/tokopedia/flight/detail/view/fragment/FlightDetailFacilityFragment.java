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
import com.tokopedia.flight.detail.view.adapter.FlightDetailFacilityAdapter;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityFragment extends BaseListFragment<FlightDetailRouteViewModel> implements BaseListAdapter.OnBaseListV2AdapterListener<FlightDetailRouteViewModel> {

    public static final String EXTRA_FLIGHT_DETAIL_MODEL = "EXTRA_FLIGHT_DETAIL_MODEL";

    private FlightDetailViewModel flightDetailViewModel;

    public static FlightDetailFacilityFragment createInstance(FlightDetailViewModel flightDetailViewModel) {
        FlightDetailFacilityFragment flightDetailFacilityFragment = new FlightDetailFacilityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_DETAIL_MODEL, flightDetailViewModel);
        flightDetailFacilityFragment.setArguments(bundle);
        return flightDetailFacilityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_detail, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightDetailViewModel = getArguments().getParcelable(EXTRA_FLIGHT_DETAIL_MODEL);
    }

    @Override
    protected BaseListAdapter<FlightDetailRouteViewModel> getNewAdapter() {
        return new FlightDetailFacilityAdapter(getContext(),this);
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        onSearchLoaded(flightDetailViewModel.getRouteList(), flightDetailViewModel.getRouteList().size());
    }

    @Override
    public void onItemClicked(FlightDetailRouteViewModel flightSearchData) {

    }


}
