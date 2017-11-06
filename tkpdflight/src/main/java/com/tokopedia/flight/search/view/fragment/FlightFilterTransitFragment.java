package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.abstraction.base.view.recyclerview.BaseListRecyclerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.adapter.FlightFilterTransitAdapter;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.TransitEnum;
import com.tokopedia.flight.search.view.model.resultstatistics.TransitStat;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nathan on 10/27/17.
 */

public class FlightFilterTransitFragment extends BaseListV2Fragment<TransitStat>
        implements BaseListV2Adapter.OnBaseListV2AdapterListener<TransitStat>,BaseListCheckableV2Adapter.OnCheckableAdapterListener<TransitStat> {
    public static final String TAG = FlightFilterTransitFragment.class.getSimpleName();

    private OnFlightFilterListener listener;
    private FlightFilterTransitAdapter flightFilterTransitAdapter;

    public static FlightFilterTransitFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterTransitFragment fragment = new FlightFilterTransitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flight_filter_transit, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        // no inject
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected BaseListV2Adapter<TransitStat> getNewAdapter() {
        flightFilterTransitAdapter = new FlightFilterTransitAdapter(this, this);
        return flightFilterTransitAdapter;
    }

    @Override
    public BaseListRecyclerView getRecyclerView(View view) {
        return (BaseListRecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return null;
    }

    @Override
    public void onItemClicked(TransitStat transitStat) {
        // no op
    }

    @Override
    public void onItemChecked(TransitStat transitStat, boolean isChecked) {
        List<TransitStat> transitStatList = flightFilterTransitAdapter.getCheckedDataList();
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<TransitEnum> transitEnumList = new ArrayList<>();
        if (transitStatList != null) {
            for (int i = 0, sizei = transitStatList.size(); i<sizei; i++) {
                transitEnumList.add(transitStatList.get(i).getTransitType());
            }
        }
        flightFilterModel.setTransitTypeList(transitEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<TransitStat> transitStats = listener.getFlightSearchStatisticModel().getTransitTypeStatList();
        onSearchLoaded(transitStats, transitStats.size());
    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (OnFlightFilterListener) context;
    }


}