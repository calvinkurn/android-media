package com.tokopedia.ride.common.ride.data;

import com.tokopedia.ride.common.configuration.PaymentMode;
import com.tokopedia.ride.common.ride.data.entity.ReceiptEntity;
import com.tokopedia.ride.common.ride.data.entity.TipListEntity;
import com.tokopedia.ride.common.ride.domain.model.Receipt;
import com.tokopedia.ride.common.ride.domain.model.TipList;

import java.text.NumberFormat;
import java.util.ArrayList;
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
            receipt.setCurrency(transformCurrency(entity.getCurrencyCode()));
            receipt.setDistance(entity.getDistance());
            receipt.setDistanceUnit(entity.getDistanceLabel());
            receipt.setDuration(entity.getDuration());
            receipt.setDuratuinInMinute(transformDurationToMinute(entity.getDuration()));
            receipt.setPendingPayment(pendingPaymentEntityMapper.transform(entity.getPendingPayment()));
            receipt.setRequestId(entity.getRequestId());
            receipt.setSubtotal(entity.getSubtotal());
            receipt.setTotalFare(formatDisplayPrice(entity.getTotalFare()));
            receipt.setTotalOwe(entity.getTotalOwe());

            String totalCharged = entity.getCurrencyCode() + " 0";
            if (entity.getPayment() != null) {
                totalCharged = formatNumber(entity.getPayment().getTotalAmount(), entity.getPayment().getCurrencyCode());
                receipt.setPaymentMethod(transformPaymentMethod(entity.getPayment().getPaymentMethod()));
            }
            receipt.setTotalCharged(totalCharged);

            receipt.setCashback(entity.getCashbackAmount());
            receipt.setDiscount(entity.getDiscountAmount());
            receipt.setCashbackDisplayFormat(formatNumber(entity.getCashbackAmount(), entity.getCurrencyCode()));
            receipt.setDiscountDisplayFormat(formatNumber(entity.getDiscountAmount(), entity.getCurrencyCode()));
            receipt.setTipList(transformTipList(entity.getTipList()));


            if (entity.getRideOffer() != null) {
                receipt.setUberSignupUrl(entity.getRideOffer().getUrl());
                receipt.setUberSignupText(entity.getRideOffer().getHtml());
                receipt.setUberSignupTermsUrl(entity.getRideOffer().getTerms());
            }
        }
        return receipt;
    }

    private TipList transformTipList(TipListEntity entity) {
        TipList tipList = null;
        if (entity != null) {
            tipList = new TipList();
            tipList.setEnabled(entity.getEnabled());
            tipList.setList(entity.getList());

            if (entity.getList() != null) {
                ArrayList<String> formattedTipList = new ArrayList<>();
                for (Integer tipAmount : entity.getList()) {
                    formattedTipList.add(formatNumber(tipAmount, "IDR"));
                }

                tipList.setFormattedCurrecyList(formattedTipList);
            }
        }
        return tipList;
    }

    private int transformDurationToMinute(String input) {
        try {
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
        } catch (Exception ex) {
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
            price = price.replace("IDR", "Rp ").replace(",", ".");
        }

        if (price != null && price.contains("Rp") && !price.contains("Rp ")) {
            price = price.replace("Rp", "Rp ").replace(",", ".");
        }

        return price;
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

    public String transformCurrency(String currencyCode) {
        if (currencyCode != null && currencyCode.equalsIgnoreCase("IDR")) {
            return "Rp";
        }

        return currencyCode;
    }

    private String transformPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && paymentMethod.equalsIgnoreCase(PaymentMode.CC)) {
            return PaymentMode.CC_DISPLAY_NAME;
        } else if (paymentMethod != null && paymentMethod.equalsIgnoreCase(PaymentMode.WALLET)) {
            return PaymentMode.WALLET_DISPLAY_NAME;
        }

        return PaymentMode.DEFAULT_DISPLAY_NAME;
    }
}
