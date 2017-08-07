package com.tokopedia.ride.common.ride.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alvarisi on 7/21/17.
 */

public class RideUtils {
    private static final String TIME_SERVER_FORMAT = "dd MMM, yyyy HH:mm";
    private static final String TIME_VIEW_FORMAT = "dd MMM yyyy HH:mm";

    private static final String IND_CURRENCY = "IDR";
    private static final String IND_LOCAL_CURRENCY = "Rp";


    public static String convertTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_SERVER_FORMAT, Locale.US);
        Date dt;
        try {
            dt = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
        SimpleDateFormat outputFormatter = new SimpleDateFormat(TIME_VIEW_FORMAT, Locale.US);
        return outputFormatter.format(dt);
    }

    public static String convertPriceValueToIdrFormat(int price){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol(IND_LOCAL_CURRENCY + " ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String result = kursIndonesia.format(price);

        return result.replace(",", ".");
    }



    public static String formatStringToPriceString(String numberString, String currency) {
        try {
            if (currency.equalsIgnoreCase(IND_CURRENCY) || currency.equalsIgnoreCase(IND_LOCAL_CURRENCY)) {
                return RideUtils.convertPriceValueToIdrFormat(Integer.parseInt(numberString));
            } else {
                return formaNumberToPriceString(Float.parseFloat(numberString), currency);
            }
        } catch (Exception ex) {
            return currency + " " + numberString;
        }
    }

    private static String formaNumberToPriceString(float number, String currency) {
        try {
            if (currency.equalsIgnoreCase(IND_LOCAL_CURRENCY)) {
                currency = IND_CURRENCY;
            }

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance(currency));
            String result = "";
            if (currency.equalsIgnoreCase(IND_CURRENCY) || currency.equalsIgnoreCase(IND_LOCAL_CURRENCY)) {
                format.setMaximumFractionDigits(0);
                result = format.format(number).replace(",", ".").replace(IND_CURRENCY, IND_LOCAL_CURRENCY + " ");
            } else {
                result = format.format(number);
            }
            return result;
        } catch (Exception ex) {
            return currency + " " + number;
        }
    }
}
