package com.tokopedia.flight.dashboard.view.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.dashboard.view.adapter.viewholder.FlightClassViewHolder;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassesAdapter extends BaseListAdapter<FlightClassViewModel> {
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, FlightClassViewHolder.LAYOUT);
        return new FlightClassViewHolder(view);
    }
}
