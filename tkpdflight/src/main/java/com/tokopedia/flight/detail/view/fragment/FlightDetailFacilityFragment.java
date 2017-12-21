package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.adapter.FlightDetailFacilityAdapterTypeFactory;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityFragment extends BaseListV2Fragment<FlightDetailRouteViewModel, FlightDetailFacilityAdapterTypeFactory> {

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
    protected void setInitialActionVar() {
        getAdapter().addData(flightDetailViewModel.getRouteList());
    }

    @Override
    protected FlightDetailFacilityAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightDetailFacilityAdapterTypeFactory();
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

}
