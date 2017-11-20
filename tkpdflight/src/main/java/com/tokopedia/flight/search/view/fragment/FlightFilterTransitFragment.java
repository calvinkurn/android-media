package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.flight.search.view.adapter.FlightFilterTransitAdapter;
import com.tokopedia.flight.search.view.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.TransitEnum;
import com.tokopedia.flight.search.view.model.resultstatistics.TransitStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FlightFilterTransitFragment extends BaseFlightFilterFragment<TransitStat>
        implements BaseListV2Adapter.OnBaseListV2AdapterListener<TransitStat>,
        BaseListCheckableV2Adapter.OnCheckableAdapterListener<TransitStat>{
    public static final String TAG = FlightFilterTransitFragment.class.getSimpleName();

    private FlightFilterTransitAdapter flightFilterTransitAdapter;

    public static FlightFilterTransitFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterTransitFragment fragment = new FlightFilterTransitFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void onItemClicked(TransitStat transitStat) {
        // no op
    }

    @Override
    public void onItemChecked(TransitStat transitStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<TransitStat> transitStatList = flightFilterTransitAdapter.getCheckedDataList();

        List<TransitEnum> transitEnumList = Observable.from(transitStatList)
                .map(new Func1<TransitStat, TransitEnum>() {
            @Override
            public TransitEnum call(TransitStat transitStat) {
                return transitStat.getTransitType();
            }
        }).toList().toBlocking().first();
        flightFilterModel.setTransitTypeList(transitEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<TransitStat> transitStats = listener.getFlightSearchStatisticModel().getTransitTypeStatList();
        onSearchLoaded(transitStats, transitStats.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<TransitStat> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel!= null) {
            List<TransitEnum> transitEnumList = flightFilterModel.getTransitTypeList();
            if (transitEnumList!= null) {
                for (int i = 0, sizei = transitEnumList.size(); i < sizei; i++) {
                    TransitEnum transitEnum = transitEnumList.get(i);
                    List<TransitStat> transitStatList = flightFilterTransitAdapter.getData();
                    if (transitStatList != null) {
                        for (int j = 0, sizej = transitStatList.size(); j < sizej; j++) {
                            TransitStat transitStat = transitStatList.get(j);
                            if (transitStat.getTransitType().getId() == transitEnum.getId()) {
                                checkedPositionList.add(j);
                                break;
                            }
                        }
                    }
                }
            }
        }
        flightFilterTransitAdapter.setCheckedPositionList(checkedPositionList);
        flightFilterTransitAdapter.notifyDataSetChanged();
    }


    @Override
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setTransitTypeList(new ArrayList<TransitEnum>());
        flightFilterTransitAdapter.resetCheckedItemSet();
        flightFilterTransitAdapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }
}