package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.transaction.R;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class SingleAddressFragmentRecyclerAdapter
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_FREE_SHIPPING_FEE) {
            return new FreeShippingFeeViewHolder(view);
        } else if (viewType == ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS) {
            return new ShipmentRecipientAddressViewHolder(view);
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
            ((ShipmentRecipientAddressViewHolder)viewHolder).bindViewHolder();
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
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TOP_POSITION) {
            return ITEM_VIEW_FREE_SHIPPING_FEE;
        } else if (position == 1) {
            return ITEM_VIEW_SHIPMENT_RECIPIENT_ADDRESS;
        } else if (position == 2) {
            return ITEM_VIEW_DROPSHIPPER_OPTION;
        } else if (position == 5) {
            return ITEM_VIEW_SHIPMENT_COST_DETAIL;
        } else {
            return ITEM_SHIPPED_PRODUCT_DETAILS;
        }
    }

    private class FreeShippingFeeViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlFreeShipmentFeeHeader;
        private TextView mTvShippingFee;

        FreeShippingFeeViewHolder(View itemView) {
            super(itemView);
            mRlFreeShipmentFeeHeader = itemView.findViewById(R.id.rl_free_shipment_fee_header);
            mTvShippingFee = itemView.findViewById(R.id.tv_shpping_fee);
        }

        void bindViewHolder() {
            mRlFreeShipmentFeeHeader.setVisibility(getVisibility());
            mTvShippingFee.setText(getShippingFee());
        }

        private int getVisibility() {
            boolean visible = true;
            return visible ? View.VISIBLE : View.GONE;
        }

        private String getShippingFee() {
            return "Rp.35.000";
        }

    }

    private class ShipmentRecipientAddressViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLlRecipientAddressHeader;
        TextView mTvRecipientName;
        TextView mTvRecipientAddress;
        TextView mTvAddOrChangeAddress;

        ShipmentRecipientAddressViewHolder(View itemView) {
            super(itemView);
            mLlRecipientAddressHeader = itemView.findViewById(R.id.ll_shipment_recipient_address_header);
            mTvRecipientName = itemView.findViewById(R.id.tv_recipient_name);
            mTvRecipientAddress = itemView.findViewById(R.id.tv_recipient_address);
            mTvAddOrChangeAddress = itemView.findViewById(R.id.tv_add_or_change_address);
        }

        void bindViewHolder() {
            mTvRecipientName.setText(getRecipientName());
            mTvRecipientAddress.setText(getRecipientAddress());
            mTvAddOrChangeAddress.setOnClickListener(addOrChangeAddressListener());
        }

        private View.OnClickListener addOrChangeAddressListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Add or change address", Toast.LENGTH_SHORT)
                            .show();
                }
            };
        }

        private String getRecipientName() {
            return "Agus Maulana";
        }

        private String getRecipientAddress() {
            return "Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\\nTokopedia Lt. 2, Jakarta, 0817 1234 5678";
        }

    }

    private class DropShipperOptionViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlDropshipperOptionLayout;
        private Switch mSwDropshipper;

        DropShipperOptionViewHolder(View itemView) {
            super(itemView);
            mRlDropshipperOptionLayout = itemView.findViewById(R.id.rl_dropshipper_option_header);
            mSwDropshipper = itemView.findViewById(R.id.sw_dropshipper);
        }

        void bindViewHolder() {
            mRlDropshipperOptionLayout.setVisibility(getVisibility());
            mSwDropshipper.setChecked(getSwitchChecked());
            mSwDropshipper.setOnClickListener(dropshipperSwitchListener());
        }

        private View.OnClickListener dropshipperSwitchListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSwDropshipper.toggle();
                }
            };
        }

        private int getVisibility() {
            return View.VISIBLE;
        }

        private boolean getSwitchChecked() {
            return true;
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

        private String getTotalItem() {
            return "Jumlah Barang (1 Item)";
        }

        private String getTotalItemPrice() {
            return "Rp1.400.000";
        }

        private String getShippingFee() {
            return "Ongkos Kirim (1kg)";
        }

        private String getShippingFeePrice() {
            return "Rp0";
        }

        private String getInsuranceFeePrice() {
            return "Rp0";
        }

        private String getPromoPrice() {
            return "Rp100.000";
        }

        private String getDrawerDetailPayableText() {
            return "Tutup";
        }

        private int getResourceDrawerChevron() {
            return R.drawable.chevron_thin_down;
        }

        private String getPayablePrice() {
            return "Rp1.300.000";
        }

        private String getPromoFreeShippingText() {
            return "Anda mendapat gratis ongkir Rp20.000";
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
            mTvSenderName.setText("Adidas");
            mTvProductName.setText("Kaos Adidas Camo Tongue Tee... White & Red, XS");
            mTvProductPrice.setText("Rp200.000");
            mTvCashback.setText("Cashback 5%");
            mTvProductWeight.setText("3kg");
            mTvTotalProductItem.setText("1");
            mTvNoteToSeller.setText("Saya pesan warna merah yah min.. jangan sampai salah\n" +
                    "kirim barangnya gan!");
            mTvShipmentOption.setText("Go-send Instan");
            mTvSubTotalItemPrice.setText("Rp200.000");

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

    }

}
