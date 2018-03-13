package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentCartData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by kris on 3/7/18. Tokopedia
 */

public class MultipleAddressShipmentFooterViewHolder extends RecyclerView.ViewHolder {

    private TextView quantityTotal;

    private TextView totalProductPrice;

    private TextView totalShippingPrice;

    private TextView insurancePrice;

    private TextView additionalFee;

    private TextView promoDiscount;

    public MultipleAddressShipmentFooterViewHolder(View itemView) {

        super(itemView);

        quantityTotal = itemView.findViewById(R.id.qty_total);

        totalProductPrice = itemView.findViewById(R.id.total_product_price);

        totalShippingPrice = itemView.findViewById(R.id.total_shipping_price);

        insurancePrice = itemView.findViewById(R.id.insurance_price);

        additionalFee = itemView.findViewById(R.id.additional_fee);

        promoDiscount = itemView.findViewById(R.id.promo_discount);

    }

    public void bindFooterView(List<MultipleAddressShipmentAdapterData> addressDataList,
                               MultipleAddressPriceSummaryData priceSummaryData,
                               CartItemPromoHolderData promoHolderData) {

        priceSummaryData.setAdditionalFee(calculateAdditionalFee(addressDataList));
        priceSummaryData.setTotalProductPrice(calculateTotalProductCost(addressDataList));
        priceSummaryData.setInsurancePrice(calculateInsuranceCost(addressDataList));
        priceSummaryData.setTotalShippingPrice(calculateTotalShippingCost(addressDataList));
        priceSummaryData.setQuantity(calculateQuantity(addressDataList));
        quantityTotal.setText(quantityTotal.getText()
                .toString()
                .replace("#", priceSummaryData.getQuantityText()));
        totalProductPrice.setText(
                formatPrice(priceSummaryData.getTotalProductPrice())
        );
        totalShippingPrice.setText(priceChecker(
                priceSummaryData.getTotalShippingPrice(),
                priceSummaryData.getTotalShippingPrice())
        );
        insurancePrice.setText(priceChecker(
                priceSummaryData.getInsurancePrice(),
                priceSummaryData.getTotalShippingPrice())
        );
        additionalFee.setText(priceChecker(
                priceSummaryData.getAdditionalFee(),
                priceSummaryData.getTotalShippingPrice())
        );
        promoDiscount.setText(priceChecker(
                getDiscountData(promoHolderData),
                priceSummaryData.getTotalShippingPrice())
        );

    }

    private long getDiscountData(CartItemPromoHolderData promoHolderData) {
        if (promoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON)
            return promoHolderData.getCouponDiscountAmount();
        else if (promoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER)
            return promoHolderData.getVoucherDiscountAmount();
        else return 0;
    }

    private String formatPrice(long unformattedPrice) {
        Locale locale = new Locale("in", "ID");
        NumberFormat rupiahCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return rupiahCurrencyFormat.format(unformattedPrice);
    }

    private long calculateTotalProductCost(List<MultipleAddressShipmentAdapterData> addressDataList) {
        long totalProductPrice = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            if (isShipmentDataInitiated(addressDataList.get(i)))
                totalProductPrice = totalProductPrice + addressDataList.get(i).getProductPriceNumber();
        }
        return totalProductPrice;
    }

    private boolean isShipmentDataInitiated(
            MultipleAddressShipmentAdapterData data
    ) {
        return data.getSelectedShipmentDetailData() != null
                &&
                data.getSelectedShipmentDetailData().getShipmentCartData() != null;
    }

    private long calculateTotalShippingCost(
            List<MultipleAddressShipmentAdapterData> addressDataList
    ) {
        long totalShipmentPrice = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            if (isShipmentDataInitiated(addressDataList.get(i))) {
                totalShipmentPrice = totalShipmentPrice
                        + getGeneratedShipmentCartData(addressDataList.get(i))
                        .getDeliveryPriceTotal()
                        - getGeneratedShipmentCartData(addressDataList.get(i)).getInsurancePrice()
                        - getGeneratedShipmentCartData(addressDataList.get(i)).getAdditionalFee();
            }
        }
        return totalShipmentPrice;
    }

    private long calculateInsuranceCost(List<MultipleAddressShipmentAdapterData> addressDataList) {
        long totalInsuranceCost = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            if (isShipmentDataInitiated(addressDataList.get(i))) {
                totalInsuranceCost = totalInsuranceCost +
                        getGeneratedShipmentCartData(addressDataList.get(i)).getInsurancePrice();
            }
        }
        return totalInsuranceCost;
    }

    private long calculateAdditionalFee(List<MultipleAddressShipmentAdapterData> addressDataList) {
        long totalAdditionalFee = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            if (isShipmentDataInitiated(addressDataList.get(i))) {
                totalAdditionalFee = totalAdditionalFee +
                        getGeneratedShipmentCartData(addressDataList.get(i)).getAdditionalFee();
            }
        }
        return totalAdditionalFee;
    }

    private long calculateQuantity(List<MultipleAddressShipmentAdapterData> addressDataList) {
        long quantity = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            quantity = quantity + Integer.parseInt(addressDataList.get(i)
                    .getItemData().getProductQty());
        }
        return quantity;
    }

    private String totalPriceChecker(String totalPriceText, long shipmentPrice) {
        if (shipmentPrice > 0) return totalPriceText;
        else return "-";
    }

    private String priceChecker(long price, long shipmentPrice) {
        if (shipmentPrice > 0) return formatPrice(price);
        else return "-";
    }

    private ShipmentCartData getGeneratedShipmentCartData(MultipleAddressShipmentAdapterData data) {
        return data.getSelectedShipmentDetailData().getShipmentCartData();
    }

}
