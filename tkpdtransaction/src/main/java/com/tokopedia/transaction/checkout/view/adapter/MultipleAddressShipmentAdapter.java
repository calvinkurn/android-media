package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentAdapter extends RecyclerView.Adapter
        <RecyclerView.ViewHolder> {

    private static final int MULTIPLE_ADDRESS_SHIPMENT_HEADER_LAYOUT =
            R.layout.multiple_address_header;
    private static final int MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT =
            R.layout.multiple_address_shipment_footer;
    private static final int MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT =
            R.layout.multiple_address_shipment_total;
    private static final int MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT =
            R.layout.multiple_address_shipment_adapter;
    private static final int HEADER_SIZE = 1;
    private static final int FOOTER_SIZE = 2;

    private List<MultipleAddressShipmentAdapterData> addressDataList;

    private MultipleAddressPriceSummaryData priceSummaryData;

    private MultipleAddressShipmentAdapterListener listener;

    public MultipleAddressShipmentAdapter(List<MultipleAddressShipmentAdapterData> addressDataList,
                                          MultipleAddressPriceSummaryData priceSummaryData,
                                          MultipleAddressShipmentAdapterListener listener) {
        this.addressDataList = addressDataList;
        this.priceSummaryData = priceSummaryData;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return MULTIPLE_ADDRESS_SHIPMENT_HEADER_LAYOUT;
        else if (position == addressDataList.size() + HEADER_SIZE)
            return MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT;
        else if (position == addressDataList.size() + HEADER_SIZE + 1)
            return MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT;
        else
            return MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == MULTIPLE_ADDRESS_SHIPMENT_HEADER_LAYOUT)
            return new MultipleAddressHeaderViewHolder(itemView);
        else if (viewType == MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT)
            return new MultipleAddressShipmentFooterViewHolder(itemView);
        else if (viewType == MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT)
            return new MultipleAddressShipmentFooterTotalPayment(itemView);
        else return new MultipleShippingAddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MultipleShippingAddressViewHolder) {
            bindItems((MultipleShippingAddressViewHolder) holder, position);
        } else if (holder instanceof MultipleAddressShipmentFooterViewHolder) {
            bindFooterView((MultipleAddressShipmentFooterViewHolder) holder);
        } else if (holder instanceof MultipleAddressShipmentFooterTotalPayment) {
            MultipleAddressShipmentFooterTotalPayment totalPaymentHolder =
                    (MultipleAddressShipmentFooterTotalPayment) holder;
            totalPaymentHolder.totalPayment.setText(
                    totalPriceChecker(formatPrice(priceSummaryData.getTotalPayment()),
                            priceSummaryData.getTotalShippingPrice()
                    )
            );
        }
    }

    private void bindFooterView(MultipleAddressShipmentFooterViewHolder holder) {
        MultipleAddressShipmentFooterViewHolder footerHolder =
                holder;
        footerHolder.quantityTotal.setText(footerHolder.quantityTotal.getText()
                .toString()
                .replace("#", priceSummaryData.getQuantityText()));
        footerHolder.totalProductPrice.setText(
                formatPrice(priceSummaryData.getTotalProductPrice())
        );
        footerHolder.totalShippingPrice.setText(priceChecker(
                priceSummaryData.getTotalShippingPrice(),
                priceSummaryData.getTotalShippingPrice())
        );
        footerHolder.insurancePrice.setText(priceChecker(
                priceSummaryData.getInsurancePrice(),
                priceSummaryData.getTotalShippingPrice())
        );
        footerHolder.additionalFee.setText(priceChecker(
                priceSummaryData.getAdditionalFee(),
                priceSummaryData.getTotalShippingPrice())
        );
        footerHolder.promoDiscount.setText(priceChecker(
                priceSummaryData.getPromoDiscount(),
                priceSummaryData.getTotalShippingPrice())
        );
    }

    private void bindItems(MultipleShippingAddressViewHolder holder, int position) {
        MultipleShippingAddressViewHolder itemViewHolder = holder;
        MultipleAddressShipmentAdapterData data = addressDataList.get(position - 1);
        MultipleAddressItemData itemData = data.getItemData();
        itemViewHolder.senderName.setText(data.getSenderName());
        ImageHandler.LoadImage(itemViewHolder.productImage, data.getProductImageUrl());
        itemViewHolder.productName.setText(data.getProductName());
        itemViewHolder.productPrice.setText(data.getProductPrice());
        itemViewHolder.productWeight.setText(itemData.getProductWeight());
        itemViewHolder.productQty.setText(itemData.getProductQty());
        itemViewHolder.notesField.setText(itemData.getProductNotes());
        itemViewHolder.addressTitle.setText(itemData.getAddressTitle());
        itemViewHolder.addressReceiverName.setText(itemData.getAddressReceiverName());
        itemViewHolder.address.setText(itemData.getAddress());
        itemViewHolder.subTotalAmount.setText(data.getSubTotalAmount());
        itemViewHolder.chooseCourierButton.setOnClickListener(onChooseCourierClicked(data));
        itemViewHolder.chooseCourierButton.setOnClickListener(getChooseCourierClickListener(data));
//        renderPickupPoint(itemViewHolder, data);
    }

    private View.OnClickListener getChooseCourierClickListener(final MultipleAddressShipmentAdapterData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChooseShipment(data);
            }
        };
    }

    private void renderPickupPoint(final MultipleShippingAddressViewHolder itemViewHolder,
                                   final MultipleAddressShipmentAdapterData data) {
        itemViewHolder.pickupPointLayout.setListener(new PickupPointLayout.ViewListener() {
            @Override
            public void onChoosePickupPoint() {
                listener.onChoosePickupPoint(data, itemViewHolder.getAdapterPosition());
            }

            @Override
            public void onClearPickupPoint(Store oldStore) {
                listener.onClearPickupPoint(data, itemViewHolder.getAdapterPosition());
            }

            @Override
            public void onEditPickupPoint(Store oldStore) {
                listener.onEditPickupPoint(data, itemViewHolder.getAdapterPosition());
            }
        });
        if (data.getStore() == null) {
            itemViewHolder.pickupPointLayout.unSetData(itemViewHolder.pickupPointLayout.getContext());
            itemViewHolder.pickupPointLayout.enableChooserButton(itemViewHolder.pickupPointLayout.getContext());
            itemViewHolder.chooseCourierButton.setText("Pilih Kurir");
        } else {
            itemViewHolder.pickupPointLayout.setData(itemViewHolder.pickupPointLayout.getContext(), data.getStore());
            itemViewHolder.chooseCourierButton.setText("Alfatrex");
        }
        itemViewHolder.pickupPointLayout.setVisibility(View.VISIBLE);
    }

    public void setPickupPoint(Store store, int position) {
        addressDataList.get(position - 1).setStore(store);
    }

    public void unSetPickupPoint(int position) {
        addressDataList.get(position - 1).setStore(null);
    }

    public List<MultipleAddressShipmentAdapterData> getAddressDataList() {
        return addressDataList;
    }

    public MultipleAddressPriceSummaryData getPriceSummaryData() {
        return priceSummaryData;
    }

    @Override
    public int getItemCount() {
        return HEADER_SIZE + addressDataList.size() + FOOTER_SIZE;
    }

    class MultipleShippingAddressViewHolder extends RecyclerView.ViewHolder {

        private TextView senderName;

        private ImageView productImage;

        private TextView productName;

        private TextView productPrice;

        private TextView productWeight;

        private TextView productQty;

        private TextView notesField;

        private ViewGroup addressLayout;

        private TextView addressTitle;

        private TextView addressReceiverName;

        private TextView address;

        private TextView chooseCourierButton;

        private ViewGroup subTotalLayout;

        private TextView subTotalAmount;

        private PickupPointLayout pickupPointLayout;

        private TextView phoneNumber;

        private RelativeLayout rlProductPoliciesLayout;

        private ImageView ivFreeReturnIcon;

        private TextView tvFreeReturnText;

        private TextView tvPoSign;

        private TextView tvCashbackText;

        MultipleShippingAddressViewHolder(View itemView) {
            super(itemView);

            senderName = itemView.findViewById(R.id.sender_name);

            productImage = itemView.findViewById(R.id.iv_product_image_container);

            productName = itemView.findViewById(R.id.tv_shipping_product_name);

            productPrice = itemView.findViewById(R.id.tv_shipped_product_price);

            productWeight = itemView.findViewById(R.id.tv_product_weight);

            productQty = itemView.findViewById(R.id.tv_total_product_item);

            notesField = itemView.findViewById(R.id.tv_optional_note_to_seller);

            addressLayout = itemView.findViewById(R.id.address_layout);

            addressTitle = itemView.findViewById(R.id.tv_text_address_description);

            addressReceiverName = itemView.findViewById(R.id.tv_recipient_name);

            address = itemView.findViewById(R.id.tv_recipient_address);

            phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);
            phoneNumber.setVisibility(View.GONE);

            chooseCourierButton = itemView.findViewById(R.id.choose_courier_button);

            subTotalLayout = itemView.findViewById(R.id.sub_total_layout);

            subTotalAmount = itemView.findViewById(R.id.sub_total_amount);

            pickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);

            rlProductPoliciesLayout = itemView.findViewById(R.id.rl_product_policies_layout);
            rlProductPoliciesLayout.setVisibility(View.GONE);

            ivFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);

            tvFreeReturnText = itemView.findViewById(R.id.tv_free_return_text);

            tvPoSign = itemView.findViewById(R.id.tv_po_sign);

            tvCashbackText = itemView.findViewById(R.id.tv_cashback_text);
        }
    }

    class MultipleAddressHeaderViewHolder extends RecyclerView.ViewHolder {

        MultipleAddressHeaderViewHolder(View itemView) {
            super(itemView);

        }
    }

    class MultipleAddressShipmentFooterViewHolder extends RecyclerView.ViewHolder {

        private TextView quantityTotal;

        private TextView totalProductPrice;

        private TextView totalShippingPrice;

        private TextView insurancePrice;

        private TextView additionalFee;

        private TextView promoDiscount;

        MultipleAddressShipmentFooterViewHolder(View itemView) {
            super(itemView);
            quantityTotal = itemView.findViewById(R.id.qty_total);

            totalProductPrice = itemView.findViewById(R.id.total_product_price);

            totalShippingPrice = itemView.findViewById(R.id.total_shipping_price);

            insurancePrice = itemView.findViewById(R.id.insurance_price);

            additionalFee = itemView.findViewById(R.id.additional_fee);

            promoDiscount = itemView.findViewById(R.id.promo_discount);

        }
    }

    private class MultipleAddressShipmentFooterTotalPayment extends RecyclerView.ViewHolder {

        private TextView totalPayment;

        MultipleAddressShipmentFooterTotalPayment(View itemView) {
            super(itemView);
            totalPayment = itemView.findViewById(R.id.total_payment);
        }
    }

    private View.OnClickListener onChooseCourierClicked(
            final MultipleAddressShipmentAdapterData data
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChooseShipment(data);
            }
        };
    }

    public interface MultipleAddressShipmentAdapterListener {

        void onConfirmedButtonClicked(
                List<MultipleAddressShipmentAdapterData> addressDataList,
                MultipleAddressPriceSummaryData summaryData);

        void onChooseShipment(MultipleAddressShipmentAdapterData addressAdapterData);

        void onChoosePickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position);

        void onClearPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position);

        void onEditPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position);

    }

    private String totalPriceChecker(String totalPriceText, long shipmentPrice) {
        if (shipmentPrice > 0) return totalPriceText;
        else return "-";
    }

    private String priceChecker(long price, long shipmentPrice) {
        if (shipmentPrice > 0) return formatPrice(price);
        else return "-";
    }

    private String formatPrice(long unformattedPrice) {
        String formattedPrice = CurrencyFormatHelper
                .ConvertToRupiah(String.valueOf(unformattedPrice));
        formattedPrice = formattedPrice.replace(",", ".");
        return formattedPrice;
    }

}
