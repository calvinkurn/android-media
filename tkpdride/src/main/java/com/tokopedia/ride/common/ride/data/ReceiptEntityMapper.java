package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.completetrip.domain.model.Receipt;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by alvarisi on 3/31/17.
 */

public class ReceiptEntityMapper {
    private PendingPaymentEntityMapper pendingPaymentEntityMapper;

    public ReceiptEntityMapper() {
        pendingPaymentEntityMapper = new PendingPaymentEntityMapper();
    }

    public Receipt transform(ReceiptEntity entity) {
        Receipt receipt = null;
        if (entity != null) {
            receipt = new Receipt();
            receipt.setCurrency(entity.getCurrencyCode());
            receipt.setDistance(entity.getDistance());
            receipt.setDistanceUnit(entity.getDistanceLabel());
            receipt.setDuration(entity.getDuration());
            receipt.setDuratuinInMinute(transformDurationToMinute(entity.getDuration()));
            receipt.setPendingPayment(pendingPaymentEntityMapper.transform(entity.getPendingPayment()));
            receipt.setRequestId(entity.getRequestId());
            receipt.setSubtotal(entity.getSubtotal());
            receipt.setTotalFare(formatNumber(Float.parseFloat(entity.getTotalFare()), entity.getPayment().getCurrencyCode()));
            receipt.setTotalOwe(entity.getTotalOwe());

            String totalCharged = entity.getCurrencyCode() + " 0";
            if (entity.getPayment() != null) {
                totalCharged = formatNumber(entity.getPayment().getTotalAmount(), entity.getPayment().getCurrencyCode());
            }

            receipt.setCashback(entity.getCashbackAmount());
            receipt.setDiscount(entity.getDiscountAmount());
            receipt.setCashbackDisplayFormat(formatNumber(entity.getCashbackAmount(), entity.getCurrencyCode()));
            receipt.setDiscountDisplayFormat(formatNumber(entity.getDiscountAmount(), entity.getCurrencyCode()));

            receipt.setTotalCharged(totalCharged);

            if (entity.getRideOffer() != null) {
                receipt.setUberSignupUrl(entity.getRideOffer().getUrl());
                receipt.setUberSignupText(entity.getRideOffer().getHtml());
                receipt.setUberSignupTermsUrl(entity.getRideOffer().getTerms());
            }
        }
        return receipt;
    }

    private int transformDurationToMinute(String input) {
        String[] parts = input.split(":");
        if (parts.length == 3) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);
            return hours * 60 + minutes + Math.abs(seconds / 60);
        } else if (parts.length == 2) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        } else {
            return 0;
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
            price = price.replace("IDR", "IDR ");
            price.replace(",", ".");
        }

        if (price != null && price.contains("Rp") && !price.contains("Rp ")) {
            price = price.replace("Rp", "Rp ");
            price.replace(",", ".");
        }

        return price;
    }

    private String formatNumber(float number, String currency) {
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance("IDR"));
            String result = "";
            if (currency.equalsIgnoreCase("IDR") || currency.equalsIgnoreCase("RP")) {
                format.setMaximumFractionDigits(0);
                result = format.format(number).replace(",", ".");
            } else {
                result = format.format(number);
            }
            return result;
        } catch (Exception ex) {
            return currency + " " + number;
        }
    }
}
