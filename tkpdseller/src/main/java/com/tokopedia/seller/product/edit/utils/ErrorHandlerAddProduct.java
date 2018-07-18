package com.tokopedia.seller.product.edit.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException;
import com.tokopedia.abstraction.common.network.constant.ResponseStatus;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.Error;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.data.exception.ImageUploadErrorException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by zulfikarrahman on 7/18/18.
 */

public class ErrorHandlerAddProduct {

    public static String getErrorMessage(Context context, Throwable e) {
        if (e instanceof ImageUploadErrorException &&
                e.getLocalizedMessage() != null &&
                !e.getLocalizedMessage().equals("") &&
                e.getLocalizedMessage().length() <= 3) {
            return context.getString(R.string.product_label_message_error_upload_image);
        }else{
            return ErrorHandler.getErrorMessage(context, e);
        }
    }
}
