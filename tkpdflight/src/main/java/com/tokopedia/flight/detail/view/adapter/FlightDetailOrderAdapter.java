package com.tokopedia.flight.detail.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderAdapter extends BaseListAdapter<FlightOrderJourney> {
    public FlightDetailOrderAdapter(Context context, OnBaseListV2AdapterListener<FlightOrderJourney> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightDetailOrderAdapter(Context context, @Nullable List<FlightOrderJourney> data, int rowPerPage, OnBaseListV2AdapterListener<FlightOrderJourney> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    public FlightDetailOrderAdapter(Context context) {
        super(context, null);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightDetailOrderViewHolder(getLayoutView(parent, R.layout.item_flight_detail_order));
    }
}
