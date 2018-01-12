package com.tokopedia.seller.shop.open.util;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.shop.common.exception.ShopException;

import java.util.List;


public class ShopErrorHandler {
    public static String getErrorMessage(Throwable t) {
        if (t instanceof ShopException) {
            List<String> errorList =((ShopException) t).getMessageError();
            if (errorList != null && errorList.size() != 0) {
                return errorList.get(0);
            }
        } else {
            return ErrorHandler.getErrorMessage(t);
        }
        return null;
    }
}
