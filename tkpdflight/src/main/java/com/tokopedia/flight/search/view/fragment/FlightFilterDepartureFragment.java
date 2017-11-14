package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListV2Fragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.adapter.FlightFilterDepartureAdapter;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightResettableListener;
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.DepartureStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FlightFilterDepartureFragment extends BaseListV2Fragment<DepartureStat>
        implements BaseListV2Adapter.OnBaseListV2AdapterListener<DepartureStat>,
        BaseListCheckableV2Adapter.OnCheckableAdapterListener<DepartureStat>,
        OnFlightResettableListener {
    public static final String TAG = FlightFilterDepartureFragment.class.getSimpleName();

    private OnFlightFilterListener listener;
    FlightFilterDepartureAdapter flightFilterDepartureAdapter;

    public static FlightFilterDepartureFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterDepartureFragment fragment = new FlightFilterDepartureFragment();
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
        return inflater.inflate(R.layout.fragment_flight_filter_departure, container, false);
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
    protected void onAttachActivity(Context context) {
        listener = (OnFlightFilterListener) context;
    }

    @Override
    protected BaseListV2Adapter<DepartureStat> getNewAdapter() {
        flightFilterDepartureAdapter = new FlightFilterDepartureAdapter(this, this);
        return flightFilterDepartureAdapter;
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return null;
    }

    @Override
    public void onItemClicked(DepartureStat departureStat) {
        // no op
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<DepartureStat> departureStats = listener.getFlightSearchStatisticModel().getDepartureTimeStatList();
        onSearchLoaded(departureStats, departureStats.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<DepartureStat> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel!= null) {
            List<DepartureTimeEnum> departureTimeEnumList = flightFilterModel.getDepartureTimeList();
            if (departureTimeEnumList!= null) {
                for (int i = 0, sizei = departureTimeEnumList.size(); i < sizei; i++) {
                    DepartureTimeEnum departureTimeEnum = departureTimeEnumList.get(i);
                    List<DepartureStat> departureStatList = flightFilterDepartureAdapter.getData();
                    if (departureStatList != null) {
                        for (int j = 0, sizej = departureStatList.size(); j < sizej; j++) {
                            DepartureStat departureStat = departureStatList.get(j);
                            if (departureStat.getDepartureTime().getId() == departureTimeEnum.getId()) {
                                checkedPositionList.add(j);
                                break;
                            }
                        }
                    }
                }
            }
        }
        flightFilterDepartureAdapter.setCheckedPositionList(checkedPositionList);
        flightFilterDepartureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(DepartureStat departureStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<DepartureStat> departureStatList = flightFilterDepartureAdapter.getCheckedDataList();

        List<DepartureTimeEnum> departureTimeEnumList = Observable.from(departureStatList)
                .map(new Func1<DepartureStat, DepartureTimeEnum>() {
                    @Override
                    public DepartureTimeEnum call(DepartureStat departureStat) {
                        return departureStat.getDepartureTime();
                    }
                }).toList().toBlocking().first();
        flightFilterModel.setDepartureTimeList(departureTimeEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void reset() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setDepartureTimeList(new ArrayList<DepartureTimeEnum>());
        flightFilterDepartureAdapter.resetCheckedItemSet();
        flightFilterDepartureAdapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }
}
