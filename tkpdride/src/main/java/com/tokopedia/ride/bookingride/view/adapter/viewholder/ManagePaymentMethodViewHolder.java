package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.bookingride.view.adapter.PaymentMethodItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Vishal on 27th Sep, 2017.
 */

public class ManagePaymentMethodViewHolder extends AbstractViewHolder<PaymentMethodViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.row_payment_method;

    @BindView(R2.id.payment_method_image)
    ImageView imageView;
    @BindView(R2.id.payment_method_name)
    TextView paymentMethodName;
    @BindView(R2.id.tokocash_balance)
    TextView tokocashBalance;
    @BindView(R2.id.bank_image)
    ImageView bankImage;
    @BindView(R2.id.tv_auto_debit_not_allowed)
    TextView autoDebitTextView;

    private PaymentMethodItemClickListener itemClickListener;
    private PaymentMethodViewModel paymentMethodViewModel;
    private Context context;

    public ManagePaymentMethodViewHolder(View parent, PaymentMethodItemClickListener itemClickListener) {
        super(parent);
        context = parent.getContext();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(PaymentMethodViewModel element) {
        paymentMethodViewModel = element;

        if (paymentMethodViewModel.getTokoCashBalance() != null && paymentMethodViewModel.getTokoCashBalance().length() > 0) {
            tokocashBalance.setText("(" + paymentMethodViewModel.getTokoCashBalance() + ")");
            tokocashBalance.setVisibility(View.VISIBLE);
        } else {
            tokocashBalance.setVisibility(View.GONE);
        }

        paymentMethodName.setText(element.getName());

        if (element.getType().equalsIgnoreCase(PaymentMethodViewModel.MODE_WALLET)) {
            imageView.setImageResource(R.drawable.ic_tokocash_icon);
        } else {
            Glide.with(context).load(element.getImageUrl())
                    .asBitmap()
                    .fitCenter()
                    .dontAnimate()
                    .error(R.drawable.error_drawable)
                    .into(imageView);
        }

        if (element.getBankImage() == null) {
            bankImage.setVisibility(View.GONE);
        } else {
            bankImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(element.getBankImage())
                    .asBitmap()
                    .fitCenter()
                    .dontAnimate()
                    .error(R.drawable.error_drawable)
                    .into(bankImage);
        }

        //autoDebitTextView.setVisibility(element.isSaveWebView() ? View.VISIBLE : View.GONE);
    }

    @OnClick(R2.id.container)
    public void actionOnPaymentMethodClicked() {
        itemClickListener.onPaymentMethodSelected(paymentMethodViewModel);
    }
}
