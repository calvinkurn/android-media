package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewholder.PaymentMethodViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

/**
 * Created by Vishal on 27th Sep, 2017.
 */

public class PaymentMethodAdapterTypeFactory extends BaseAdapterTypeFactory implements PaymentMethodTypeFactory {
    private final PaymentMethodItemClickListener mItemClickListener;

    public PaymentMethodAdapterTypeFactory(PaymentMethodItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int type(PaymentMethodViewModel paymentMethodViewModel) {
        return PaymentMethodViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == PaymentMethodViewHolder.LAYOUT) {
            viewHolder = new PaymentMethodViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
