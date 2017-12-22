package com.tokopedia.flight.detail.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.AdapterTypeFactory;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;

/**
 * Created by alvarisi on 12/21/17.
 */

public interface FlightDetailRouteTypeFactory extends AdapterTypeFactory {
    int type(FlightDetailRouteViewModel viewModel);
}
