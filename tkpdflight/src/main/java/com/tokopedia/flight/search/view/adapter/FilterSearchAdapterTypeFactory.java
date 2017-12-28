package com.tokopedia.flight.search.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightSearchViewHolderV2;
import com.tokopedia.flight.search.view.model.EmptyResultViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FilterSearchAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory, ErrorNetworkViewHolder.OnRetryListener {

    private FlightSearchAdapter.OnBaseFlightSearchAdapterListener baseFlightSearchAdapterListener;

    public FilterSearchAdapterTypeFactory(FlightSearchAdapter.OnBaseFlightSearchAdapterListener baseFlightSearchAdapterListener) {
        this.baseFlightSearchAdapterListener = baseFlightSearchAdapterListener;
    }

    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightSearchViewHolderV2.LAYOUT) {
            return new FlightSearchViewHolderV2(parent, baseFlightSearchAdapterListener);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent, this);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public void onRetryClicked() {
        baseFlightSearchAdapterListener.onRetryClicked();
    }

    public int type(FlightSearchViewModel flightSearchViewModel) {
        return FlightSearchViewHolderV2.LAYOUT;
    }
}
