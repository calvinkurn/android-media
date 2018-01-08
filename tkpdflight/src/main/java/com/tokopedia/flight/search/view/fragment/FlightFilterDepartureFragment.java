package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.flight.search.view.adapter.FlightFilterDepartureTimeAdapterTypeFactory;
import com.tokopedia.flight.search.view.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.DepartureStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FlightFilterDepartureFragment extends BaseFlightFilterFragment<DepartureStat, FlightFilterDepartureTimeAdapterTypeFactory> {
    public static final String TAG = FlightFilterDepartureFragment.class.getSimpleName();

    public static FlightFilterDepartureFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterDepartureFragment fragment = new FlightFilterDepartureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClicked(DepartureStat departureStat) {
        // no op
    }

    @Override
    public void renderList(@NonNull List<DepartureStat> list) {
        super.renderList(list);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel != null) {
            List<DepartureTimeEnum> departureTimeEnumList = flightFilterModel.getDepartureTimeList();
            if (departureTimeEnumList != null) {
                for (int i = 0, sizei = departureTimeEnumList.size(); i < sizei; i++) {
                    DepartureTimeEnum departureTimeEnum = departureTimeEnumList.get(i);
                    for (int j = 0, sizej = list.size(); j < sizej; j++) {
                        DepartureStat departureStat = list.get(j);
                        if (departureStat.getDepartureTime().getId() == departureTimeEnum.getId()) {
                            checkedPositionList.add(j);
                            break;
                        }
                    }
                }
            }
        }
        adapter.setCheckedPositionList(checkedPositionList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(DepartureStat departureStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<DepartureStat> departureStatList = adapter.getCheckedDataList();

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
        adapter.resetCheckedItemSet();
        adapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void loadData(int page) {
        List<DepartureStat> airlineStatList = listener.getFlightSearchStatisticModel().getDepartureTimeStatList();
        renderList(airlineStatList);
    }

    @Override
    protected FlightFilterDepartureTimeAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightFilterDepartureTimeAdapterTypeFactory(this);
    }

    @Override
    public boolean isChecked(int position) {
        return adapter.isChecked(position);
    }

    @Override
    public void updateListByCheck(boolean isChecked, int position) {
        adapter.updateListByCheck(isChecked, position);
    }
}
