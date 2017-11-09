package com.tokopedia.flight.detail.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.data.cloud.model.response.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailAdapter extends BaseListAdapter<Route> {
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_detail);
        return new FlightDetailViewHolder(view);
    }

    @Override
    protected void bindData(int position, RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        if(viewHolder instanceof FlightDetailViewHolder){
            ((FlightDetailViewHolder)viewHolder).bindLastPosition(isLastItemPosition(position));
            ((FlightDetailViewHolder)viewHolder).bindTransitInfo(data.size() > 1);
        }
    }
}
