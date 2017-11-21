package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.flight.search.view.adapter.FlightFilterAirlineAdapter;
import com.tokopedia.flight.search.view.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.resultstatistics.AirlineStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FlightFilterAirlineFragment extends BaseFlightFilterFragment<AirlineStat>
        implements BaseListAdapter.OnBaseListV2AdapterListener<AirlineStat>,
        BaseListCheckableAdapter.OnCheckableAdapterListener<AirlineStat>{
    public static final String TAG = FlightFilterAirlineFragment.class.getSimpleName();

    private FlightFilterAirlineAdapter adapter;

    public static FlightFilterAirlineFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterAirlineFragment fragment = new FlightFilterAirlineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseListAdapter<AirlineStat> getNewAdapter() {
        adapter = new FlightFilterAirlineAdapter(this, this);
        return adapter;
    }

    @Override
    public void onItemClicked(AirlineStat airlineStat) {
        // no op
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<AirlineStat> airlineStatList = listener.getFlightSearchStatisticModel().getAirlineStatList();
        onSearchLoaded(airlineStatList, airlineStatList.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<AirlineStat> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel!= null) {
            List<String> airlineList = flightFilterModel.getAirlineList();
            if (airlineList!= null) {
                for (int i = 0, sizei = airlineList.size(); i < sizei; i++) {
                    String selectedAirline = airlineList.get(i);
                    List<AirlineStat> airlineStatList = adapter.getData();
                    if (airlineStatList != null) {
                        for (int j = 0, sizej = airlineStatList.size(); j < sizej; j++) {
                            AirlineStat airlineStat = airlineStatList.get(j);
                            if (airlineStat.getAirlineDB().getId().equals(selectedAirline)) {
                                checkedPositionList.add(j);
                                break;
                            }
                        }
                    }
                }
            }
        }
        adapter.setCheckedPositionList(checkedPositionList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(AirlineStat airlineStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<AirlineStat> airlineStatList = adapter.getCheckedDataList();
        List<String> airlineList = Observable.from(airlineStatList).map(new Func1<AirlineStat, String>() {
            @Override
            public String call(AirlineStat airlineStat) {
                return airlineStat.getAirlineDB().getId();
            }
        }).toList().toBlocking().first();
        flightFilterModel.setAirlineList(airlineList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setAirlineList(new ArrayList<String>());
        adapter.resetCheckedItemSet();
        adapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }
}
