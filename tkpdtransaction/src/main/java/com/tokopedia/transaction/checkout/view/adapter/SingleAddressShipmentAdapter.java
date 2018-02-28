package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartPromo;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class SingleAddressShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SingleAddressShipmentAdapter.class.getSimpleName();

    private static final int PRIME_ADDRESS = 2;

    private static final NumberFormat CURRENCY_IDR =
            NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    private static final int ITEM_VIEW_PROMO =
            R.layout.holder_item_cart_promo;
    private static final int ITEM_VIEW_PROMO_SUGGESTION =
            R.layout.holder_item_cart_potential_promo;
    private static final int ITEM_VIEW_FREE_SHIPPING_FEE =
            R.layout.view_item_free_shipping_fee;
    private static final int ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS =
            R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST_DETAIL =
            R.layout.view_item_shipment_cost_details;
    private static final int ITEM_SHIPPED_PRODUCT_DETAILS =
            R.layout.item_shipped_product_details;

    private static final int PROMO_POSITION = 0;
    private static final int ADDRESS_POSITION = 1;
    private static final int ALL_HEADER_SIZE = 2;
    private static final int ALL_FOOTER_SIZE = 1;

    private static final int FIRST_ELEMENT = 0;

    private Context mContext;

    private CartSingleAddressData mCartSingleAddressData;

    private SingleAddressShipmentAdapterListener mAdapterViewListener;

    public SingleAddressShipmentAdapter() {

    }

    public void updateSelectedShipment(int position, ShipmentDetailData shipmentDetailData) {
        mCartSingleAddressData.getCartSellerItemModelList().get(position - ALL_HEADER_SIZE)
                .setSelectedShipmentDetailData(shipmentDetailData);
    }

    public void setViewListener(SingleAddressShipmentAdapterListener shipmentAdapterListener) {
        this.mAdapterViewListener = shipmentAdapterListener;
    }

    public void updateData(CartSingleAddressData cartSingleAddressData) {
        mCartSingleAddressData = cartSingleAddressData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_PROMO) {
            return new CartPromoHolder(view);
        } else if (viewType == ITEM_VIEW_PROMO_SUGGESTION) {
            return new CartPromoSuggestionHolder(view);
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            return new ShippingRecipientHolder(view);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST_DETAIL) {
            return new ShipmentCostDetailHolder(view);
        } else {
            return new ShippedProductDetailsHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == ITEM_VIEW_PROMO) {
            ((CartPromoHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getCartPromo(), position);
        } else if (viewType == ITEM_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getCartPromoSuggestion(), position);
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            ((ShippingRecipientHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getRecipientAddressModel());
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST_DETAIL) {
            ((ShipmentCostDetailHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getCartPayableDetailModel());
        } else {
            ((ShippedProductDetailsHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getCartSellerItemModelList()
                            .get(position - ALL_HEADER_SIZE));
        }
    }

    @Override
    public int getItemCount() {
        return getCartItemSize() + ALL_HEADER_SIZE + ALL_FOOTER_SIZE;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == PROMO_POSITION) {
            return ITEM_VIEW_PROMO;
        } else if (position == ADDRESS_POSITION) {
            return ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS;
        } else if (position == ALL_HEADER_SIZE + getCartItemSize()) {
            return ITEM_VIEW_SHIPMENT_COST_DETAIL;
        } else {
            return ITEM_SHIPPED_PRODUCT_DETAILS;
        }
    }

    public interface SingleAddressShipmentAdapterListener {

        void onAddOrChangeAddress();

        void onChooseShipment(int position, CartSellerItemModel cartSellerItemModel);

        void onChoosePickupPoint(RecipientAddressModel addressAdapterData);

        void onClearPickupPoint(RecipientAddressModel addressAdapterData);

        void onEditPickupPoint(RecipientAddressModel addressAdapterData);

        void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

        void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

        void onCartPromoUseVoucherPromoClicked(CartPromo cartPromo, int position);

        void onCartPromoCancelVoucherPromoClicked(CartPromo cartPromo, int position);

        void onCartPromoTrackingSuccess(CartPromo cartPromo, int position);

        void onCartPromoTrackingCancelled(CartPromo cartPromo, int position);

    }

    public void setPickupPoint(Store store) {
        mCartSingleAddressData.getRecipientAddressModel().setStore(store);
    }

    public void unSetPickupPoint() {
        mCartSingleAddressData.getRecipientAddressModel().setStore(null);
    }

    private int getCartItemSize() {
        return mCartSingleAddressData.getCartSellerItemModelList().size();
    }

    private String getPriceFormat(double pricePlan) {
        return pricePlan == 0 ? "-" : CURRENCY_IDR.format(pricePlan);
    }

    private String getTotalItemFormat(int totalItem) {
        return String.format("Jumlah Barang (%s Item)", totalItem);
    }

    private String getTotalWeightFormat(double totalWeight, int weightUnit) {
        String unit = weightUnit == 0 ? "Kg" : "gr";
        return String.format("%s %s", (int) totalWeight, unit);
    }

    private String getLabelShipmentFeeWeight(double totalWeight, int weightUnit) {
        return String.format("Ongkos Kirim (%s)", getTotalWeightFormat(totalWeight, weightUnit));
    }

    private int getResourceDrawerChevron(boolean isExpanded) {
        return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
    }

    class CartPromoHolder extends RecyclerView.ViewHolder {

        private VoucherCartHachikoView voucherCartHachikoView;

        CartPromoHolder(View itemView) {
            super(itemView);

            this.voucherCartHachikoView = itemView.findViewById(R.id.voucher_cart_holder_view);
        }

        void bindViewHolder(final CartPromo data, final int position) {
            voucherCartHachikoView.setActionListener(new VoucherCartHachikoView.ActionListener() {
                @Override
                public void onClickUseVoucher() {
                    mAdapterViewListener.onCartPromoUseVoucherPromoClicked(data, position);
                }

                @Override
                public void disableVoucherDisount() {
                    mAdapterViewListener.onCartPromoCancelVoucherPromoClicked(data, position);
                }

                @Override
                public void trackingSuccessVoucher(String voucherName) {
                    mAdapterViewListener.onCartPromoTrackingSuccess(data, position);
                }

                @Override
                public void trackingCancelledVoucher() {
                    mAdapterViewListener.onCartPromoTrackingCancelled(data, position);
                }
            });

            if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
                voucherCartHachikoView.setCoupon(
                        data.getCouponTitle(), data.getCouponMessage(), data.getCouponCode()
                );
            } else if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
                voucherCartHachikoView.setVoucher(
                        data.getVoucherCode(), data.getVoucherMessage()
                );
            } else {
                voucherCartHachikoView.setPromoAndCouponLabel();
                voucherCartHachikoView.resetView();
            }
        }
    }

    class CartPromoSuggestionHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlHeaderLayout;
        private ImageView btnClose;
        private TextView tvDesc;
        private TextView tvAction;

        CartPromoSuggestionHolder(View itemView) {
            super(itemView);

            this.mRlHeaderLayout = itemView.findViewById(R.id.rl_free_shipment_fee_header);
            this.btnClose = itemView.findViewById(R.id.btn_close);
            this.tvAction = itemView.findViewById(R.id.tv_action);
            this.tvDesc = itemView.findViewById(R.id.tv_desc);
        }

        void bindViewHolder(final CartPromoSuggestion data, final int position) {
            if (data.isVisible()) {
                mRlHeaderLayout.setVisibility(View.VISIBLE);

                tvDesc.setText(data.getText());
                tvAction.setText(data.getCta());
                tvAction.setTextColor(Color.parseColor(data.getCtaColor()));

                tvAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapterViewListener.onCartPromoSuggestionActionClicked(data, position);
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapterViewListener.onCartPromoSuggestionButtonCloseClicked(data, position);
                    }
                });
            } else {
                mRlHeaderLayout.setVisibility(View.GONE);
            }
        }
    }

    class ShippingRecipientHolder extends RecyclerView.ViewHolder {

        TextView mTvAddressStatus;
        TextView mTvAddressName;
        TextView mTvRecipientName;
        TextView mTvRecipientAddress;
        TextView mTvRecipientPhone;
        TextView mTvAddOrChangeAddress;
        PickupPointLayout pickupPointLayout;

        ShippingRecipientHolder(View itemView) {
            super(itemView);

            mTvAddressStatus = itemView.findViewById(R.id.tv_address_status);
            mTvAddressName = itemView.findViewById(R.id.tv_address_name);
            mTvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
            mTvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
            mTvRecipientPhone = itemView.findViewById(R.id.tv_recipient_phone);
            mTvAddOrChangeAddress = itemView.findViewById(R.id.tv_add_or_change_address);
            pickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);
        }

        void bindViewHolder(RecipientAddressModel model) {
            mTvAddressStatus.setVisibility(model.getAddressStatus() == PRIME_ADDRESS ?
                    View.VISIBLE : View.GONE);
            mTvAddressName.setText(model.getAddressName());
            mTvRecipientName.setText(model.getRecipientName());
            mTvRecipientAddress.setText(getFullAddress(model.getAddressStreet(),
                    model.getDestinationDistrictName(), model.getAddressCityName(),
                    model.getAddressProvinceName()));
            mTvRecipientPhone.setText(model.getRecipientPhoneNumber());

            renderPickupPoint(pickupPointLayout, mCartSingleAddressData.getRecipientAddressModel());

            mTvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());
        }

        private String getFullAddress(String street, String district, String city, String province) {
            return street + ", " + district + ", " + city + ", " + province;
        }

        private void renderPickupPoint(PickupPointLayout pickupPointLayout,
                                       final RecipientAddressModel data) {

            pickupPointLayout.setListener(new PickupPointLayout.ViewListener() {
                @Override
                public void onChoosePickupPoint() {
                    mAdapterViewListener.onChoosePickupPoint(data);
                }

                @Override
                public void onClearPickupPoint(Store oldStore) {
                    mAdapterViewListener.onClearPickupPoint(data);
                }

                @Override
                public void onEditPickupPoint(Store oldStore) {
                    mAdapterViewListener.onEditPickupPoint(data);
                }
            });

            if (data.getStore() == null) {
                pickupPointLayout.unSetData(pickupPointLayout.getContext());
                pickupPointLayout.enableChooserButton(pickupPointLayout.getContext());
            } else {
                pickupPointLayout.setData(pickupPointLayout.getContext(), data.getStore());
            }

        }

        private View.OnClickListener addOrChangeAddressListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterViewListener.onAddOrChangeAddress();
                }
            };
        }

    }

    class ShipmentCostDetailHolder extends RecyclerView.ViewHolder {

        RelativeLayout mRlDetailShipmentFee;
        TextView mTvTotalItemLabel;
        TextView mTvTotalItem;
        TextView mTvShippingFeeLabel;
        TextView mTvShippingFee;
        TextView mTvInsuranceFee;
        TextView mTvPromo;
        TextView mTvPayable;
        TextView mTvPromoFreeShipping;
        TextView mTvPromoTextViewRemove;

        ShipmentCostDetailHolder(View itemView) {
            super(itemView);

            mRlDetailShipmentFee = itemView.findViewById(R.id.rl_detail_shipment_fee);
            mTvTotalItemLabel = itemView.findViewById(R.id.tv_total_item_label);
            mTvTotalItem = itemView.findViewById(R.id.tv_total_item_price);
            mTvShippingFeeLabel = itemView.findViewById(R.id.tv_shipping_fee_label);
            mTvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
            mTvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee);
            mTvPromo = itemView.findViewById(R.id.tv_promo);
            mTvPayable = itemView.findViewById(R.id.tv_payable);
            mTvPromoFreeShipping = itemView.findViewById(R.id.tv_promo_free_shipping);
            mTvPromoTextViewRemove = itemView.findViewById(R.id.tv_promo_text_view_remove);
        }

        void bindViewHolder(CartPayableDetailModel model) {
            mRlDetailShipmentFee.setVisibility(View.VISIBLE);

            mTvTotalItemLabel.setText(getTotalItemFormat(model.getTotalItem()));
            mTvTotalItem.setText(getPriceFormat(model.getTotalPrice()));
            mTvShippingFeeLabel.setText(getLabelShipmentFeeWeight(model.getTotalWeight(), 1));
            mTvShippingFee.setText(getPriceFormat(model.getShippingFee()));
            mTvInsuranceFee.setText(getPriceFormat(model.getInsuranceFee()));
            mTvPromo.setText(getPriceFormat(model.getPromoPrice()));
            mTvPayable.setText(getPriceFormat(model.getTotalPrice()));

            mTvPromoFreeShipping.setVisibility(View.VISIBLE);
            mTvPromoTextViewRemove.setOnClickListener(togglePromoTextListener);
        }

        private void togglePromoText() {
            mTvPromoFreeShipping.setVisibility(View.GONE);
        }

        View.OnClickListener togglePromoTextListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePromoText();
            }
        };

    }

    class ShippedProductDetailsHolder extends RecyclerView.ViewHolder {

        private static final int IMAGE_ALPHA_DISABLED = 128;
        private static final int IMAGE_ALPHA_ENABLED = 255;

        TextView mTvSenderName;
        ImageView mIvProductImage;
        TextView mTvProductName;
        TextView mTvProductPrice;
        TextView mTvProductWeight;
        TextView mTvTotalProductItem;
        TextView mTvOptionalNote;

        RelativeLayout mRlProductPoliciesContainer;
        ImageView mIvFreeReturnIcon;
        TextView mTvFreeReturnText;
        TextView mTvPoSign;
        TextView mTvCashBack;

        RecyclerView mRvProductList;

        RelativeLayout mRlExpandOtherProductContainer;
        TextView mTvExpandOtherProduct;

        TextView mChooseCourierButton;
        ImageView mIvChevronShipmentOption;
        TextView mTvSelectedShipment;

        RelativeLayout mRlDetailShipmentFeeContainer;
        TextView mTvTotalItem;
        TextView mTvTotalItemPrice;
        TextView mTvShippingFee;
        TextView mTvShippingFeePrice;
        TextView mTvInsuranceFeePrice;
        TextView mTvPromoPrice;

        RelativeLayout mRlCartSubTotal;
        ImageView mIvDetailOptionChevron;
        TextView mTvSubTotalPrice;

        LinearLayout llWarningContainer;
        TextView tvWarning;

        LinearLayout llShippingWarningContainer;
        ImageView imgShippingWarning;
        TextView tvShippingWarning;
        TextView tvTextProductWeight;
        TextView tvLabelItemCount;
        TextView tvLabelNoteToSeller;
        LinearLayout mLlNoteToSellerLayout;

        private boolean mIsExpandAllProduct;
        private boolean mIsExpandCostDetail;

        ShippedProductDetailsHolder(View itemView) {
            super(itemView);

            mTvSenderName = itemView.findViewById(R.id.tv_sender_name);
            mIvProductImage = itemView.findViewById(R.id.iv_product_image_container);
            mTvProductName = itemView.findViewById(R.id.tv_shipping_product_name);
            mTvProductPrice = itemView.findViewById(R.id.tv_shipped_product_price);
            mTvProductWeight = itemView.findViewById(R.id.tv_product_weight);
            mTvTotalProductItem = itemView.findViewById(R.id.tv_total_product_item);
            mTvOptionalNote = itemView.findViewById(R.id.tv_optional_note_to_seller);

            mRlProductPoliciesContainer = itemView.findViewById(R.id.rl_product_policies_layout);
            mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
            mTvFreeReturnText = itemView.findViewById(R.id.tv_free_return_text);
            mTvPoSign = itemView.findViewById(R.id.tv_po_sign);
            mTvCashBack = itemView.findViewById(R.id.tv_cashback_text);

            mRvProductList = itemView.findViewById(R.id.rv_product_list);

            mRlExpandOtherProductContainer = itemView.findViewById(R.id.rl_expand_other_product);
            mTvExpandOtherProduct = itemView.findViewById(R.id.tv_expand_other_product);

            mChooseCourierButton = itemView.findViewById(R.id.choose_courier_button);
            mIvChevronShipmentOption = itemView.findViewById(R.id.iv_chevron_shipment_option);
            mTvSelectedShipment = itemView.findViewById(R.id.tv_selected_shipment);

            mRlDetailShipmentFeeContainer = itemView.findViewById(R.id.rl_detail_shipment_fee);
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
            mLlNoteToSellerLayout = itemView.findViewById(R.id.ll_note_to_seller);
        }

        void bindViewHolder(CartSellerItemModel model) {
            // Initialize variables
            List<CartItemModel> cartItemModels = new ArrayList<>(model.getCartItemModels());

            CartItemModel firstItem = cartItemModels.remove(FIRST_ELEMENT);

            mIsExpandAllProduct = false;
            mIsExpandCostDetail = false;

            // Assign variables
            mTvSenderName.setText(model.getShopName());
            mTvSelectedShipment.setText(getCourierName(model.getCourierItemData()));
            mTvSubTotalPrice.setText(getPriceFormat(model.getTotalPrice()));

            mRlDetailShipmentFeeContainer.setVisibility(mIsExpandCostDetail ? View.VISIBLE : View.GONE);

            String insuranceFee = "-";
            String shippingFee = "-";

            if (model.getCourierItemData() != null) {
                insuranceFee = String.valueOf(model.getCourierItemData().getInsurancePrice());
                shippingFee = String.valueOf(model.getCourierItemData().getDeliveryPrice());
            }

            mTvShippingFeePrice.setText(shippingFee);
            mTvInsuranceFeePrice.setText(insuranceFee);

            mTvTotalItem.setText(getTotalItemFormat(model.getTotalQuantity()));
            mTvShippingFee.setText(getLabelShipmentFeeWeight(model.getTotalWeight(), model.getWeightUnit()));

            mTvTotalItemPrice.setText(getPriceFormat(model.getTotalPrice()));

            ImageHandler.LoadImage(mIvProductImage, firstItem.getImageUrl());
            mTvProductName.setText(firstItem.getName());
            mTvProductPrice.setText(CURRENCY_IDR.format(firstItem.getPrice()));
            mTvProductWeight.setText(firstItem.getWeightFmt());
            mTvTotalProductItem.setText(String.valueOf(firstItem.getQuantity()));

            mRlExpandOtherProductContainer.setVisibility(cartItemModels.isEmpty() ?
                    View.GONE : View.VISIBLE);
            mTvExpandOtherProduct.setText(getExpandOtherProductLabel(cartItemModels,
                    mIsExpandAllProduct));

            mRlProductPoliciesContainer.setVisibility(getPoliciesVisibility(
                    firstItem.isCashback(),
                    firstItem.isFreeReturn(),
                    firstItem.isPreOrder()));
            mIvFreeReturnIcon.setVisibility(firstItem.isFreeReturn() ? View.VISIBLE : View.GONE);
            mTvFreeReturnText.setVisibility(firstItem.isFreeReturn() ? View.VISIBLE : View.GONE);
            mTvPoSign.setVisibility(firstItem.isPreOrder() ? View.VISIBLE : View.GONE);
            mTvCashBack.setVisibility(firstItem.isCashback() ? View.VISIBLE : View.GONE);
            mTvCashBack.setText(firstItem.getCashback());

            mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsExpandCostDetail));

            // Init nested recycler view
            initInnerRecyclerView(cartItemModels);

            // Set listeners
            mRlExpandOtherProductContainer.setOnClickListener(showAllProductListener(cartItemModels));
            mTvExpandOtherProduct.setOnClickListener(showAllProductListener(cartItemModels));

            mChooseCourierButton.setOnClickListener(selectShippingOptionListener(getAdapterPosition(), model));
            mTvSelectedShipment.setOnClickListener(selectShippingOptionListener(getAdapterPosition(), model));
            mIvChevronShipmentOption.setOnClickListener(selectShippingOptionListener(getAdapterPosition(), model));

            mIvDetailOptionChevron.setOnClickListener(costDetailOptionListener());

