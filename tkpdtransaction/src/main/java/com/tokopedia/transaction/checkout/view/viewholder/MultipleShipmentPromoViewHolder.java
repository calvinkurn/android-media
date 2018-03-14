package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.compoundview.VoucherPromoView;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

/**
 * Created by kris on 3/7/18. Tokopedia
 */

public class MultipleShipmentPromoViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView.LayoutParams layoutParams;
    private VoucherPromoView voucherCartHachikoView;

    public MultipleShipmentPromoViewHolder(View itemView) {
        super(itemView);
        this.voucherCartHachikoView = itemView.findViewById(R.id.voucher_cart_holder_view);
        layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void bindPromoView(CartItemPromoHolderData data, VoucherCartHachikoView.ActionListener actionListener) {
        voucherCartHachikoView.setActionListener(actionListener);
        if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
            voucherCartHachikoView.setCoupon(
                    data.getCouponTitle(), data.getCouponMessage(), data.getCouponCode()
            );
            layoutParams.bottomMargin = 0;
        } else if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
            voucherCartHachikoView.setVoucher(
                    data.getVoucherCode(), data.getVoucherMessage()
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
