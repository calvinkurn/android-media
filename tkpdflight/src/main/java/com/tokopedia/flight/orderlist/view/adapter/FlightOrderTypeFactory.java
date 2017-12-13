package com.tokopedia.flight.orderlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface FlightOrderTypeFactory extends AdapterTypeFactory {
    int type(FlightOrderSuccessViewModel successViewModel);

    int type(FlightOrderFailedViewModel failedViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
