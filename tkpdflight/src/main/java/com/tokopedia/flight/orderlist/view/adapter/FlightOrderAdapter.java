package com.tokopedia.flight.orderlist.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderBaseViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class FlightOrderAdapter extends BaseListAdapter<Visitable, FlightOrderTypeFactory> {

    public FlightOrderAdapter(FlightOrderTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    public interface OnAdapterInteractionListener {
        void onDetailOrderClicked(FlightOrderDetailPassData viewModel);

        void onDetailOrderClicked(String invoiceId);

        void onHelpOptionClicked(String invoiceId, int status);

        void onReBookingClicked(FlightOrderBaseViewModel item);

        void onDownloadETicket(String invoiceId);

        void onCancelOptionClicked(FlightOrderSuccessViewModel item);
    }
}
