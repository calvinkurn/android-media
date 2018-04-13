package com.tokopedia.flight.search.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightSearchShimmeringViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightSearchViewHolder;
import com.tokopedia.flight.search.view.model.EmptyResultViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FilterSearchAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory, ErrorNetworkViewHolder.OnRetryListener {

    private OnFlightSearchListener onFlightSearchListener;

    public FilterSearchAdapterTypeFactory(OnFlightSearchListener onFlightSearchListener) {
        this.onFlightSearchListener = onFlightSearchListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightSearchViewHolder.LAYOUT) {
            return new FlightSearchViewHolder(parent, onFlightSearchListener);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new EmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent);
        } else if (type == FlightSearchShimmeringViewHolder.LAYOUT) {
            return new FlightSearchShimmeringViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public void onRetryClicked() {
        onFlightSearchListener.onRetryClicked();
    }

    public int type(FlightSearchViewModel flightSearchViewModel) {
        return FlightSearchViewHolder.LAYOUT;
    }

    public int type(LoadingModel flightSearchShimmeringViewModel) {
        return FlightSearchShimmeringViewHolder.LAYOUT;
    }

    public int type(EmptyResultViewModel viewModel) {
        return EmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    public interface OnFlightSearchListener {
        void onRetryClicked();

        void onDetailClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition);

        void onItemClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition);
    }
}
