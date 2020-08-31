package com.tokopedia.seller.manageitem.common.util;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.network.data.model.response.ResponseV4ErrorException;

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
            return context.getString(com.tokopedia.seller.R.string.msg_no_connection);
        } else if (t instanceof SocketTimeoutException) {
            return context.getString(com.tokopedia.seller.R.string.default_request_error_timeout);
        } else if (t instanceof IOException) {
            return context.getString(com.tokopedia.seller.R.string.default_request_error_internal_server);
        } else {
            return context.getString(com.tokopedia.seller.R.string.default_request_error_unknown);
        }
    }

}