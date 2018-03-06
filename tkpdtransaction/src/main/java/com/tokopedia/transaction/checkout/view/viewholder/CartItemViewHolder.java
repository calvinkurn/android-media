package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartItemViewHolder extends RecyclerView.ViewHolder {

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final NumberFormat CURRENCY_RUPIAH = NumberFormat.getCurrencyInstance(LOCALE_ID);

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

        mRlProductPoliciesLayout = itemView.findViewById(R.id.rl_product_policies_layout);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        mTvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        mTvCashback = itemView.findViewById(R.id.tv_cashback);
    }

    public void bindViewHolder(CartItemModel cartItemModel) {
        ImageHandler.LoadImage(mIvProductImage, cartItemModel.getImageUrl());
        mTvProductName.setText(cartItemModel.getName());
        mTvProductPrice.setText(CURRENCY_RUPIAH.format(cartItemModel.getPrice()));
        mTvProductWeight.setText(cartItemModel.getWeightFmt());
        mTvProductTotalItem.setText(String.valueOf(cartItemModel.getQuantity()));

        boolean isEmptyNotes = TextUtils.isEmpty(cartItemModel.getNoteToSeller());
        mLlOptionalNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        mTvOptionalNoteToSeller.setText(cartItemModel.getNoteToSeller());

        mRlProductPoliciesLayout.setVisibility(isPoliciesVisible(cartItemModel) ?
                View.VISIBLE : View.GONE);
        mIvFreeReturnIcon.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvFreeReturnLabel.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        mTvPreOrder.setVisibility(cartItemModel.isPreOrder() ? View.VISIBLE : View.GONE);
        mTvCashback.setVisibility(cartItemModel.isCashback() ? View.VISIBLE : View.GONE);
        mTvCashback.setText(cartItemModel.getCashback());
    }

    private boolean isPoliciesVisible(CartItemModel cartItemModel) {
        return cartItemModel.isCashback()
                || cartItemModel.isFreeReturn()
                || cartItemModel.isPreOrder();
    }

}
