package com.tokopedia.flight.dashboard.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.dashboard.view.adapter.viewholder.FlightClassViewHolder;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;

import java.util.List;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassesAdapter extends BaseListV2Adapter<FlightClassViewModel> {
    public FlightClassesAdapter(OnBaseListV2AdapterListener<FlightClassViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightClassesAdapter(@Nullable List<FlightClassViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightClassViewModel> onBaseListV2AdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, FlightClassViewHolder.LAYOUT);
        return new FlightClassViewHolder(view);
    }
}
