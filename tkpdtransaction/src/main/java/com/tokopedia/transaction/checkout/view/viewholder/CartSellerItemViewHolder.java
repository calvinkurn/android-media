package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartSellerItemViewHolder extends RecyclerView.ViewHolder {

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private TextView mTvShopName;
    private ImageView mIvProductImage;
    private TextView mTvProductName;
    private TextView mTvProductPrice;
    private TextView mTvProductWeight;
    private TextView mTvTotalProductItem;
    private TextView mTvOptionalNote;

    private RelativeLayout mRlProductPoliciesContainer;
    private ImageView mIvFreeReturnIcon;
    private TextView mTvFreeReturnText;
    private TextView mTvPoSign;
    private TextView mTvCashBack;

    private RecyclerView mRvProductList;

    private RelativeLayout mRlExpandOtherProductContainer;
    private TextView mTvExpandOtherProduct;

    private TextView mChooseCourierButton;
    private ImageView mIvChevronShipmentOption;
    private TextView mTvSelectedShipment;

    private RelativeLayout mRlDetailShipmentFeeContainer;
    private TextView mTvTotalItem;
    private TextView mTvTotalItemPrice;
    private TextView mTvShippingFee;
    private TextView mTvShippingFeePrice;
    private TextView mTvInsuranceFeePrice;
    private TextView mTvPromoPrice;

    private RelativeLayout mRlCartSubTotal;
    private ImageView mIvDetailOptionChevron;
    private TextView mTvSubTotalPrice;

    private LinearLayout llWarningContainer;
    private TextView tvWarning;

    private LinearLayout llShippingWarningContainer;
    private ImageView imgShippingWarning;
    private TextView tvShippingWarning;
    private TextView tvTextProductWeight;
    private TextView tvLabelItemCount;
    private TextView tvLabelNoteToSeller;
    private LinearLayout mLlNoteToSellerLayout;

    private boolean mIsExpandAllProduct;
    private boolean mIsExpandCostDetail;

    public CartSellerItemViewHolder(View itemView) {
        super(itemView);

        mTvShopName = itemView.findViewById(R.id.tv_sender_name);
        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvProductWeight = itemView.findViewById(R.id.tv_product_weight);
        mTvTotalProductItem = itemView.findViewById(R.id.tv_product_total_item);
        mTvOptionalNote = itemView.findViewById(R.id.tv_optional_note_to_seller);

        mRlProductPoliciesContainer = itemView.findViewById(R.id.rl_product_policies_layout);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        mTvFreeReturnText = itemView.findViewById(R.id.tv_free_return_label);
        mTvPoSign = itemView.findViewById(R.id.tv_pre_order);
        mTvCashBack = itemView.findViewById(R.id.tv_cashback);

        mRvProductList = itemView.findViewById(R.id.rv_product_list);

        mRlExpandOtherProductContainer = itemView.findViewById(R.id.rl_expand_other_product);
        mTvExpandOtherProduct = itemView.findViewById(R.id.tv_expand_other_product);

        mChooseCourierButton = itemView.findViewById(R.id.choose_courier_button);
        mIvChevronShipmentOption = itemView.findViewById(R.id.iv_chevron_shipment_option);
        mTvSelectedShipment = itemView.findViewById(R.id.tv_selected_shipment);

        mRlDetailShipmentFeeContainer = itemView.findViewById(R.id.rl_shipment_cost);
        mTvTotalItem = itemView.findViewById(R.id.tv_total_item);
        mTvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
        mTvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
        mTvShippingFeePrice = itemView.findViewById(R.id.tv_shipping_fee_price);
        mTvInsuranceFeePrice = itemView.findViewById(R.id.tv_insurance_fee_price);
        mTvPromoPrice = itemView.findViewById(R.id.tv_promo_price);

        mRlCartSubTotal = itemView.findViewById(R.id.rl_cart_sub_total);
        mIvDetailOptionChevron = itemView.findViewById(R.id.iv_detail_option_chevron);
        mTvSubTotalPrice = itemView.findViewById(R.id.tv_sub_total_price);

        llWarningContainer = itemView.findViewById(R.id.ll_warning_container);
        tvWarning = itemView.findViewById(R.id.tv_warning);

        llShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        imgShippingWarning = itemView.findViewById(R.id.img_shipping_warning);
        tvShippingWarning = itemView.findViewById(R.id.tv_shipping_warning);
        tvTextProductWeight = itemView.findViewById(R.id.tv_text_product_weight);
        tvLabelItemCount = itemView.findViewById(R.id.tv_label_item_count);
        tvLabelNoteToSeller = itemView.findViewById(R.id.tv_label_note_to_seller);
        mLlNoteToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);
    }
}
