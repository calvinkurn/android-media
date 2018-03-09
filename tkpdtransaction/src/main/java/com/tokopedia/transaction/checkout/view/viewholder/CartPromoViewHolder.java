package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.compoundview.VoucherPromoView;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartPromoViewHolder extends RecyclerView.ViewHolder {

    private VoucherPromoView mVoucherCartHachikoView;
    private SingleAddressShipmentAdapter.ActionListener mActionListener;

    public CartPromoViewHolder(View itemView,
                               SingleAddressShipmentAdapter.ActionListener actionListener) {
        super(itemView);

        mVoucherCartHachikoView = itemView.findViewById(R.id.voucher_cart_holder_view);
        mActionListener = actionListener;
    }

    public void bindViewHolder(CartItemPromoHolderData cartItemPromoHolderData, int position) {
        mVoucherCartHachikoView.setActionListener(voucherActionListener(cartItemPromoHolderData, position));

        if (cartItemPromoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
            mVoucherCartHachikoView.setCoupon(cartItemPromoHolderData.getCouponTitle(),
                    cartItemPromoHolderData.getCouponMessage(),
                    cartItemPromoHolderData.getCouponCode()
            );
        } else if (cartItemPromoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
            mVoucherCartHachikoView.setVoucher(cartItemPromoHolderData.getVoucherCode(),
                    cartItemPromoHolderData.getVoucherMessage()
            );
        } else {
            mVoucherCartHachikoView.setPromoAndCouponLabel();
            mVoucherCartHachikoView.resetView();
        }
    }

    private VoucherCartHachikoView.ActionListener voucherActionListener(final CartItemPromoHolderData cartPromo,
                                                                        final int position) {
        return new VoucherCartHachikoView.ActionListener() {
            @Override
            public void onClickUseVoucher() {
                mActionListener.onCartPromoUseVoucherPromoClicked(cartPromo, position);
            }

            @Override
            public void disableVoucherDiscount() {
                mActionListener.onCartPromoCancelVoucherPromoClicked(cartPromo, position);
            }

            @Override
            public void trackingSuccessVoucher(String voucherName) {
                mActionListener.onCartPromoTrackingSuccess(cartPromo, position);

            }

            @Override
            public void trackingCancelledVoucher() {
                mActionListener.onCartPromoTrackingCancelled(cartPromo, position);
            }
        };
    }

}
