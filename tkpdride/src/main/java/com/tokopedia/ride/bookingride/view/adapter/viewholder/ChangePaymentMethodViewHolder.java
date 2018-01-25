package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.common.configuration.PaymentMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vishal on 27th Sep, 2017.
 */

public class ChangePaymentMethodViewHolder extends AbstractViewHolder<PaymentMethodViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.row_change_payment_method;

    @BindView(R2.id.payment_method_image)
    ImageView imageView;
    @BindView(R2.id.payment_method_name)
    TextView paymentMethodName;
    @BindView(R2.id.img_tick)
    ImageView tickImage;
    @BindView(R2.id.tokocash_balance)
    TextView otherInfoTextView;

    private PaymentMethodItemClickListener itemClickListener;
    private PaymentMethodViewModel paymentMethodViewModel;
    private Context context;

    public ChangePaymentMethodViewHolder(View parent, PaymentMethodItemClickListener itemClickListener) {
        super(parent);
        context = parent.getContext();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(PaymentMethodViewModel element) {
        paymentMethodViewModel = element;

        otherInfoTextView.setTextColor(ContextCompat.getColor(context, R.color.grey_700));
        if (paymentMethodViewModel.getTokoCashBalance() != null && paymentMethodViewModel.getTokoCashBalance().length() > 0) {
            otherInfoTextView.setText(context.getString(R.string.info_your_balance_is) + " " + paymentMethodViewModel.getTokoCashBalance());
        } else if (paymentMethodViewModel.getType().equalsIgnoreCase(PaymentMode.CC)) {
            if (paymentMethodViewModel.isSaveWebView()) {
                otherInfoTextView.setText(context.getString(R.string.auto_debit_not_allowed));
                otherInfoTextView.setTextColor(ContextCompat.getColor(context, R.color.red_500));
            } else {
                otherInfoTextView.setText(context.getString(R.string.auto_debit_allowed));
            }
        }

        paymentMethodName.setText(element.getName());

        if (element.getType().equalsIgnoreCase(PaymentMode.WALLET)) {
            imageView.setImageResource(R.drawable.ic_tokocash_icon);
        } else {
            Glide.with(context).load(element.getBankImage())
                    .asBitmap()
                    .fitCenter()
                    .dontAnimate()
                    .error(R.drawable.tokocash)
                    .into(imageView);
        }

        tickImage.setVisibility(paymentMethodViewModel.isActive() ? View.VISIBLE : View.GONE);
    }

    @OnClick(R2.id.container)
    public void actionOnPaymentMethodClicked() {
        itemClickListener.onPaymentMethodSelected(paymentMethodViewModel);
    }
}
