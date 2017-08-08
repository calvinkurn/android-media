package com.tokopedia.seller.product.edit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorListStringException;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ViewUtils {

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
        if (t instanceof ResponseErrorListStringException) {
            return ((ResponseErrorListStringException) t).getErrorList().get(0);
        } else if (t instanceof ResponseErrorException) {
            return getErrorMessage(t);
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

    public static Pair<Double, Double> minMaxPrice(Context context, int currencyType) {
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
        String minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_rp));
        String maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_rp));
        if (spinnerValue.equalsIgnoreCase(context.getString(R.string.product_currency_value_usd))) {
            minPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_minimum_price_usd));
            maxPriceString = CurrencyFormatHelper.removeCurrencyPrefix(context.getString(R.string.product_maximum_price_usd));
        }
        double minPrice = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(minPriceString));
        double maxPrice = Double.parseDouble(CurrencyFormatHelper.RemoveNonNumeric(maxPriceString));
        return Pair.create(minPrice, maxPrice);
    }
}