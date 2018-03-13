package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.adapter.CartAdapterActionListener;
import com.tokopedia.transaction.checkout.view.compoundview.VoucherPromoView;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartVoucherPromoViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private final CartAdapterActionListener actionListener;
    private VoucherPromoView voucherCartHachikoView;
    private RecyclerView.LayoutParams layoutParams;

    public CartVoucherPromoViewHolder(View itemView, CartAdapterActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.voucherCartHachikoView = itemView.findViewById(R.id.voucher_cart_holder_view);
        layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void bindData(final CartItemPromoHolderData data, final int position) {
        this.voucherCartHachikoView.setActionListener(new VoucherCartHachikoView.ActionListener() {
            @Override
            public void onClickUseVoucher() {
                actionListener.onCartPromoUseVoucherPromoClicked(data, position);
            }

            @Override
            public void disableVoucherDiscount() {
                actionListener.onCartPromoCancelVoucherPromoClicked(data, position);
            }

            @Override
            public void trackingSuccessVoucher(String voucherName) {
                actionListener.onCartPromoTrackingSuccess(data, position);
            }

            @Override
            public void trackingCancelledVoucher() {
                actionListener.onCartPromoTrackingCancelled(data, position);
            }
        });

        if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
            voucherCartHachikoView.setCoupon(data.getCouponTitle(),
                    data.getCouponMessage(),
                    data.getCouponCode()
            );
            layoutParams.bottomMargin = 0;
        } else if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
            voucherCartHachikoView.setVoucher(data.getVoucherCode(),
                    data.getVoucherMessage()
            );
            layoutParams.bottomMargin = 0;
        } else {
            voucherCartHachikoView.setPromoAndCouponLabel();
            voucherCartHachikoView.resetView();
            layoutParams.bottomMargin = (int) voucherCartHachikoView.getContext().getResources()
                    .getDimension(R.dimen.new_margin_med);
        }
        itemView.setLayoutParams(layoutParams);
    }
}
