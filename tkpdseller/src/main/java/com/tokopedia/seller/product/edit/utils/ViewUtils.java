package com.tokopedia.seller.product.edit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.exception.TomeException;

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

}