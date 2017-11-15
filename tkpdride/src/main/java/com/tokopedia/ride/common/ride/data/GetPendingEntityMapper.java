package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.GetPendingEntity;
import com.tokopedia.ride.common.ride.domain.model.GetPending;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Vishal on 11/10/17.
 */

public class GetPendingEntityMapper {


    public GetPendingEntityMapper() {
    }

    public GetPending transform(GetPendingEntity entity) {
        GetPending getPending = null;

        if (entity != null) {
            getPending = new GetPending();
            getPending.setCcActiveToken(entity.getCcActiveToken());
            getPending.setGuestId(entity.getGuestId());
            getPending.setPaymentMethod(transformPaymentMethod(entity.getPaymentMethod()));
            getPending.setPendingAmountFormatted(formatNumber(entity.getPendingAmount(), "IDR"));
            getPending.setPendingAmount(entity.getPendingAmount());
            getPending.setToken(entity.getToken());
            getPending.setUid(entity.getUid());
            getPending.setDate(entity.getDate());
            getPending.setDestinationAddressName(entity.getDestinationAddressName());
            getPending.setLastRequestId("TRIP ID: " + entity.getLastRequestId());
            getPending.setLastRidePayment(formatNumber(entity.getLastRidePayment(), "IDR"));
            getPending.setLastRidePaymentMethod(entity.getLastRidePaymentMethod());
            getPending.setPickupAddressName(entity.getPickupAddressName());
            getPending.setLastRideAmount(formatNumber(entity.getLastRideAmount(), "IDR"));
        }

        return getPending;
    }

    private String transformPaymentMethod(String paymentMethod) {
        if (paymentMethod.equalsIgnoreCase("wallet")) {
            return "TokoCash";
        }

        return "Credit Card";
    }

    private String formatNumber(float number, String currency) {
        try {
            if (currency.equalsIgnoreCase("RP")) {
                currency = "IDR";
            }

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance(currency));
            String result = "";
            if (currency.equalsIgnoreCase("IDR") || currency.equalsIgnoreCase("RP")) {
                format.setMaximumFractionDigits(0);
                result = format.format(number).replace(",", ".").replace("IDR", "Rp");
                result = formatDisplayPrice(result);
            } else {
                result = format.format(number);
            }
            return result;
        } catch (Exception ex) {
            return currency + " " + number;
        }
    }

    /**
     * This function added a space after IDR currency if not provided
     *
     * @param price
     */
    private String formatDisplayPrice(String price) {
        //format display to add space after currency
        if (price != null && price.contains("IDR") && !price.contains("IDR ")) {
            price = price.replace("IDR", "Rp ").replace(",", ".");
        }

        if (price != null && price.contains("Rp") && !price.contains("Rp ")) {
            price = price.replace("Rp", "Rp ").replace(",", ".");
        }

        return price;
    }
}
