package com.tokopedia.seller.product.edit.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.exception.TomeException;
import com.tokopedia.seller.product.common.utils.CurrencyUtils;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ViewUtils {

    private static final double MIN_IDR = 100;
    private static final double MAX_IDR = 100000000;
    private static final double MIN_USD = 1;
    private static final double MAX_USD = 7500;
    private static final double MAX_IDR_OS = 500000000;
    private static final double MAX_USD_OS = 37000;

    public static String getErrorMessage(Throwable t) {
        String errorMessage = null;
        if (t instanceof ResponseErrorException) {
            errorMessage = ((ResponseErrorException) t).getErrorList().get(0).getDetail();
        }
        return errorMessage;
    }

    public static String getErrorMessage(@NonNull Context context, Throwable t) {
        String errorMessage = getErrorMessage(t);
        if (TextUtils.isEmpty(errorMessage)) {
            return getGeneralErrorMessage(context, t);
        } else {
            return errorMessage;
        }
    }

    public static String getGeneralErrorMessage(@NonNull Context context, Throwable t) {
        if (t instanceof ResponseV4ErrorException) {
            return ((ResponseV4ErrorException) t).getErrorList().get(0);
        } else if (t instanceof ResponseErrorException) {
            return getErrorMessage(t);
        } else if (t instanceof TomeException) {
            return ((TomeException) t).getMessageError().get(0);
        } else if (t instanceof UnknownHostException) {
            return context.getString(R.string.msg_no_connection);
        } else if (t instanceof SocketTimeoutException) {
            return context.getString(R.string.default_request_error_timeout);
        } else if (t instanceof IOException) {
            return context.getString(R.string.default_request_error_internal_server);
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }


    /**
     * Use isPriceValid instead for better performance
     */
    @Deprecated
    public static Pair<Double, Double> minMaxPrice(Context context, int currencyType, boolean isOfficialStore) {
        String spinnerValue = null;
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                spinnerValue = Integer.toString(CurrencyTypeDef.TYPE_USD);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                spinnerValue = Integer.toString(CurrencyTypeDef.TYPE_IDR);
                break;
        }
        String minPriceString;
        String maxPriceString;
        if (isOfficialStore) {
            minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_rp));
            maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_rp_official_store));
            if (spinnerValue.equalsIgnoreCase(context.getString(R.string.product_currency_value_usd))) {
                minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_usd));
                maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_usd_official_store));
            }
        } else {
            minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_rp));
            maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_rp_general));
            if (spinnerValue.equalsIgnoreCase(context.getString(R.string.product_currency_value_usd))) {
                minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_usd));
                maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_usd_general));
            }
        }
        double minPrice = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minPriceString));
        double maxPrice = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxPriceString));
        return Pair.create(minPrice, maxPrice);
    }

    public static boolean isPriceValid(double price, @CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        double minPrice = getMinPrice(currencyType, isOfficialStore);
        double maxPrice = getMaxPrice(currencyType, isOfficialStore);
        return price >= minPrice && price <= maxPrice;
    }

    private static double getMinPrice(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                return MIN_USD;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                return MIN_IDR;
        }
    }

    public static String getMinPriceString(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                return CurrencyFormatUtil.convertPriceValue(MIN_USD, true);
            default:
            case CurrencyTypeDef.TYPE_IDR:
                return CurrencyFormatUtil.convertPriceValue(MIN_IDR, false);
        }
    }

    private static double getMaxPrice(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                if (isOfficialStore) {
                    return MAX_USD_OS;
                } else {
                    return MAX_USD;
                }
            default:
            case CurrencyTypeDef.TYPE_IDR:
                if (isOfficialStore) {
                    return MAX_IDR_OS;
                } else {
                    return MAX_IDR;
                }
        }
    }

    public static String getMaxPriceString(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                if (isOfficialStore) {
                    return CurrencyFormatUtil.convertPriceValue(MAX_USD_OS, true);
                } else {
                    return CurrencyFormatUtil.convertPriceValue(MAX_USD, true);
                }
            default:
            case CurrencyTypeDef.TYPE_IDR:
                if (isOfficialStore) {
                    return CurrencyFormatUtil.convertPriceValue(MAX_IDR_OS, false);
                } else {
                    return CurrencyFormatUtil.convertPriceValue(MAX_IDR, false);
                }
        }
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }
}