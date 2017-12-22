package com.tokopedia.flight.search.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightFilterRefundableViewHolder;
import com.tokopedia.flight.search.view.model.resultstatistics.RefundableStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterRefundableAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<RefundableStat> {
    private CheckableBaseViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterRefundableAdapterTypeFactory(CheckableBaseViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(RefundableStat viewModel) {
        return FlightFilterRefundableViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterRefundableViewHolder.LAYOUT) {
            return new FlightFilterRefundableViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
