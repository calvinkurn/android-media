package com.tokopedia.flight.orderlist.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderBaseViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;

import java.util.List;

/**
 * Created by alvarisi on 12/7/17.
 */

public class FlightOrderAdapter extends BaseAdapter<FlightOrderTypeFactory> {

    public FlightOrderAdapter(FlightOrderTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    public interface OnAdapterInteractionListener {
        void onDetailOrderClicked(FlightOrderDetailPassData viewModel);

        void onDetailOrderClicked(String orderId);

        void onHelpOptionClicked(String orderId);

        void onReBookingClicked(FlightOrderBaseViewModel item);
    }
}
