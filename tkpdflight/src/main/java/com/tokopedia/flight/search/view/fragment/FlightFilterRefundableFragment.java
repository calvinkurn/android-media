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
import com.tokopedia.abstraction.base.view.recyclerview.BaseListRecyclerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.adapter.FlightFilterRefundableAdapter;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

import java.util.HashSet;
import java.util.List;


public class FlightFilterRefundableFragment extends BaseListV2Fragment<RefundableEnum> implements BaseListV2Adapter.OnBaseListV2AdapterListener<RefundableEnum>,BaseListCheckableV2Adapter.OnCheckableAdapterListener<RefundableEnum> {
    public static final String TAG = FlightFilterRefundableFragment.class.getSimpleName();

    private OnFlightFilterListener listener;

    FlightFilterRefundableAdapter flightFilterRefundableAdapter;

    public static FlightFilterRefundableFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterRefundableFragment fragment = new FlightFilterRefundableFragment();
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
        return inflater.inflate(R.layout.fragment_flight_filter_refundable, container, false);
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
    protected BaseListV2Adapter<RefundableEnum> getNewAdapter() {
        flightFilterRefundableAdapter = new FlightFilterRefundableAdapter(this, this);
        return flightFilterRefundableAdapter;
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
    public void onItemClicked(RefundableEnum refundableEnum) {
        // no op
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        List<RefundableEnum> refundableEnumList = listener.getFlightSearchStatisticModel().getRefundableTypeList();
        onSearchLoaded(refundableEnumList, refundableEnumList.size());
    }

    @Override
    public void onSearchLoaded(@NonNull List<RefundableEnum> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        HashSet<Integer> checkedPositionList = new HashSet<>();
        if (flightFilterModel!= null) {
            List<RefundableEnum> refundableEnumList = flightFilterModel.getRefundableTypeList();
            if (refundableEnumList!= null) {
                for (int i = 0, sizei = refundableEnumList.size(); i < sizei; i++) {
                    RefundableEnum refundableEnum = refundableEnumList.get(i);
                    List<RefundableEnum> refundableEnumAdapterList = flightFilterRefundableAdapter.getData();
                    if (refundableEnumAdapterList != null) {
                        for (int j = 0, sizej = refundableEnumAdapterList.size(); j < sizej; j++) {
                            RefundableEnum refundableAdapterEnum = refundableEnumAdapterList.get(j);
                            if (refundableAdapterEnum.getId() == refundableEnum.getId()) {
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
    public void onItemChecked(RefundableEnum refundableEnum, boolean isChecked) {
        FlightFilterModel flightFilterModel = listener.getFlightFilterModel();
        List<RefundableEnum> refundableEnumList = flightFilterRefundableAdapter.getCheckedDataList();
        flightFilterModel.setRefundableTypeList(refundableEnumList);
        listener.onFilterModelChanged(flightFilterModel);
    }
}
