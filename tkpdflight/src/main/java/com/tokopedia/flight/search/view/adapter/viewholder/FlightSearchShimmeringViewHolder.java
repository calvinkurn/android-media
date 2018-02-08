package com.tokopedia.flight.search.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.MethodChecker;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.view.FlightMultiAirlineView;
import com.tokopedia.flight.search.util.DurationUtil;
import com.tokopedia.flight.search.view.adapter.FilterSearchAdapterTypeFactory;
import com.tokopedia.flight.search.view.model.Duration;
import com.tokopedia.flight.search.view.model.FlightSearchShimmeringViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.ArrayList;
import java.util.List;

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
