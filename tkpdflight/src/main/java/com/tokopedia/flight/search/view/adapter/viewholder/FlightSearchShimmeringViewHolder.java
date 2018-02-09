package com.tokopedia.flight.search.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FlightSearchShimmeringViewHolder extends AbstractViewHolder<LoadingModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_search_shimmering;


    public FlightSearchShimmeringViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(final LoadingModel flightSearchViewModel) {

    }
}
