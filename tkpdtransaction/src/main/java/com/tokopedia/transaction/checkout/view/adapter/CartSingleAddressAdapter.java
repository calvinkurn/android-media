package com.tokopedia.transaction.checkout.view.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.DropshipperShippingOptionModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentFeeBannerModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSingleAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CartSingleAddressAdapter.class.getSimpleName();

    private static final int ITEM_VIEW_FREE_SHIPPING_FEE =
            R.layout.view_item_free_shipping_fee;
    private static final int ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS =
            R.layout.view_item_shipment_recipient_address;
    private static final int ITEM_VIEW_DROPSHIPPER_OPTION =
            R.layout.view_item_dropshipper_option;
    private static final int ITEM_VIEW_SHIPMENT_COST_DETAIL =
            R.layout.view_item_shipment_cost_details;
    private static final int ITEM_SHIPPED_PRODUCT_DETAILS =
            R.layout.item_shipped_product_details;

    private static final int TOP_POSITION = 0;
    private static final int ADDRESS_POSITION = 1;
    private static final int DROPSHIP_OPT_POSITION = 2;
    private static final int ALL_HEADER_SIZE = 3;
    private static final int ALL_FOOTER_SIZE = 1;

    private static final int FIRST_ELEMENT = 0;

    private Context mContext;
    private CartSingleAddressData mCartSingleAddressData;
    private SingleAddressShipmentAdapterListener viewListener;

    public CartSingleAddressAdapter() {
        Log.d(TAG, "Create instance");
    }

    public void setViewListener(SingleAddressShipmentAdapterListener viewListener) {
        this.viewListener = viewListener;
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
        } else if (viewType == ITEM_VIEW_DROPSHIPPER_OPTION) {
            return new DropShipperOptionViewHolder(view);
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
            ((FreeShippingFeeViewHolder)viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getShipmentFeeBannerModel());
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            ((ShippingRecipientViewHolder)viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getShipmentRecipientModel());
        } else if (viewType == ITEM_VIEW_DROPSHIPPER_OPTION) {
            ((DropShipperOptionViewHolder)viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getDropshipperShippingOptionModel());
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST_DETAIL) {
            ((ShipmentCostDetailViewHolder)viewHolder)
                    .bindViewHolder(mCartSingleAddressData.getCartPayableDetailModel());
        } else {
            ((ShippedProductDetailsViewHolder)viewHolder)
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
        } else if (position == DROPSHIP_OPT_POSITION) {
            return ITEM_VIEW_DROPSHIPPER_OPTION;
        } else if (position == ALL_HEADER_SIZE + getCartItemSize()) {
            return ITEM_VIEW_SHIPMENT_COST_DETAIL;
        } else {
            return ITEM_SHIPPED_PRODUCT_DETAILS;
        }
    }

    public interface SingleAddressShipmentAdapterListener {

        void onAddOrChangeAddress(ShipmentRecipientModel shipmentRecipientModel);

        void onChooseShipment();

        void onChoosePickupPoint(ShipmentRecipientModel addressAdapterData);

        void onClearPickupPoint(ShipmentRecipientModel addressAdapterData);

        void onEditPickupPoint(ShipmentRecipientModel addressAdapterData);

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

    class FreeShippingFeeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_free_shipment_fee_header) RelativeLayout mRlFreeShipmentFeeHeader;
        @BindView(R2.id.tv_shpping_fee) TextView mTvShippingFee;

        FreeShippingFeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(ShipmentFeeBannerModel model) {
            mRlFreeShipmentFeeHeader.setVisibility(getVisibility(model.isVisible()));
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

        private int getVisibility(boolean isVisible) {
            return isVisible ? View.VISIBLE : View.GONE;
        }

    }

    class ShippingRecipientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_text_address_description) TextView mTvAddressDescription;
        @BindView(R2.id.tv_recipient_name) TextView mTvRecipientName;
        @BindView(R2.id.tv_recipient_address) TextView mTvRecipientAddress;
        @BindView(R2.id.tv_recipient_phone) TextView mTvRecipientPhone;
        @BindView(R2.id.tv_add_or_change_address) TextView mTvAddOrChangeAddress;
        @BindView(R2.id.pickup_point_layout) PickupPointLayout pickupPointLayout;

        ShippingRecipientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(ShipmentRecipientModel model) {
            mTvAddressDescription.setText(model.getAddressIdentifier());
            mTvRecipientName.setText(model.getRecipientName());
            mTvRecipientAddress.setText(model.getRecipientAddress());
            mTvRecipientPhone.setText(model.getRecipientPhone());

            renderPickupPoint(pickupPointLayout, mCartSingleAddressData.getShipmentRecipientModel());
            mTvAddOrChangeAddress.setOnClickListener(
                    addOrChangeAddressListener(mCartSingleAddressData.getShipmentRecipientModel()));
        }

        private void renderPickupPoint(PickupPointLayout pickupPointLayout,
                                       final ShipmentRecipientModel data) {

            pickupPointLayout.setListener(new PickupPointLayout.ViewListener() {
                @Override
                public void onChoosePickupPoint() {
                    viewListener.onChoosePickupPoint(data);
                }

                @Override
                public void onClearPickupPoint(Store oldStore) {
                    viewListener.onClearPickupPoint(data);
                }

                @Override
                public void onEditPickupPoint(Store oldStore) {
                    viewListener.onEditPickupPoint(data);
                }
            });

            if (data.getStore() == null) {
                pickupPointLayout.unSetData(pickupPointLayout.getContext());
                pickupPointLayout.enableChooserButton(pickupPointLayout.getContext());
            } else {
                pickupPointLayout.setData(pickupPointLayout.getContext(), data.getStore());
            }

            pickupPointLayout.setVisibility(View.VISIBLE);
        }

        private View.OnClickListener addOrChangeAddressListener(final ShipmentRecipientModel model) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onAddOrChangeAddress(model);
                    fragmentTransaction();
                }
            };
        }

        private void fragmentTransaction() {
            FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
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

    class DropShipperOptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_dropshipper_option_header) RelativeLayout mRlDropshipperOptionLayout;
        @BindView(R2.id.ll_detail_dropshipper) LinearLayout mLlDetailDropshipperLayout;
        @BindView(R2.id.sw_dropshipper) Switch mSwDropshipper;

        DropShipperOptionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(DropshipperShippingOptionModel model) {
            mLlDetailDropshipperLayout.setVisibility(getVisibility(model.isDropshipping()));
            mSwDropshipper.setChecked(model.isDropshipping());
            mSwDropshipper.setOnClickListener(dropshipperSwitchListener(model));
        }

        private View.OnClickListener dropshipperSwitchListener(final DropshipperShippingOptionModel model) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setDropshipping(!model.isDropshipping());

                    mSwDropshipper.setChecked(model.isDropshipping());
                    mLlDetailDropshipperLayout.setVisibility(getVisibility(model.isDropshipping()));
                }
            };
        }

        private int getVisibility(boolean isDropshipping) {
            return isDropshipping ? View.VISIBLE : View.GONE;
        }

    }

    class ShipmentCostDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_detail_shipment_fee_view_layout) RelativeLayout mRlDetailFee;
        @BindView(R2.id.tv_total_item) TextView mTvTotalItem;
        @BindView(R2.id.tv_total_item_price) TextView mTvTotalItemPrice;
        @BindView(R2.id.tv_shipping_fee) TextView mTvShippingFee;
        @BindView(R2.id.tv_shipping_fee_price) TextView mTvShippingFeePrice;
        @BindView(R2.id.tv_insurance_fee_price) TextView mTvInsuranceFeePrice;
        @BindView(R2.id.tv_promo_price) TextView mTvPromoPrice;
        @BindView(R2.id.tv_drawer_detail_payable) TextView mTvDrawerDetailPayable;
        @BindView(R2.id.iv_drawer_chevron) ImageView mIvDrawerChevron;
        @BindView(R2.id.tv_payable_price) TextView mTvPayablePrice;
        @BindView(R2.id.tv_promo_free_shipping) TextView mTvPromoFreeShipping;

        private boolean mIsExpanded;

        ShipmentCostDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(CartPayableDetailModel model) {
            mIsExpanded = false;

            mTvTotalItem.setText(getTotalItem(model.getTotalItem()));
            mTvTotalItemPrice.setText(model.getTotalItemPrice());
            mTvShippingFee.setText(getShippingFee(model.getShippingWeight()));
            mTvShippingFeePrice.setText(model.getShippingFee());
            mTvInsuranceFeePrice.setText(model.getInsuranceFee());
            mTvPromoPrice.setText(model.getPromoPrice());
            mTvDrawerDetailPayable.setText(getDrawerDetailPayableText(mIsExpanded));
            mIvDrawerChevron.setImageResource(getResourceDrawerChevron(mIsExpanded));
            mTvPayablePrice.setText(model.getPayablePrice());
            mTvPromoFreeShipping.setText(model.getPromoFreeShipping());

            mTvDrawerDetailPayable.setOnClickListener(expandDetailListener);
            mIvDrawerChevron.setOnClickListener(expandDetailListener);
        }

        private void toggleDetail() {
            mIsExpanded = !mIsExpanded;
            mTvDrawerDetailPayable.setText(getDrawerDetailPayableText(mIsExpanded));
            mIvDrawerChevron.setImageResource(getResourceDrawerChevron(mIsExpanded));
            mRlDetailFee.setVisibility(getVisibility(mIsExpanded));
        }

        View.OnClickListener expandDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDetail();
            }
        };

        private String getTotalItem(String totalItem) {
            return String.format("Jumlah Barang (%s Item)", totalItem);
        }

        private String getShippingFee(String weight) {
            return String.format("Ongkos Kirim (%skg)", weight);
        }

        private String getDrawerDetailPayableText(boolean isExpanded) {
            return isExpanded ? "Tutup" : "Detil";
        }

        private int getResourceDrawerChevron(boolean isExpanded) {
            return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
        }

        private int getVisibility(boolean isVisible) {
            return isVisible ? View.VISIBLE : View.GONE;
        }

    }

    class ShippedProductDetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_sender_name) TextView mTvSenderName;

        @BindView(R2.id.iv_product_image_container) ImageView mIvProductImage;
        @BindView(R2.id.tv_shipping_product_name) TextView mTvProductName;
        @BindView(R2.id.tv_shipped_product_price) TextView mTvProductPrice;
        @BindView(R2.id.tv_product_weight) TextView mTvProductWeight;
        @BindView(R2.id.tv_total_product_item) TextView mTvTotalProductItem;
        @BindView(R2.id.tv_optional_note_to_seller) TextView mTvOptionalNote;

        @BindView(R2.id.rl_product_policies_layout) RelativeLayout mRlProductPoliciesContainer;
        @BindView(R2.id.iv_free_return_icon) ImageView mIvFreeReturnIcon;
        @BindView(R2.id.tv_free_return_text) TextView mTvFreeReturnText;
        @BindView(R2.id.tv_po_sign) TextView mTvPoSign;
        @BindView(R2.id.tv_cashback_text) TextView mTvCashback;

        @BindView(R2.id.rv_product_list) RecyclerView mRvProductList;
        @BindView(R2.id.rv_product_thumb_image) RecyclerView mRvProductThumbImage;

        @BindView(R2.id.ll_other_product) LinearLayout mLlOtherProductContainer;
        @BindView(R2.id.tv_expand_other_product) TextView mTvExpandOtherProduct;

        @BindView(R2.id.tv_shipment_option) TextView mTvShipmentOption;
        @BindView(R2.id.iv_chevron_shipment_option) ImageView mIvChevronShipmentOption;

        @BindView(R2.id.rl_detail_shipment_fee) RelativeLayout mRlDetailShipmentFeeContainer;
        @BindView(R2.id.tv_total_item) TextView mTvTotalItem;
        @BindView(R2.id.tv_total_item_price) TextView mTvTotalItemPrice;
        @BindView(R2.id.tv_shipping_fee) TextView mTvShippingFee;
        @BindView(R2.id.tv_shipping_fee_price) TextView mTvShippingFeePrice;
        @BindView(R2.id.tv_insurance_fee_price) TextView mTvInsuranceFeePrice;
        @BindView(R2.id.tv_promo_price) TextView mTvPromoPrice;

        @BindView(R2.id.rl_cart_sub_total) RelativeLayout mRlCartSubTotal;
        @BindView(R2.id.tv_detail_option_text) TextView mTvDetailOptionText;
        @BindView(R2.id.iv_detail_option_chevron) ImageView mIvDetailOptionChevron;
        @BindView(R2.id.tv_sub_total_price) TextView mTvSubTotalPrice;

        private boolean mIsExpandAllProduct;
        private boolean mIsExpandCostDetail;

        ShippedProductDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(CartSellerItemModel model) {
            // Initialize variables
            List<CartItemModel> cartItemModels = model.getCartItemModels();
            CartItemModel mainProductItem = cartItemModels.remove(FIRST_ELEMENT);

            mIsExpandAllProduct = false;
            mIsExpandCostDetail = false;

            // Assign variables
            mTvSenderName.setText(model.getSenderName());
            mTvShipmentOption.setText(model.getShipmentOption());
            mTvSubTotalPrice.setText(model.getTotalPrice());

            ImageHandler.LoadImage(mIvProductImage, mainProductItem.getProductImageUrl());
            mTvProductName.setText(mainProductItem.getProductName());
            mTvProductPrice.setText(mainProductItem.getProductPrice());
            mTvProductWeight.setText(mainProductItem.getProductWeight());
            mTvTotalProductItem.setText(mainProductItem.getTotalProductItem());
            mTvOptionalNote.setText(mainProductItem.getNoteToSeller());

            mLlOtherProductContainer.setVisibility(getExpandOtherProductVisibility(cartItemModels));
            mTvExpandOtherProduct.setText(getExpandOtherProductLabel(cartItemModels,
                    mIsExpandAllProduct));

            mRlProductPoliciesContainer.setVisibility(getPoliciesVisibility());
            mIvFreeReturnIcon.setVisibility(getFreeReturnVisibility(mainProductItem.isFreeReturn()));
            mTvFreeReturnText.setVisibility(getFreeReturnVisibility(mainProductItem.isFreeReturn()));
            mTvPoSign.setVisibility(getPoVisibility(mainProductItem.isPoAvailable()));
            mTvCashback.setVisibility(getCashbackVisibility(mainProductItem.getCashback()));
            mTvCashback.setText(getCashback(mainProductItem.getCashback()));

            mTvDetailOptionText.setText(getTextDrawerChevron(mIsExpandCostDetail));
            mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsExpandCostDetail));

            // Init nested recycler view
            initInnerRecyclerView(cartItemModels);
            initInnerHorizontalRecyclerView(cartItemModels);

            // Set listeners
            mLlOtherProductContainer.setOnClickListener(showAllProductListener(cartItemModels));
            mTvExpandOtherProduct.setOnClickListener(showAllProductListener(cartItemModels));

            mTvShipmentOption.setOnClickListener(selectShippingOptionListener());
            mIvChevronShipmentOption.setOnClickListener(selectShippingOptionListener());

            mTvDetailOptionText.setOnClickListener(costDetailOptionListener());
            mIvDetailOptionChevron.setOnClickListener(costDetailOptionListener());
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

        private void initInnerHorizontalRecyclerView(List<CartItemModel> cartItemModels) {
            mRvProductThumbImage.setVisibility(View.VISIBLE);

            mRvProductThumbImage.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRvProductThumbImage.setLayoutManager(layoutManager);

            InnerProductImageListAdapter innerProductImageListAdapter =
                    new InnerProductImageListAdapter(cartItemModels);
            mRvProductThumbImage.setAdapter(innerProductImageListAdapter);
        }

        private int getExpandOtherProductVisibility(List<CartItemModel> cartItemModels) {
            return cartItemModels.isEmpty() ? View.GONE : View.VISIBLE;
        }

        private String getExpandOtherProductLabel(List<CartItemModel> cartItemModels,
                                                  boolean isExpandAllProduct) {

            return isExpandAllProduct ? "Tutup" :
                    String.format("+%s Produk Lainnya", cartItemModels.size());
        }

        private int getPoliciesVisibility() {
            return View.VISIBLE;
        }

        private int getFreeReturnVisibility(boolean isFreeReturn) {
            return isFreeReturn ? View.VISIBLE : View.GONE;
        }

        private int getPoVisibility(boolean isPoAvailable) {
            return isPoAvailable ? View.VISIBLE : View.GONE;
        }

        private int getCashbackVisibility(String cashback) {
            final String NO_CASHBACK = "0%";

            return NO_CASHBACK.equals(cashback) ? View.GONE : View.VISIBLE;
        }

        private String getCashback(String cashback) {
            return "Cashback " + cashback;
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
            mRvProductList.setVisibility(getOtherProductsVisibility(mIsExpandAllProduct));
            mRvProductThumbImage.setVisibility(getProductImageThumbVisibility(mIsExpandAllProduct));

            mTvExpandOtherProduct.setText(getExpandOtherProductLabel(cartItemModels,
                    mIsExpandAllProduct));
        }

        private int getOtherProductsVisibility(boolean isExpandAllProduct) {
            return isExpandAllProduct ? View.VISIBLE : View.GONE;
        }

        private int getProductImageThumbVisibility(boolean isExpandAllProduct) {
            return isExpandAllProduct ? View.GONE : View.VISIBLE;
        }

        private View.OnClickListener selectShippingOptionListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewListener.onChooseShipment();
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
            mTvDetailOptionText.setText(getTextDrawerChevron(mIsExpandCostDetail));
            mIvDetailOptionChevron.setImageResource(getResourceDrawerChevron(mIsExpandCostDetail));

            mRlDetailShipmentFeeContainer.setVisibility(getDetailFeeVisibility());
        }

        private String getTextDrawerChevron(boolean isExpanded) {
            return isExpanded ? "Tutup" : "Detil";
        }

        private int getResourceDrawerChevron(boolean isExpanded) {
            return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
        }

        private int getDetailFeeVisibility() {
            return mIsExpandCostDetail ? View.VISIBLE : View.GONE;
        }

    }

}
