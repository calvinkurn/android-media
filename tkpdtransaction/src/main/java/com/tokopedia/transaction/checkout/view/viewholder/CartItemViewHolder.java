package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView mIvProductImage;
    private TextView mTvProductName;
    private TextView mTvProductPrice;
    private TextView mTvProductWeight;
    private TextView mTvProductTotalItem;

    private LinearLayout mLlOptionalNoteToSellerLayout;
    private TextView mTvOptionalNoteToSeller;

    private RelativeLayout mRlProductPoliciesLayout;
    private ImageView mIvFreeReturnIcon;
    private TextView mTvFreeReturnLabel;
    private TextView mTvPreOrder;
    private TextView mTvCashback;

    public CartItemViewHolder(View itemView) {
        super(itemView);

        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvProductWeight = itemView.findViewById(R.id.tv_product_weight);
        mTvProductTotalItem = itemView.findViewById(R.id.tv_product_total_item);

        mLlOptionalNoteToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);
        mTvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);

        mRlProductPoliciesLayout = itemView.findViewById(R.id.layout_policy);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        mTvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        mTvCashback = itemView.findViewById(R.id.tv_cashback);
    }

    public void bindViewHolder(CartItemModel cartItem) {
        ImageHandler.LoadImage(mIvProductImage, cartItem.getImageUrl());
        mTvProductName.setText(cartItem.getName());
        mTvProductPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(
                (int) cartItem.getPrice(), true));
        mTvProductWeight.setText(cartItem.getWeightFmt());
        mTvProductTotalItem.setText(String.valueOf(cartItem.getQuantity()));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItem.getNoteToSeller());
        mLlOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        mTvOptionalNoteToSeller.setText(cartItem.getNoteToSeller());

        mRlProductPoliciesLayout.setVisibility(isPoliciesVisible(cartItem) ?
                View.VISIBLE : View.GONE);
        mIvFreeReturnIcon.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvFreeReturnLabel.setVisibility(cartItem.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvPreOrder.setVisibility(cartItem.isPreOrder() ? View.VISIBLE : View.GONE);
        mTvCashback.setVisibility(cartItem.isCashback() ? View.VISIBLE : View.GONE);
        mTvCashback.setText(cartItem.getCashback());
    }

    private boolean isPoliciesVisible(CartItemModel cartItem) {
        return cartItem.isCashback()
                || cartItem.isFreeReturn()
                || cartItem.isPreOrder();
    }

}
