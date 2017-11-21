package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.flight.search.view.adapter.FlightFilterDepartureAdapter;
import com.tokopedia.flight.search.view.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.DepartureStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FlightFilterDepartureFragment extends BaseFlightFilterFragment<DepartureStat>
        implements BaseListAdapter.OnBaseListV2AdapterListener<DepartureStat>,
        BaseListCheckableAdapter.OnCheckableAdapterListener<DepartureStat> {
    public static final String TAG = FlightFilterDepartureFragment.class.getSimpleName();

    FlightFilterDepartureAdapter flightFilterDepartureAdapter;

    public static FlightFilterDepartureFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterDepartureFragment fragment = new FlightFilterDepartureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseListAdapter<DepartureStat> getNewAdapter() {
        flightFilterDepartureAdapter = new FlightFilterDepartureAdapter(this, this);
        return flightFilterDepartureAdapter;
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
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setDepartureTimeList(new ArrayList<DepartureTimeEnum>());
        flightFilterDepartureAdapter.resetCheckedItemSet();
        flightFilterDepartureAdapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }
}
