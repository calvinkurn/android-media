package com.tokopedia.flight.detail.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailAdapter extends BaseAdapter<FlightDetailRouteTypeFactory> {

    public FlightDetailAdapter(FlightDetailRouteTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

}