//            if (getAdapterPosition() % 2 == 1) {
//                // Test show shipment warning
//                showGreyWarning("Terdapat kendala pengiriman pada 1 produk");
//                showShipmentWarning("Produk ini tidak dapat dikirimkan dengan kurir yang dipilih");
//            } else {
//                // Test show general warning
//                showRedWarning("Toko sedang tutup sementara, pesanan dapat di proses setelah toko buka kembali");
//            }

            boolean isEmptyNotes = TextUtils.isEmpty(firstItem.getNoteToSeller());

            mLlNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
            mTvOptionalNote.setText(firstItem.getNoteToSeller());

            if (model.getSelectedShipmentDetailData() != null &&
                    model.getSelectedShipmentDetailData().getSelectedCourier() != null) {
                mChooseCourierButton.setVisibility(View.GONE);
                mTvSelectedShipment.setText(
                        model.getSelectedShipmentDetailData().getSelectedCourier().getName());
                mTvSelectedShipment.setVisibility(View.VISIBLE);
                mIvChevronShipmentOption.setVisibility(View.VISIBLE);
            } else {
                mTvSelectedShipment.setVisibility(View.GONE);
                mIvChevronShipmentOption.setVisibility(View.GONE);
                mChooseCourierButton.setVisibility(View.VISIBLE);
            }
        }

        private void initInnerRecyclerView(List<CartItemModel> cartItemModels) {
            mRvProductList.setVisibility(View.GONE);

            mRvProductList.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRvProductList.setLayoutManager(layoutManager);

            InnerProductListAdapter innerProductListAdapter =
                    new InnerProductListAdapter(cartItemModels);
            mRvProductList.setAdapter(innerProductListAdapter);
        }

        private String getCourierName(CourierItemData courierItemData) {
            if (courierItemData == null) {
                return "";
            }
            return courierItemData.getName();
        }

        private String getExpandOtherProductLabel(List<CartItemModel> cartItemModels,
                                                  boolean isExpandAllProduct) {

            return isExpandAllProduct ? "Tutup" :
                    String.format("+%s Produk Lainnya", cartItemModels.size());
        }

        private int getPoliciesVisibility(boolean isCashback,
                                          boolean isFreeReturn,
                                          boolean isPreOrder) {

            return !isCashback && !isFreeReturn && !isPreOrder ? View.GONE : View.VISIBLE;
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
            mIsExpandAllProduct = !mIsExpandAllProduct;
            mRvProductList.setVisibility(mIsExpandAllProduct ? View.VISIBLE : View.GONE);

            mTvExpandOtherProduct.setText(getExpandOtherProductLabel(cartItemModels,
                    mIsExpandAllProduct));
        }

        private View.OnClickListener selectShippingOptionListener(final int position,
                                                                  final CartSellerItemModel cartSellerItemModel) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterViewListener.onChooseShipment(position, cartSellerItemModel);
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
            mIsExpandCostDetail = !mIsExpandCostDetail;
            mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsExpandCostDetail));

            mRlDetailShipmentFeeContainer.setVisibility(mIsExpandCostDetail ? View.VISIBLE : View.GONE);
        }

        private int getResourceDrawerChevron(boolean isExpanded) {
            return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
        }

        private void showRedWarning(String message) {
            llWarningContainer.setBackgroundColor(ContextCompat.getColor(
                    llWarningContainer.getContext(), R.color.bg_warning_red));
            tvWarning.setText(message);
            tvWarning.setTextColor(ContextCompat.getColor(
                    llWarningContainer.getContext(), R.color.text_warning_red));
            llWarningContainer.setVisibility(View.VISIBLE);
        }

        //        private void showGreyWarning(String message) {
