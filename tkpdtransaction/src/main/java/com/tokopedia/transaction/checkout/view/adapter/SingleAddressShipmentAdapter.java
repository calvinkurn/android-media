package com.tokopedia.transaction.checkout.view.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentFeeBannerModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class SingleAddressShipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SingleAddressShipmentAdapter.class.getSimpleName();

    private static final NumberFormat CURRENCY_IDR =
            NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    private static final int ITEM_VIEW_FREE_SHIPPING_FEE =
            R.layout.view_item_free_shipping_fee;
    private static final int ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS =
            R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_SHIPMENT_COST_DETAIL =
            R.layout.view_item_shipment_cost_details;
    private static final int ITEM_SHIPPED_PRODUCT_DETAILS =
            R.layout.item_shipped_product_details;

    private static final int TOP_POSITION = 0;
    private static final int ADDRESS_POSITION = 1;
    private static final int ALL_HEADER_SIZE = 2;
    private static final int ALL_FOOTER_SIZE = 1;

    private static final int FIRST_ELEMENT = 0;

    private Context mContext;
    private CartSingleAddressData mCartSingleAddressData;
    private SingleAddressShipmentAdapterListener mAdapterViewListener;

    public SingleAddressShipmentAdapter() {

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

        if (viewType == ITEM_VIEW_FREE_SHIPPING_FEE) {
            return new FreeShippingFeeViewHolder(view);
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            return new ShippingRecipientViewHolder(view);
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST_DETAIL) {
            return new ShipmentCostDetailViewHolder(view);
        } else {
            return new ShippedProductDetailsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == ITEM_VIEW_FREE_SHIPPING_FEE) {
            ((FreeShippingFeeViewHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getShipmentFeeBannerModel());
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            ((ShippingRecipientViewHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getShipmentRecipientModel());
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST_DETAIL) {
            ((ShipmentCostDetailViewHolder) viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getCartPayableDetailModel());
        } else {
            ((ShippedProductDetailsViewHolder) viewHolder)
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
        if (position == TOP_POSITION) {
            return ITEM_VIEW_FREE_SHIPPING_FEE;
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

        void onChooseShipment();

        void onChoosePickupPoint(ShipmentRecipientModel addressAdapterData);

        void onClearPickupPoint(ShipmentRecipientModel addressAdapterData);

        void onEditPickupPoint(ShipmentRecipientModel addressAdapterData);

        void onTotalPaymentUpdate(String priceFormat);

        void onRecyclerViewReachBottom();

    }

    public void setPickupPoint(Store store) {
        mCartSingleAddressData.getShipmentRecipientModel().setStore(store);
    }

    public void unSetPickupPoint() {
        mCartSingleAddressData.getShipmentRecipientModel().setStore(null);
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

    class FreeShippingFeeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_free_shipment_fee_header)
        RelativeLayout mRlFreeShipmentFeeHeader;
        @BindView(R2.id.tv_shpping_fee)
        TextView mTvShippingFee;

        FreeShippingFeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(ShipmentFeeBannerModel model) {
            mRlFreeShipmentFeeHeader.setVisibility(model.isVisible() ? View.VISIBLE : View.GONE);
            mTvShippingFee.setText(model.getShipmentFeeDiscount());

            mRlFreeShipmentFeeHeader.setOnClickListener(feeShipmentOnClickListener());
        }

        private View.OnClickListener feeShipmentOnClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRlFreeShipmentFeeHeader.setVisibility(View.VISIBLE);
                }
            };
        }

    }

    class ShippingRecipientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_text_address_description)
        TextView mTvAddressDescription;
        @BindView(R2.id.tv_recipient_name)
        TextView mTvRecipientName;
        @BindView(R2.id.tv_recipient_address)
        TextView mTvRecipientAddress;
        @BindView(R2.id.tv_recipient_phone)
        TextView mTvRecipientPhone;
        @BindView(R2.id.tv_add_or_change_address)
        TextView mTvAddOrChangeAddress;
        @BindView(R2.id.pickup_point_layout)
        PickupPointLayout pickupPointLayout;

        ShippingRecipientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(ShipmentRecipientModel model) {
            mTvAddressDescription.setText(model.getRecipientAddressDescription());
            mTvRecipientName.setText(model.getRecipientName());
            mTvRecipientAddress.setText(model.getRecipientAddress());
            mTvRecipientPhone.setText(model.getRecipientPhoneNumber());

            renderPickupPoint(pickupPointLayout, mCartSingleAddressData.getShipmentRecipientModel());
            mTvAddOrChangeAddress.setOnClickListener(
                    addOrChangeAddressListener(mCartSingleAddressData.getShipmentRecipientModel()));
        }

        private void renderPickupPoint(PickupPointLayout pickupPointLayout,
                                       final ShipmentRecipientModel data) {

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

        private View.OnClickListener addOrChangeAddressListener(final ShipmentRecipientModel model) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterViewListener.onAddOrChangeAddress();
                }
            };
        }

        private void fragmentTransaction() {
            FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
            Fragment fragment = ShipmentAddressListFragment.newInstance();

            String backStateName = fragment.getClass().getName();

            boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            if (!isFragmentPopped) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(backStateName)
                        .commit();
            }
        }

    }

    class ShipmentCostDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_detail_shipment_fee_view_layout)
        RelativeLayout mRlDetailFee;
        @BindView(R2.id.tv_total_item)
        TextView mTvTotalItem;
        @BindView(R2.id.tv_total_item_price)
        TextView mTvTotalItemPrice;
        @BindView(R2.id.tv_shipping_fee)
        TextView mTvShippingFee;
        @BindView(R2.id.tv_shipping_fee_price)
        TextView mTvShippingFeePrice;
        @BindView(R2.id.tv_insurance_fee_price)
        TextView mTvInsuranceFeePrice;
        @BindView(R2.id.tv_promo_price)
        TextView mTvPromoPrice;
        @BindView(R2.id.tv_payable_price)
        TextView mTvPayablePrice;
        @BindView(R2.id.tv_promo_free_shipping)
        TextView mTvPromoFreeShipping;

        private boolean mIsExpanded;

        ShipmentCostDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(CartPayableDetailModel model) {
            mIsExpanded = false;

            mTvTotalItem.setText(getTotalItemFormat(model.getTotalItem()));
            mTvTotalItemPrice.setText(getPriceFormat(model.getTotalPrice()));
            mTvShippingFee.setText(getLabelShipmentFeeWeight(model.getTotalWeight(), 0));
            mTvShippingFeePrice.setText(getPriceFormat(model.getShippingFee()));
            mTvInsuranceFeePrice.setText(getPriceFormat(model.getInsuranceFee()));
            mTvPromoPrice.setText(getPriceFormat(model.getPromoPrice()));
            mTvPayablePrice.setText(getPriceFormat(model.getTotalPrice()));
        }

        private void toggleDetail() {
            mIsExpanded = !mIsExpanded;
            mRlDetailFee.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
        }

        View.OnClickListener expandDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDetail();
            }
        };

    }

    class ShippedProductDetailsViewHolder extends RecyclerView.ViewHolder {

        private static final int IMAGE_ALPHA_DISABLED = 128;
        private static final int IMAGE_ALPHA_ENABLED = 255;

        @BindView(R2.id.tv_sender_name)
        TextView mTvSenderName;
        @BindView(R2.id.iv_product_image_container)
        ImageView mIvProductImage;
        @BindView(R2.id.tv_shipping_product_name)
        TextView mTvProductName;
        @BindView(R2.id.tv_shipped_product_price)
        TextView mTvProductPrice;
        @BindView(R2.id.tv_product_weight)
        TextView mTvProductWeight;
        @BindView(R2.id.tv_total_product_item)
        TextView mTvTotalProductItem;
        @BindView(R2.id.tv_optional_note_to_seller)
        TextView mTvOptionalNote;

        @BindView(R2.id.rl_product_policies_layout)
        RelativeLayout mRlProductPoliciesContainer;
        @BindView(R2.id.iv_free_return_icon)
        ImageView mIvFreeReturnIcon;
        @BindView(R2.id.tv_free_return_text)
        TextView mTvFreeReturnText;
        @BindView(R2.id.tv_po_sign)
        TextView mTvPoSign;
        @BindView(R2.id.tv_cashback_text)
        TextView mTvCashback;

        @BindView(R2.id.rv_product_list)
        RecyclerView mRvProductList;

        @BindView(R2.id.rl_expand_other_product)
        RelativeLayout mRlExpandOtherProductContainer;
        @BindView(R2.id.tv_expand_other_product)
        TextView mTvExpandOtherProduct;

        @BindView(R2.id.choose_courier_button)
        TextView mChooseCourierButton;
        @BindView(R2.id.iv_chevron_shipment_option)
        ImageView mIvChevronShipmentOption;
        @BindView(R2.id.tv_selected_shipment)
        TextView mTvSelectedShipment;

        @BindView(R2.id.rl_detail_shipment_fee)
        RelativeLayout mRlDetailShipmentFeeContainer;
        @BindView(R2.id.tv_total_item)
        TextView mTvTotalItem;
        @BindView(R2.id.tv_total_item_price)
        TextView mTvTotalItemPrice;
        @BindView(R2.id.tv_shipping_fee)
        TextView mTvShippingFee;
        @BindView(R2.id.tv_shipping_fee_price)
        TextView mTvShippingFeePrice;
        @BindView(R2.id.tv_insurance_fee_price)
        TextView mTvInsuranceFeePrice;
        @BindView(R2.id.tv_promo_price)
        TextView mTvPromoPrice;

        @BindView(R2.id.rl_cart_sub_total)
        RelativeLayout mRlCartSubTotal;
        @BindView(R2.id.iv_detail_option_chevron)
        ImageView mIvDetailOptionChevron;
        @BindView(R2.id.tv_sub_total_price)
        TextView mTvSubTotalPrice;

        @BindView(R2.id.ll_warning_container)
        LinearLayout llWarningContainer;
        @BindView(R2.id.tv_warning)
        TextView tvWarning;

        @BindView(R2.id.ll_shipping_warning_container)
        LinearLayout llShippingWarningContainer;
        @BindView(R2.id.img_shipping_warning)
        ImageView imgShippingWarning;
        @BindView(R2.id.tv_shipping_warning)
        TextView tvShippingWarning;
        @BindView(R2.id.tv_text_product_weight)
        TextView tvTextProductWeight;
        @BindView(R2.id.tv_label_item_count)
        TextView tvLabelItemCount;
        @BindView(R2.id.tv_label_note_to_seller)
        TextView tvLabelNoteToSeller;
        @BindView(R2.id.ll_note_to_seller)
        LinearLayout mLlNoteToSellerLayout;

        private boolean mIsExpandAllProduct;
        private boolean mIsExpandCostDetail;

        ShippedProductDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

            String insuranceFee ="-";
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
            mTvProductWeight.setText(getTotalWeightFormat(firstItem.getWeight(),
                    firstItem.getWeightUnit()));
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
            mTvCashback.setVisibility(firstItem.isCashback() ? View.VISIBLE : View.GONE);
            mTvCashback.setText(firstItem.getCashback());

            mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsExpandCostDetail));

            // Init nested recycler view
            initInnerRecyclerView(cartItemModels);

            // Set listeners
            mRlExpandOtherProductContainer.setOnClickListener(showAllProductListener(cartItemModels));
            mTvExpandOtherProduct.setOnClickListener(showAllProductListener(cartItemModels));

            mChooseCourierButton.setOnClickListener(selectShippingOptionListener());
            mIvChevronShipmentOption.setOnClickListener(selectShippingOptionListener());

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

        private View.OnClickListener selectShippingOptionListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterViewListener.onChooseShipment();
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
            mTvCashback.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey_nonactive_text));
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
            mTvCashback.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_bg_cashback));
            setImageFilterNormal();
        }

        private void setImageFilterNormal() {
            mIvProductImage.setColorFilter(null);
            mIvProductImage.setImageAlpha(IMAGE_ALPHA_ENABLED);
        }

    }

}
