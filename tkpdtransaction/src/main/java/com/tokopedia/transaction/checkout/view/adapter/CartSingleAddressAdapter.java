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

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.ShippingAddressListFragment;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.DropshipperShippingOptionModel;
import com.tokopedia.transaction.checkout.view.data.ShippingFeeBannerModel;
import com.tokopedia.transaction.checkout.view.data.ShippingRecipientModel;

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

    private class FreeShippingFeeViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlFreeShipmentFeeHeader;
        private TextView mTvShippingFee;

        private ShippingFeeBannerModel mShippingFeeBannerModel;

        FreeShippingFeeViewHolder(View itemView) {
            super(itemView);
            mRlFreeShipmentFeeHeader = itemView.findViewById(R.id.rl_free_shipment_fee_header);
            mTvShippingFee = itemView.findViewById(R.id.tv_shpping_fee);
        }

        void bindViewHolder() {
            mShippingFeeBannerModel = mCartSingleAddressData.getShippingFeeBannerModel();

            mRlFreeShipmentFeeHeader.setVisibility(getVisibility());
            mTvShippingFee.setText(getShippingFee());
        }

        private int getVisibility() {
            return mShippingFeeBannerModel.isVisible() ? View.VISIBLE : View.GONE;
        }

        private String getShippingFee() {
            return mShippingFeeBannerModel.getShipmentFeeDiscount();
        }

    }

    private class ShippingRecipientViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mRlRecipientAddressHeader;
        TextView mTvRecipientName;
        TextView mTvRecipientAddress;
        TextView mTvAddOrChangeAddress;

        private ShippingRecipientModel mShippingRecipientModel;

        ShippingRecipientViewHolder(View itemView) {
            super(itemView);
            mRlRecipientAddressHeader = itemView.findViewById(R.id.rl_shipment_recipient_address_header);
            mTvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
            mTvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
            mTvAddOrChangeAddress = itemView.findViewById(R.id.tv_add_or_change_address);
        }

        void bindViewHolder() {
            mShippingRecipientModel = mCartSingleAddressData.getShippingRecipientModel();

            mTvRecipientName.setText(getRecipientName());
            mTvRecipientAddress.setText(getRecipientAddress());
            mTvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());
        }

        private View.OnClickListener addOrChangeAddressListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.container);
                    if (fragment == null || !(fragment instanceof ShippingAddressListFragment)) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, ShippingAddressListFragment.newInstance())
                                .commit();
                        // TODO: add to back stack
                    }
                }
            };
        }

        private String getRecipientName() {
            return mShippingRecipientModel.getRecipientName();
        }

        private String getRecipientAddress() {
            return mShippingRecipientModel.getRecipientAddress();
        }

    }

    private class DropShipperOptionViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlDropshipperOptionLayout;
        private Switch mSwDropshipper;

        private DropshipperShippingOptionModel mDropshipperOptionModel;

        DropShipperOptionViewHolder(View itemView) {
            super(itemView);
            mRlDropshipperOptionLayout = itemView.findViewById(R.id.rl_dropshipper_option_header);
            mSwDropshipper = itemView.findViewById(R.id.sw_dropshipper);
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
                    mSwDropshipper.toggle();
                    mDropshipperOptionModel.setDropshipping(!mDropshipperOptionModel.isDropshipping());
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

    private class ShipmentCostDetailViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTotalItem;
        private TextView mTvTotalItemPrice;
        private TextView mTvShippingFee;
        private TextView mTvShippingFeePrice;
        private TextView mTvInsuranceFeePrice;
        private TextView mTvPromoPrice;
        private TextView mTvDrawerDetailPayable;
        private ImageView mIvDrawerChevron;
        private TextView mTvPayablePrice;
        private TextView mTvPromoFreeShipping;

        private boolean isExpanded;
        private CartPayableDetailModel mCartPayableDetailModel;

        ShipmentCostDetailViewHolder(View itemView) {
            super(itemView);
            mTvTotalItem = itemView.findViewById(R.id.tv_total_item);
            mTvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
            mTvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
            mTvShippingFeePrice = itemView.findViewById(R.id.tv_shipping_fee_price);
            mTvInsuranceFeePrice = itemView.findViewById(R.id.tv_insurance_fee_price);
            mTvPromoPrice = itemView.findViewById(R.id.tv_promo_price);
            mTvDrawerDetailPayable = itemView.findViewById(R.id.tv_drawer_detail_payable);
            mIvDrawerChevron = itemView.findViewById(R.id.iv_drawer_chevron);
            mTvPayablePrice = itemView.findViewById(R.id.tv_payable_price);
            mTvPromoFreeShipping = itemView.findViewById(R.id.tv_promo_free_shipping);
        }

        void bindViewHolder() {
            mCartPayableDetailModel = mCartSingleAddressData.getCartPayableDetailModel();

            isExpanded = false;
            mTvDrawerDetailPayable.setOnClickListener(expandDetailListener);

            mTvTotalItem.setText(getTotalItem());
            mTvTotalItemPrice.setText(getTotalItemPrice());
            mTvShippingFee.setText(getShippingFee());
            mTvShippingFeePrice.setText(getShippingFeePrice());
            mTvInsuranceFeePrice.setText(getInsuranceFeePrice());
            mTvPromoPrice.setText(getPromoPrice());
            mTvDrawerDetailPayable.setText(getDrawerDetailPayableText());
            mIvDrawerChevron.setBackgroundResource(getResourceDrawerChevron());
            mTvPayablePrice.setText(getPayablePrice());
            mTvPromoFreeShipping.setText(getPromoFreeShippingText());
        }

        View.OnClickListener expandDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;
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

    private class ShippedProductDetailsViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvSenderName;
        private ImageView mIvProductImage;
        private TextView mTvProductName;
        private TextView mTvProductPrice;
        private RelativeLayout mRlProductPoliciesLayout;
        private ImageView mIvFreeReturnIcon;
        private TextView mTvPoSign;
        private TextView mTvCashback;
        private TextView mTvProductWeight;
        private TextView mTvTotalProductItem;
        private TextView mTvNoteToSeller;
        private RelativeLayout mRlProductGalleriesLayout;
        private TextView mTvShipmentOption;
        private ImageView mIvChevronShipmentOption;
        private TextView mTvCartDetailOption;
        private ImageView mIvDetailDrawerChevron;
        private TextView mTvSubTotalItemPrice;

        private CartItemModel mCartItemModel;

        ShippedProductDetailsViewHolder(View itemView) {
            super(itemView);
            mTvSenderName = itemView.findViewById(R.id.tv_sender_name);
            mIvProductImage = itemView.findViewById(R.id.iv_product_image_container);
            mTvProductName = itemView.findViewById(R.id.tv_shipping_product_name);
            mTvProductPrice = itemView.findViewById(R.id.tv_shipped_product_price);
            mRlProductPoliciesLayout = itemView.findViewById(R.id.rl_product_policies_layout);
            mIvFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);
            mTvPoSign = itemView.findViewById(R.id.tv_po_sign);
            mTvCashback = itemView.findViewById(R.id.tv_cashback_text);
            mTvProductWeight = itemView.findViewById(R.id.tv_product_weight);
            mTvTotalProductItem = itemView.findViewById(R.id.tv_total_product_item);
            mTvNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller);
            mRlProductGalleriesLayout = itemView.findViewById(R.id.rl_product_image_galleries);
            mTvShipmentOption = itemView.findViewById(R.id.tv_shipment_option);
            mIvChevronShipmentOption = itemView.findViewById(R.id.iv_chevron_shipment_option);
            mTvCartDetailOption = itemView.findViewById(R.id.tv_cart_detail_option);
            mIvDetailDrawerChevron = itemView.findViewById(R.id.iv_drawer_chevron);
            mTvSubTotalItemPrice = itemView.findViewById(R.id.tv_sub_total_item_price);
        }

        void bindViewHolder() {
            mCartItemModel = mCartSingleAddressData.getCartItemModelList().get(0);

            mTvSenderName.setText(getSenderName());
            mTvProductName.setText(getProductName());
            mTvProductPrice.setText(getProductPrice());
            mTvCashback.setText(getCashback());
            mTvProductWeight.setText(getProductWeight());
            mTvTotalProductItem.setText(getTotalProductItem());
            mTvNoteToSeller.setText(getNoteToSeller());
            mTvShipmentOption.setText(getShippmentOption());
            mTvSubTotalItemPrice.setText(getTotalPrice());

            ImageHandler.LoadImage(mIvProductImage, getProductImage());
            mRlProductPoliciesLayout.setVisibility(getPoliciesVisibility());
            mIvFreeReturnIcon.setVisibility(getFreeReturnIconVisibility());
            mTvPoSign.setVisibility(getPoStatus());
            mRlProductGalleriesLayout.setVisibility(getProductGalleryVisibility());

            mIvChevronShipmentOption.setOnClickListener(courierOptionSelectionListener());
            mTvCartDetailOption.setOnClickListener(itemPriceDetailListener());
            mIvDetailDrawerChevron.setOnClickListener(itemPriceDetailListener());
        }

        private View.OnClickListener itemPriceDetailListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            };
        }

        private View.OnClickListener courierOptionSelectionListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

        private String getShippmentOption() {
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

        private int getProductGalleryVisibility() {
            return View.GONE;
        }

    }

}
