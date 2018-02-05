package com.tokopedia.transaction.checkout.view.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.DropshipperShippingOptionModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentFeeBannerModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartSingleAddressAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    private Context mContext;
    private CartSingleAddressData mCartSingleAddressData;

    public CartSingleAddressAdapter() {

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
            ((FreeShippingFeeViewHolder)viewHolder).bindViewHolder();
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            ((ShippingRecipientViewHolder)viewHolder).bindViewHolder();
        } else if (viewType == ITEM_VIEW_DROPSHIPPER_OPTION) {
            ((DropShipperOptionViewHolder)viewHolder).bindViewHolder();
        } else if (viewType == ITEM_VIEW_SHIPMENT_COST_DETAIL) {
            ((ShipmentCostDetailViewHolder)viewHolder).bindViewHolder();
        } else {
            ((ShippedProductDetailsViewHolder)viewHolder).bindViewHolder();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TOP_POSITION) {
            return ITEM_VIEW_FREE_SHIPPING_FEE;
        } else if (position == 1) {
            return ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS;
        } else if (position == 2) {
            return ITEM_VIEW_DROPSHIPPER_OPTION;
        } else if (position == 4) {
            return ITEM_VIEW_SHIPMENT_COST_DETAIL;
        } else {
            return ITEM_SHIPPED_PRODUCT_DETAILS;
        }
    }

    private int getShippedItemListSize() {
        return mCartSingleAddressData.getCartItemModelList().size();
    }

    class FreeShippingFeeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_free_shipment_fee_header) RelativeLayout mRlFreeShipmentFeeHeader;
        @BindView(R2.id.tv_shpping_fee) TextView mTvShippingFee;

        private ShipmentFeeBannerModel mShipmentFeeBannerModel;

        FreeShippingFeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {
            mShipmentFeeBannerModel = mCartSingleAddressData.getShipmentFeeBannerModel();

            mRlFreeShipmentFeeHeader.setVisibility(getVisibility());
            mTvShippingFee.setText(getShippingFee());
        }

        private int getVisibility() {
            return mShipmentFeeBannerModel.isVisible() ? View.VISIBLE : View.GONE;
        }

        private String getShippingFee() {
            return mShipmentFeeBannerModel.getShipmentFeeDiscount();
        }

    }

    class ShippingRecipientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_recipient_name) TextView mTvRecipientName;
        @BindView(R2.id.tv_recipient_address) TextView mTvRecipientAddress;
        @BindView(R2.id.tv_add_or_change_address) TextView mTvAddOrChangeAddress;

        private ShipmentRecipientModel mShipmentRecipientModel;

        ShippingRecipientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {
            mShipmentRecipientModel = mCartSingleAddressData.getShipmentRecipientModel();

            mTvRecipientName.setText(getRecipientName());
            mTvRecipientAddress.setText(getRecipientAddress());
            mTvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());
        }

        private View.OnClickListener addOrChangeAddressListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            };
        }

        private String getRecipientName() {
            return mShipmentRecipientModel.getRecipientName();
        }

        private String getRecipientAddress() {
            return mShipmentRecipientModel.getRecipientAddress();
        }

    }

    class DropShipperOptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_dropshipper_option_header) RelativeLayout mRlDropshipperOptionLayout;
        @BindView(R2.id.sw_dropshipper) Switch mSwDropshipper;

        private DropshipperShippingOptionModel mDropshipperOptionModel;

        DropShipperOptionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {
            mDropshipperOptionModel = mCartSingleAddressData.getDropshipperShippingOptionModel();

            mRlDropshipperOptionLayout.setVisibility(getVisibility());
            mSwDropshipper.setChecked(getSwitchChecked());
            mSwDropshipper.setOnClickListener(dropshipperSwitchListener());
        }

        private View.OnClickListener dropshipperSwitchListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isDropshipping = !mDropshipperOptionModel.isDropshipping();

                    mSwDropshipper.setChecked(isDropshipping);
                    mDropshipperOptionModel.setDropshipping(isDropshipping);

                    Toast.makeText(mContext, isDropshipping ? "True" : "False", Toast.LENGTH_SHORT).show();
                }
            };
        }

        private int getVisibility() {
            return View.VISIBLE;
        }

        private boolean getSwitchChecked() {
            return mDropshipperOptionModel.isDropshipping();
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

        private CartPayableDetailModel mCartPayableDetailModel;
        private boolean isExpanded;

        ShipmentCostDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {
            mCartPayableDetailModel = mCartSingleAddressData.getCartPayableDetailModel();
            isExpanded = true;

            mTvTotalItem.setText(getTotalItem());
            mTvTotalItemPrice.setText(getTotalItemPrice());
            mTvShippingFee.setText(getShippingFee());
            mTvShippingFeePrice.setText(getShippingFeePrice());
            mTvInsuranceFeePrice.setText(getInsuranceFeePrice());
            mTvPromoPrice.setText(getPromoPrice());
            mTvDrawerDetailPayable.setText(getDrawerDetailPayableText());
            mIvDrawerChevron.setImageResource(getResourceDrawerChevron());
            mTvPayablePrice.setText(getPayablePrice());
            mTvPromoFreeShipping.setText(getPromoFreeShippingText());

            mTvDrawerDetailPayable.setOnClickListener(expandDetailListener);
            mIvDrawerChevron.setOnClickListener(expandDetailListener);
        }

        private void toggleDetail() {
            isExpanded = !isExpanded;

            mTvDrawerDetailPayable.setText(getDrawerDetailPayableText());
            mIvDrawerChevron.setImageResource(getResourceDrawerChevron());
            mRlDetailFee.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

        View.OnClickListener expandDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDetail();
            }
        };

        private String getTotalItem() {
            return String.format("Jumlah Barang (%s Item)", mCartPayableDetailModel.getTotalItem());
        }

        private String getTotalItemPrice() {
            return mCartPayableDetailModel.getTotalItemPrice();
        }

        private String getShippingFee() {
            return String.format("Ongkos Kirim (%skg)", mCartPayableDetailModel.getShippingWeight());
        }

        private String getShippingFeePrice() {
            return mCartPayableDetailModel.getShippingFee();
        }

        private String getInsuranceFeePrice() {
            return mCartPayableDetailModel.getInsuranceFee();
        }

        private String getPromoPrice() {
            return mCartPayableDetailModel.getPromoPrice();
        }

        private String getDrawerDetailPayableText() {
            return isExpanded ? "Tutup" : "Detil";
        }

        private int getResourceDrawerChevron() {
            return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
        }

        private String getPayablePrice() {
            return mCartPayableDetailModel.getPayablePrice();
        }

        private String getPromoFreeShippingText() {
            return mCartPayableDetailModel.getPromoFreeShipping();
        }

    }

    class ShippedProductDetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.rl_detail_shipment_fee_view_layout) RelativeLayout mRlDetailFee;
        @BindView(R2.id.tv_sender_name) TextView mTvSenderName;
        @BindView(R2.id.iv_product_image_container) ImageView mIvProductImage;
        @BindView(R2.id.tv_shipping_product_name) TextView mTvProductName;
        @BindView(R2.id.tv_shipped_product_price) TextView mTvProductPrice;
        @BindView(R2.id.rl_product_policies_layout) RelativeLayout mRlProductPoliciesLayout;
        @BindView(R2.id.iv_free_return_icon) ImageView mIvFreeReturnIcon;
        @BindView(R2.id.tv_po_sign) TextView mTvPoSign;
        @BindView(R2.id.tv_cashback_text) TextView mTvCashback;
        @BindView(R2.id.tv_product_weight) TextView mTvProductWeight;
        @BindView(R2.id.tv_total_product_item) TextView mTvTotalProductItem;
        @BindView(R2.id.tv_optional_note_to_seller) TextView mTvNoteToSeller;
        @BindView(R2.id.rl_product_image_galleries) RelativeLayout mRlProductGalleriesLayout;
        @BindView(R2.id.tv_shipment_option) TextView mTvShipmentOption;
        @BindView(R2.id.iv_chevron_shipment_option) ImageView mIvChevronShipmentOption;
        @BindView(R2.id.tv_cart_detail_option) TextView mTvCartDetailOption;
        @BindView(R2.id.iv_drawer_chevron) ImageView mIvDetailDrawerChevron;
        @BindView(R2.id.tv_sub_total_item_price) TextView mTvSubTotalItemPrice;

        private CartItemModel mCartItemModel;
        private boolean isExpanded;

        ShippedProductDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {
            mCartItemModel = mCartSingleAddressData.getCartItemModelList().get(0);
            isExpanded = true;

            mTvSenderName.setText(getSenderName());
            mTvProductName.setText(getProductName());
            mTvProductPrice.setText(getProductPrice());
            mTvCashback.setText(getCashback());
            mTvProductWeight.setText(getProductWeight());
            mTvTotalProductItem.setText(getTotalProductItem());
            mTvNoteToSeller.setText(getNoteToSeller());
            mTvShipmentOption.setText(getShipmentOption());
            mTvSubTotalItemPrice.setText(getTotalPrice());

            ImageHandler.LoadImage(mIvProductImage, getProductImage());
            mRlProductPoliciesLayout.setVisibility(getPoliciesVisibility());
            mIvFreeReturnIcon.setVisibility(getFreeReturnIconVisibility());
            mTvPoSign.setVisibility(getPoStatus());
            mRlProductGalleriesLayout.setVisibility(getProductGalleryVisibility());

            mTvShipmentOption.setOnClickListener(courierOptionSelectionListener());
            mIvChevronShipmentOption.setOnClickListener(courierOptionSelectionListener());

            mTvCartDetailOption.setText(getTextDrawerChevron());
            mIvDetailDrawerChevron.setImageResource(getResourceDrawerChevron());

            mTvCartDetailOption.setOnClickListener(itemPriceDetailListener());
            mIvDetailDrawerChevron.setOnClickListener(itemPriceDetailListener());

            mRlDetailFee.setVisibility(getDetailFeeVisibility() );
        }

        private void toggleDetail() {
            isExpanded = !isExpanded;

            mTvCartDetailOption.setText(getTextDrawerChevron());
            mIvDetailDrawerChevron.setImageResource(getResourceDrawerChevron());

            mRlDetailFee.setVisibility(getDetailFeeVisibility());
        }

        private String getTextDrawerChevron() {
            return isExpanded ? "Tutup" : "Detil";
        }

        private int getResourceDrawerChevron() {
            return isExpanded ? R.drawable.chevron_thin_up : R.drawable.chevron_thin_down;
        }

        private View.OnClickListener itemPriceDetailListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleDetail();
                }
            };
        }

        private View.OnClickListener courierOptionSelectionListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Select Courier", Toast.LENGTH_SHORT).show();
                }
            };
        }

        private String getSenderName() {
            return mCartItemModel.getSenderName();
        }

        private String getProductName() {
            return mCartItemModel.getProductName();
        }

        private String getProductPrice() {
            return mCartItemModel.getProductPrice();
        }

        private String getCashback() {
            return "Cashback " + mCartItemModel.getCashback();
        }

        private String getProductWeight() {
            return mCartItemModel.getProductWeight();
        }

        private String getTotalProductItem() {
            return mCartItemModel.getTotalProductItem();
        }

        private String getNoteToSeller() {
            return mCartItemModel.getNoteToSeller();
        }

        private String getShipmentOption() {
            return mCartItemModel.getShipmentOption();
        }

        private String getTotalPrice() {
            return mCartItemModel.getTotalPrice();
        }

        private String getProductImage() {
            return mCartItemModel.getProductImageUrl();
        }

        private int getPoliciesVisibility() {
            return View.VISIBLE;
        }

        private int getFreeReturnIconVisibility() {
            return mCartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE;
        }

        private int getPoStatus() {
            return mCartItemModel.isPoAvailable() ? View.VISIBLE : View.GONE;
        }

        private int getDetailFeeVisibility() {
            return isExpanded ? View.VISIBLE : View.GONE;
        }

        private int getProductGalleryVisibility() {
            return View.VISIBLE;
        }

    }

}
