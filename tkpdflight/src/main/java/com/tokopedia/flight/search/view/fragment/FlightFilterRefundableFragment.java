package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.flight.search.view.adapter.FlightFilterRefundableAdapter;
import com.tokopedia.flight.search.view.fragment.base.BaseFlightFilterFragment;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;
import com.tokopedia.flight.search.view.model.resultstatistics.RefundableStat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


public class FlightFilterRefundableFragment extends BaseFlightFilterFragment<RefundableStat>
        implements BaseListAdapter.OnBaseListV2AdapterListener<RefundableStat>,
        BaseListCheckableAdapter.OnCheckableAdapterListener<RefundableStat>{
    public static final String TAG = FlightFilterRefundableFragment.class.getSimpleName();

    FlightFilterRefundableAdapter flightFilterRefundableAdapter;

    public static FlightFilterRefundableFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterRefundableFragment fragment = new FlightFilterRefundableFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected BaseListAdapter<RefundableStat> getNewAdapter() {
        flightFilterRefundableAdapter = new FlightFilterRefundableAdapter(getContext(), this, this);
        return flightFilterRefundableAdapter;
    }

    @Override
    public void onItemClicked(RefundableStat refundableStat) {
        // no op
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<RefundableStat> refundableTypeStatList = listener.getFlightSearchStatisticModel().getRefundableTypeStatList();
        onSearchLoaded(refundableTypeStatList, refundableTypeStatList.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<RefundableStat> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel!= null) {
            List<RefundableEnum> refundableEnumList = flightFilterModel.getRefundableTypeList();
            if (refundableEnumList!= null) {
                for (int i = 0, sizei = refundableEnumList.size(); i < sizei; i++) {
                    RefundableEnum refundableEnum = refundableEnumList.get(i);
                    List<RefundableStat> refundableEnumAdapterList = flightFilterRefundableAdapter.getData();
                    if (refundableEnumAdapterList != null) {
                        for (int j = 0, sizej = refundableEnumAdapterList.size(); j < sizej; j++) {
                            RefundableStat refundableAdapterEnum = refundableEnumAdapterList.get(j);
                            if (refundableAdapterEnum.getRefundableEnum().getId() == refundableEnum.getId()) {
                                checkedPositionList.add(j);
                                break;
                            }
                        }
                    }
                }
            }
        }
        flightFilterRefundableAdapter.setCheckedPositionList(checkedPositionList);
        flightFilterRefundableAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemChecked(RefundableStat refundableStat, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<RefundableStat> refundableStatList = flightFilterRefundableAdapter.getCheckedDataList();

        List<RefundableEnum> refundableEnumList = Observable.from(refundableStatList)
                .map(new Func1<RefundableStat, RefundableEnum>() {
                    @Override
                    public RefundableEnum call(RefundableStat refundableStat) {
                        return refundableStat.getRefundableEnum();
                    }
                }).toList().toBlocking().first();
        flightFilterModel.setRefundableTypeList(refundableEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    public void resetFilter() {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        flightFilterModel.setRefundableTypeList(new ArrayList<RefundableEnum>());
        flightFilterRefundableAdapter.resetCheckedItemSet();
        flightFilterRefundableAdapter.notifyDataSetChanged();
        listener.onFilterModelChanged(flightFilterModel);
    }

}
