package com.tokopedia.seller.shop.open.util;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.common.exception.TomeException;

import java.util.List;


public class ShopErrorHandler {
    public static String getErrorMessage(Context context, Throwable t) {
        if (t instanceof TomeException) {
            List<String> errorList = ((TomeException) t).getMessageError();
            if (errorList != null && errorList.size() != 0) {
                return errorList.get(0);
            }
        } else {
            return ErrorHandler.getErrorMessage(t, context);
        }
        return null;
    }

    public static String getGeneratedErrorMessage(char[] errorMessage, String... additionalMessages) {
        if (additionalMessages == null || additionalMessages.length < 1) {
            return new String(errorMessage);
        }
        String additionalError = "";
        for (String additionalMessage : additionalMessages) {
            if (!TextUtils.isEmpty(additionalError)) {
                additionalError += " - '";
            }
            if (!TextUtils.isEmpty(additionalMessage)) {
                additionalError += additionalMessage;
            }
            additionalError += "'";
        }
        return new String(errorMessage) + " :: " + additionalError;
    }
}
