package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressTotalPriceHolderData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.viewholder.CartPromoSuggestionViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.CartVoucherPromoViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.MultipleAddressShipmentFooterTotalPayment;
import com.tokopedia.transaction.checkout.view.viewholder.MultipleAddressShipmentFooterViewHolder;
import com.tokopedia.transaction.checkout.view.viewholder.MultipleShippingAddressViewHolder;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentAdapter extends RecyclerView.Adapter
        <RecyclerView.ViewHolder> {

    private static final int MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT =
            R.layout.multiple_address_shipment_footer;
    private static final int MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT =
            R.layout.multiple_address_shipment_total;
    private static final int MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT =
            R.layout.multiple_address_shipment_adapter;

    private List<MultipleAddressShipmentAdapterData> addressDataList;

    private MultipleAddressPriceSummaryData priceSummaryData;

    private CartItemPromoHolderData cartItemPromoHolderData;

    private CartPromoSuggestion promoSuggestionData;

    private MultipleAddressTotalPriceHolderData totalPriceData;

    private MultipleAddressShipmentAdapterListener listener;

    private List<Object> multipleAddressShipmentItemList;

    public MultipleAddressShipmentAdapter(CartPromoSuggestion promoSuggestion,
                                          CartItemPromoHolderData promoHolderData,
                                          List<MultipleAddressShipmentAdapterData> addressDataList,
                                          MultipleAddressShipmentAdapterListener listener) {
        this.addressDataList = addressDataList;
        this.priceSummaryData = new MultipleAddressPriceSummaryData();
        this.totalPriceData = new MultipleAddressTotalPriceHolderData();
        this.listener = listener;
        multipleAddressShipmentItemList = new ArrayList<>();

        cartItemPromoHolderData = promoHolderData;

        this.promoSuggestionData = promoSuggestion;

        if (promoHolderData != null && promoHolderData.getTypePromo() != CartItemPromoHolderData.TYPE_PROMO_NOT_ACTIVE) {
            promoSuggestionData.setVisible(false);
        }

        multipleAddressShipmentItemList.add(cartItemPromoHolderData);

        multipleAddressShipmentItemList.add(promoSuggestionData);

        multipleAddressShipmentItemList.addAll(addressDataList);

        multipleAddressShipmentItemList.add(priceSummaryData);

        multipleAddressShipmentItemList.add(totalPriceData);

    }

    public void setShipmentDetailData(int position, ShipmentDetailData shipmentDetailData) {
        this.addressDataList.get(position).setSelectedShipmentDetailData(shipmentDetailData);
        calculateTemporarySubTotalForFloatingIndicator(position, shipmentDetailData);
        if (isAllShipmentChosen()) {
            listener.onAllShipmentChosen(addressDataList);
        }
    }

    private void calculateTemporarySubTotalForFloatingIndicator(int position, ShipmentDetailData shipmentDetailData) {
        this.addressDataList.get(position).setSubTotal(shipmentDetailData
                .getShipmentCartData()
                .getDeliveryPriceTotal()
                + addressDataList.get(position).getProductPriceNumber()
        );
    }

    @Override
    public int getItemViewType(int position) {
        if (multipleAddressShipmentItemList.get(position) instanceof CartPromoSuggestion)
            return CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION;
        else if (multipleAddressShipmentItemList.get(position) instanceof CartItemPromoHolderData)
            return CartVoucherPromoViewHolder.TYPE_VIEW_PROMO;
        else if (multipleAddressShipmentItemList.get(position)
                instanceof MultipleAddressShipmentAdapterData)
            return MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT;
        else if (multipleAddressShipmentItemList.get(position)
                instanceof MultipleAddressPriceSummaryData)
            return MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT;
        else if (multipleAddressShipmentItemList.get(position)
                instanceof MultipleAddressTotalPriceHolderData)
            return MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT;
        else
            return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO)
            return new CartVoucherPromoViewHolder(itemView, listener);
        else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION)
            return new CartPromoSuggestionViewHolder(itemView, listener);
        else if (viewType == MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT)
            return new MultipleShippingAddressViewHolder(itemView);
        else if (viewType == MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT)
            return new MultipleAddressShipmentFooterViewHolder(itemView);
        else if (viewType == MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT)
            return new MultipleAddressShipmentFooterTotalPayment(itemView, listener);
        else return new MultipleShippingAddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == MULTIPLE_ADDRESS_ADAPTER_SHIPMENT_LAYOUT) {
            bindItems((MultipleShippingAddressViewHolder) holder, position);
        } else if (getItemViewType(position) == CartVoucherPromoViewHolder.TYPE_VIEW_PROMO) {
            ((CartVoucherPromoViewHolder) holder).bindData(
                    (CartItemPromoHolderData) multipleAddressShipmentItemList.get(position), position
            );
        } else if (getItemViewType(position)
                == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            ((CartPromoSuggestionViewHolder) holder).bindData(
                    (CartPromoSuggestion) multipleAddressShipmentItemList.get(position), position
            );
        } else if (getItemViewType(position) == MULTIPLE_ADDRESS_FOOTER_SHIPMENT_LAYOUT) {
            bindFooterView((MultipleAddressShipmentFooterViewHolder) holder);
        } else if (getItemViewType(position) == MULTIPLE_ADDRESS_FOOTER_TOTAL_PAYMENT) {
            MultipleAddressShipmentFooterTotalPayment totalPaymentHolder =
                    (MultipleAddressShipmentFooterTotalPayment) holder;
            totalPaymentHolder.bindFooterTotalPayment(
                    addressDataList,
                    priceSummaryData,
                    totalPriceData,
                    cartItemPromoHolderData
            );
        }
    }

    private void bindFooterView(MultipleAddressShipmentFooterViewHolder footerHolder) {
        footerHolder.bindFooterView(addressDataList, priceSummaryData, cartItemPromoHolderData);
    }

    private void bindItems(MultipleShippingAddressViewHolder holder, int position) {
        MultipleAddressShipmentAdapterData data = (MultipleAddressShipmentAdapterData)
                multipleAddressShipmentItemList.get(position);
        holder.bindItems(data, listener);
    }

    public void setPickupPoint(Store store, int position) {
        addressDataList.get(position).setStore(store);
    }

    public void unSetPickupPoint(int position) {
        addressDataList.get(position).setStore(null);
    }

    public List<MultipleAddressShipmentAdapterData> getAddressDataList() {
        return addressDataList;
    }

    public MultipleAddressPriceSummaryData getPriceSummaryData() {
        return priceSummaryData;
    }

    @Override
    public int getItemCount() {
        return multipleAddressShipmentItemList.size();
    }

    private String formatPrice(long unformattedPrice) {
        Locale locale = new Locale("in", "ID");
        NumberFormat rupiahCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return rupiahCurrencyFormat.format(unformattedPrice);
    }

    public String getTotalPayment() {
        return formatPrice(calculateTotalPayment());
    }

    private boolean isShipmentDataInitiated(MultipleAddressShipmentAdapterData data) {
        return data.getSelectedShipmentDetailData() != null
                &&
                data.getSelectedShipmentDetailData().getShipmentCartData() != null;
    }

    private boolean isAllShipmentChosen() {
        boolean allFilled = true;
        for (int i = 0; i < addressDataList.size(); i++) {
            if (!isShipmentDataInitiated(addressDataList.get(i))) {
                allFilled = false;
            }
        }
        return allFilled;
    }

    public void showPromoSuggestion() {
        promoSuggestionData.setVisible(true);
        cartItemPromoHolderData.setPromoNotActive();
    }

    public void hidePromoSuggestion() {
        promoSuggestionData.setVisible(false);
    }

    public void setPromo(CartItemPromoHolderData promo) {
        cartItemPromoHolderData = promo;
        hidePromoSuggestion();
    }

    public CartItemPromoHolderData getAppliedPromo() {
        return cartItemPromoHolderData;
    }

    private long calculateTotalPayment() {
        long totalPayment = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            totalPayment = totalPayment + addressDataList.get(i).getSubTotal();
        }
        return totalPayment - getDiscountData(cartItemPromoHolderData);
    }

    private long getDiscountData(CartItemPromoHolderData promoHolderData) {
        if (promoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON)
            return promoHolderData.getCouponDiscountAmount();
        else if (promoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER)
            return promoHolderData.getVoucherDiscountAmount();
        else return 0;
    }

    public interface MultipleAddressShipmentAdapterListener extends CartAdapterActionListener {

        void onChooseShipment(MultipleAddressShipmentAdapterData addressAdapterData, int position);

        void onAllShipmentChosen(List<MultipleAddressShipmentAdapterData> adapterDataList);

        void onChoosePickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position);

        void onClearPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position);

        void onEditPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position);

        void onShowPromo(String promoMessage);

        void onRemovePromo();
    }
}