//            llWarningContainer.setBackgroundColor(ContextCompat.getColor(
//                    llWarningContainer.getContext(), R.color.bg_warning_grey));
//            imgWarning.setImageResource(R.drawable.ic_warning_grey);
//            tvWarning.setText(message);
//            tvWarning.setTextColor(ContextCompat.getColor(
//                    llWarningContainer.getContext(), R.color.black_54));
//            llWarningContainer.setVisibility(View.VISIBLE);
//        }
//
        private void hideWarning() {
            llWarningContainer.setVisibility(View.GONE);
        }
//
//        private void showShipmentWarning(String message) {
//            imgShippingWarning.setImageResource(R.drawable.ic_warning_red);
//            tvShippingWarning.setText(message);
//            llShippingWarningContainer.setVisibility(View.VISIBLE);
//            disableItemView();
//        }

        private void hideShipmentWarning() {
            llShippingWarningContainer.setVisibility(View.GONE);
            enableItemView();
        }

        private void disableItemView() {
            mTvProductName.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvFreeReturnText.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvPoSign.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            tvTextProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            tvLabelItemCount.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvTotalProductItem.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            tvLabelNoteToSeller.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvOptionalNote.setTextColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            mTvCashBack.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
            setImageFilterGrayscale();
        }

        private void setImageFilterGrayscale() {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
            mIvProductImage.setColorFilter(cf);
            mIvProductImage.setImageAlpha(IMAGE_ALPHA_DISABLED);
        }

        private void enableItemView() {
            mTvProductName.setTextColor(ContextCompat.getColor(mContext, R.color.black_70));
            mTvProductPrice.setTextColor(ContextCompat.getColor(mContext, R.color.orange_red));
            mTvFreeReturnText.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
            mTvPoSign.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
            tvTextProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
            mTvProductWeight.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
            tvLabelItemCount.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
            mTvTotalProductItem.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_secondary_54));
            tvLabelNoteToSeller.setTextColor(ContextCompat.getColor(mContext, R.color.black_38));
            mTvOptionalNote.setTextColor(ContextCompat.getColor(mContext, R.color.black_70));
            mTvCashBack.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_bg_cashback));
            setImageFilterNormal();
        }

        private void setImageFilterNormal() {
            mIvProductImage.setColorFilter(null);
            mIvProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
        }

    }

}
