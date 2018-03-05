package com.tokopedia.transaction.checkout.view.viewholder;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.CourierItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.transaction.checkout.view.adapter.InnerProductListAdapter;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class CartSellerItemViewHolder extends RecyclerView.ViewHolder {

    private static final int IMAGE_ALPHA_DISABLED = 128;
    private static final int IMAGE_ALPHA_ENABLED = 255;

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final NumberFormat CURRENCY_IDR = NumberFormat.getCurrencyInstance(LOCALE_ID);

    private static final int GRAM = 0;
    private static final int KILOGRAM = 1;

    private static final int FIRST_ELEMENT = 0;

    private Context mContext;

    private TextView mTvShopName;

    private ImageView mIvProductImage;
    private TextView mTvProductName;
    private TextView mTvProductPrice;
    private TextView mTvProductWeight;
    private TextView mTvProductTotalItem;

    private LinearLayout mLlOptionalNoteToSellerLayout;
    private TextView mTvOptionalNoteToSeller;
    private TextView mTvNoteToSellerLabel;

    private RelativeLayout mRlProductPoliciesLayout;
    private ImageView mIvFreeReturnIcon;
    private TextView mTvFreeReturnLabel;
    private TextView mTvPreOrder;
    private TextView mTvCashBack;

    private RecyclerView mRvCartItem;
    private RelativeLayout mRlOtherCartItemLayout;
    private TextView mTvOtherCartItemLabel;

    private TextView mTvChooseCourierButton;
    private ImageView mIvChevronShipmentOption;
    private TextView mTvSelectedShipment;

    private RelativeLayout mRlCostDetailLayout;
    private TextView mTvTotalItemLabel;
    private TextView mTvTotalItemPrice;
    private TextView mTvShippingFeeLabel;
    private TextView mTvShippingFee;
    private TextView mTvInsuranceFee;
    private TextView mTvPromoDiscount;
    private RelativeLayout mRlSubTotalLayout;
    private ImageView mIvDetailOptionChevron;
    private TextView mTvSubTotal;

    private LinearLayout mLlWarningContainer;
    private TextView mTvWarningText;
    private LinearLayout mLlShippingWarningContainer;
    private ImageView mIvShippingWarning;
    private TextView mTvShippingWarning;
    private TextView mTvTextProductWeight;
    private TextView mTvLabelItemCount;

    private SingleAddressShipmentAdapter.ActionListener mActionListener;

    private boolean mIsAllCartItemShown;
    private boolean mIsCostDetailShown;

    public CartSellerItemViewHolder(View itemView,
                                    SingleAddressShipmentAdapter.ActionListener actionListener) {
        super(itemView);
        mActionListener = actionListener;

        mTvShopName = itemView.findViewById(R.id.tv_shop_name);

        mIvProductImage = itemView.findViewById(R.id.iv_product_image);
        mTvProductName = itemView.findViewById(R.id.tv_product_name);
        mTvProductPrice = itemView.findViewById(R.id.tv_product_price);
        mTvProductWeight = itemView.findViewById(R.id.tv_product_weight);
        mTvProductTotalItem = itemView.findViewById(R.id.tv_product_total_item);

        mLlOptionalNoteToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);
        mTvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
        mTvNoteToSellerLabel = itemView.findViewById(R.id.tv_note_to_seller_label);

        mRlProductPoliciesLayout = itemView.findViewById(R.id.rl_product_policies_layout);
        mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
        mTvFreeReturnLabel = itemView.findViewById(R.id.tv_free_return_label);
        mTvPreOrder = itemView.findViewById(R.id.tv_pre_order);
        mTvCashBack = itemView.findViewById(R.id.tv_cashback);

        mRvCartItem = itemView.findViewById(R.id.rv_cart_item);
        mRlOtherCartItemLayout = itemView.findViewById(R.id.rl_expand_other_product);
        mTvOtherCartItemLabel = itemView.findViewById(R.id.tv_expand_other_product);

        mTvChooseCourierButton = itemView.findViewById(R.id.choose_courier_button);
        mIvChevronShipmentOption = itemView.findViewById(R.id.iv_chevron_shipment_option);
        mTvSelectedShipment = itemView.findViewById(R.id.tv_selected_shipment);

        mRlCostDetailLayout = itemView.findViewById(R.id.rl_shipment_cost);
        mTvTotalItemLabel = itemView.findViewById(R.id.tv_total_item);
        mTvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
        mTvShippingFeeLabel = itemView.findViewById(R.id.tv_shipping_fee);
        mTvShippingFee = itemView.findViewById(R.id.tv_shipping_fee_price);
        mTvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee_price);
        mTvPromoDiscount = itemView.findViewById(R.id.tv_promo_price);
        mRlSubTotalLayout = itemView.findViewById(R.id.rl_cart_sub_total);
        mIvDetailOptionChevron = itemView.findViewById(R.id.iv_detail_option_chevron);
        mTvSubTotal = itemView.findViewById(R.id.tv_sub_total_price);

        mLlWarningContainer = itemView.findViewById(R.id.ll_warning_container);
        mTvWarningText = itemView.findViewById(R.id.tv_warning);
        mLlShippingWarningContainer = itemView.findViewById(R.id.ll_shipping_warning_container);
        mIvShippingWarning = itemView.findViewById(R.id.img_shipping_warning);
        mTvShippingWarning = itemView.findViewById(R.id.tv_shipping_warning);
        mTvTextProductWeight = itemView.findViewById(R.id.tv_text_product_weight);
        mTvLabelItemCount = itemView.findViewById(R.id.tv_label_item_count);
    }

    public void bindViewHolder(CartSellerItemModel cartSellerItemModel,
                               ShipmentCostModel shipmentCostModel,
                               RecipientAddressModel recipientAddressModel) {
        mIsAllCartItemShown = false;
        mIsCostDetailShown = false;

        List<CartItemModel> cartItemModelList = new ArrayList<>(cartSellerItemModel.getCartItemModels());

        bindFirstCartItem(cartItemModelList.remove(FIRST_ELEMENT));
        bindOtherCartItems(cartItemModelList);
        bindCostDetail(cartSellerItemModel, cartSellerItemModel.getSelectedShipmentDetailData(),
                shipmentCostModel);
        bindChooseCourier(cartSellerItemModel, cartSellerItemModel.getSelectedShipmentDetailData(),
                recipientAddressModel);
    }

    private void bindFirstCartItem(CartItemModel cartItemModel) {
        ImageHandler.LoadImage(mIvProductImage, cartItemModel.getImageUrl());
        mTvProductName.setText(cartItemModel.getName());
        mTvProductPrice.setText(CURRENCY_IDR.format(cartItemModel.getPrice()));
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
        mTvCashBack.setVisibility(cartItemModel.isCashback() ? View.VISIBLE : View.GONE);
        mTvCashBack.setText(cartItemModel.getCashback());
    }

    private void bindOtherCartItems(List<CartItemModel> cartItemModelList) {
        mRlOtherCartItemLayout.setVisibility(cartItemModelList.isEmpty() ?
                View.GONE : View.VISIBLE);
        mTvOtherCartItemLabel.setText(getOtherCartItemsLabel(cartItemModelList,
                mIsAllCartItemShown));

        mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsAllCartItemShown));
        mRlOtherCartItemLayout.setOnClickListener(showAllProductListener(cartItemModelList));
        mTvOtherCartItemLabel.setOnClickListener(showAllProductListener(cartItemModelList));

        initInnerRecyclerView(cartItemModelList);
    }

    private void bindChooseCourier(CartSellerItemModel cartSellerItemModel,
                                   ShipmentDetailData shipmentDetailData,
                                   RecipientAddressModel recipientAddressModel) {
        mTvChooseCourierButton.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                cartSellerItemModel, recipientAddressModel));
        mTvSelectedShipment.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                cartSellerItemModel, recipientAddressModel));
        mIvChevronShipmentOption.setOnClickListener(selectShippingOptionListener(getAdapterPosition(),
                cartSellerItemModel, recipientAddressModel));

        boolean isCourierSelected = shipmentDetailData != null
                && shipmentDetailData.getSelectedCourier() != null;

        mTvChooseCourierButton.setVisibility(isCourierSelected ? View.GONE : View.VISIBLE);
        mTvSelectedShipment.setText(isCourierSelected ?
                shipmentDetailData.getSelectedCourier().getName() : "");
        mTvSelectedShipment.setVisibility(isCourierSelected ? View.VISIBLE : View.GONE);
        mIvChevronShipmentOption.setVisibility(isCourierSelected ? View.VISIBLE : View.GONE);
    }

    private void bindCostDetail(CartSellerItemModel cartSellerItemModel,
                                ShipmentDetailData shipmentDetailData,
                                ShipmentCostModel shipmentCostModel) {
        mRlSubTotalLayout.setVisibility(View.VISIBLE);
        mRlCostDetailLayout.setVisibility(mIsCostDetailShown ? View.VISIBLE : View.GONE);

        mTvShopName.setText(cartSellerItemModel.getShopName());

        mTvTotalItemPrice.setText(getPriceFormat(cartSellerItemModel.getTotalItemPrice()));
        mTvTotalItemLabel.setText(getTotalItemLabel(cartSellerItemModel.getTotalQuantity()));
        mTvShippingFeeLabel.setText(getTotalWeightLabel(cartSellerItemModel.getTotalWeight(),
                cartSellerItemModel.getWeightUnit()));

        if (shipmentDetailData != null) {
            CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

            cartSellerItemModel.setShippingFee(courierItemData.getDeliveryPrice()
                    + courierItemData.getAdditionalPrice());
            cartSellerItemModel.setInsuranceFee(courierItemData.getInsurancePrice());

            cartSellerItemModel.setTotalPrice(cartSellerItemModel.getTotalItemPrice()
                    + cartSellerItemModel.getShippingFee()
                    + cartSellerItemModel.getInsuranceFee());

            if (shipmentCostModel != null) {
                shipmentCostModel.setShippingFee(shipmentCostModel.getShippingFee()
                        + cartSellerItemModel.getShippingFee());
                shipmentCostModel.setInsuranceFee(shipmentCostModel.getInsuranceFee()
                        + cartSellerItemModel.getInsuranceFee());
            }
        }

        mTvShippingFee.setText(getPriceFormat(cartSellerItemModel.getShippingFee()));
        mTvInsuranceFee.setText(getPriceFormat(cartSellerItemModel.getInsuranceFee()));

        mTvSubTotal.setText(getPriceFormat(cartSellerItemModel.getTotalPrice()));
        mIvDetailOptionChevron.setOnClickListener(costDetailOptionListener());
    }

    private void bindWarnings() {
        if (getAdapterPosition() % 2 == 1) {
            // Test show shipment warning
            showGreyWarning("Terdapat kendala pengiriman pada 1 produk");
            showShipmentWarning("Produk ini tidak dapat dikirimkan dengan kurir yang dipilih");
        } else {
            // Test show general warning
            showRedWarning("Toko sedang tutup sementara, pesanan dapat di proses setelah toko buka kembali");
        }
    }

    private void initInnerRecyclerView(List<CartItemModel> cartItemModels) {
        mRvCartItem.setVisibility(View.GONE);

        mRvCartItem.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvCartItem.setLayoutManager(layoutManager);

        InnerProductListAdapter innerProductListAdapter =
                new InnerProductListAdapter(cartItemModels);
        mRvCartItem.setAdapter(innerProductListAdapter);
    }

    private boolean isPoliciesVisible(CartItemModel cartItemModel) {
        return cartItemModel.isCashback()
                || cartItemModel.isFreeReturn()
                || cartItemModel.isPreOrder();
    }

    private String getTotalItemLabel(int totalItem) {
        return String.format("Jumlah Barang (%s Item)", totalItem);
    }

    private String getTotalWeightLabel(double weight, int weightUnit) {
        String unit = weightUnit == GRAM ? "gr" : "Kg";
        return String.format("Ongkos Kirim (%s %s)", (int) weight, unit);
    }

    private String getPriceFormat(double price) {
        return price == 0 ? "-" : CURRENCY_IDR.format(price);
    }

    private String getOtherCartItemsLabel(List<CartItemModel> cartItemModels,
                                              boolean isExpandAllProduct) {
        return isExpandAllProduct ? "Tutup" :
                String.format("+%s Produk Lainnya", cartItemModels.size());
    }

    private View.OnClickListener showAllProductListener(final List<CartItemModel> cartItemModels) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowAllProduct(cartItemModels);
            }
        };
    }

    private void toggleShowAllProduct(List<CartItemModel> cartItemModels) {
        mIsAllCartItemShown = !mIsAllCartItemShown;
        mRvCartItem.setVisibility(mIsAllCartItemShown ? View.VISIBLE : View.GONE);

        mTvOtherCartItemLabel.setText(getOtherCartItemsLabel(cartItemModels,
                mIsAllCartItemShown));
    }

    private View.OnClickListener selectShippingOptionListener(final int position,
                                                              final CartSellerItemModel cartSellerItemModel,
                                                              final RecipientAddressModel recipientAddressModel) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onChooseShipment(position, cartSellerItemModel, recipientAddressModel);
            }
        };
    }

    private View.OnClickListener costDetailOptionListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleShowCostDetail();
            }
        };
    }

    private void toggleShowCostDetail() {
        mIsCostDetailShown = !mIsCostDetailShown;
        mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsCostDetailShown));

        mRlCostDetailLayout.setVisibility(mIsCostDetailShown ? View.VISIBLE : View.GONE);
    }

    private int getResourceDrawerChevron(boolean isExpanded) {
        return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
    }

    private void showRedWarning(String message) {
        mLlWarningContainer.setBackgroundColor(ContextCompat.getColor(
                mLlWarningContainer.getContext(), R.color.bg_warning_red));
        mTvWarningText.setText(message);
        mTvWarningText.setTextColor(ContextCompat.getColor(
                mLlWarningContainer.getContext(), R.color.text_warning_red));
        mLlWarningContainer.setVisibility(View.VISIBLE);
    }

    private void showGreyWarning(String message) {
        mLlWarningContainer.setBackgroundColor(ContextCompat.getColor(
                mLlWarningContainer.getContext(), R.color.bg_warning_grey));
//        imgWarning.setImageResource(R.drawable.ic_warning_grey);
        mTvWarningText.setText(message);
        mTvWarningText.setTextColor(ContextCompat.getColor(
                mLlWarningContainer.getContext(), R.color.black_54));
        mLlWarningContainer.setVisibility(View.VISIBLE);
    }

    private void hideWarning() {
        mLlShippingWarningContainer.setVisibility(View.GONE);
    }

    private void showShipmentWarning(String message) {
        mIvShippingWarning.setImageResource(R.drawable.ic_warning_red);
        mTvShippingWarning.setText(message);
        mLlShippingWarningContainer.setVisibility(View.VISIBLE);
        disableItemView();
    }

    private void hideShipmentWarning() {
        mLlShippingWarningContainer.setVisibility(View.GONE);
        enableItemView();
    }

    private void disableItemView() {
        mTvProductName.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvFreeReturnLabel.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvPreOrder.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvTextProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvLabelItemCount.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvProductTotalItem.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvNoteToSellerLabel.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        mTvCashBack.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
        setImageFilterGrayScale();
    }

    private void setImageFilterGrayScale() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        mIvProductImage.setColorFilter(cf);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
    }

    private void enableItemView() {
        mTvProductName.setTextColor(ContextCompat.getColor(mContext, R.color.black_70));
        mTvProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.orange_red));
        mTvFreeReturnLabel.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        mTvPreOrder.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        mTvTextProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
        mTvProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        mTvLabelItemCount.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
        mTvProductTotalItem.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
        mTvNoteToSellerLabel.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
        mTvOptionalNoteToSeller.setTextColor(ContextCompat.getColor(mContext, R.color.black_70));
        mTvCashBack.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_bg_cashback));
        setImageFilterNormal();
    }

    private void setImageFilterNormal() {
        mIvProductImage.setColorFilter(null);
        mIvProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
    }

}