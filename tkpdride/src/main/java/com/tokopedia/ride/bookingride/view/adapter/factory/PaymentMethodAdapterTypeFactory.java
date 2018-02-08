package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewholder.ChangePaymentMethodViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewholder.ManagePaymentMethodViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

/**
 * Created by Vishal on 27th Sep, 2017.
 */

public class PaymentMethodAdapterTypeFactory extends BaseAdapterTypeFactory implements PaymentMethodTypeFactory {
    private final PaymentMethodItemClickListener mItemClickListener;
    public static int TYPE_MANAGE_PAYMENT = 1;
    public static int TYPE_CHOOSE_PAYMENT = 2;

    private int type = TYPE_MANAGE_PAYMENT;

    public PaymentMethodAdapterTypeFactory(PaymentMethodItemClickListener itemClickListener, int type) {
        mItemClickListener = itemClickListener;
        this.type = type;
    }

    @Override
    public int type(PaymentMethodViewModel paymentMethodViewModel) {
        if (type == TYPE_CHOOSE_PAYMENT) {
            return ChangePaymentMethodViewHolder.LAYOUT;
        }
        return ManagePaymentMethodViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == ManagePaymentMethodViewHolder.LAYOUT) {
            viewHolder = new ManagePaymentMethodViewHolder(parent, mItemClickListener);
        } else if (type == ChangePaymentMethodViewHolder.LAYOUT) {
            viewHolder = new ChangePaymentMethodViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
