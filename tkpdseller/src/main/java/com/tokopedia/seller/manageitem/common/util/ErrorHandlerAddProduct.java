package com.tokopedia.seller.manageitem.common.util;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 7/18/18.
 */

public class ErrorHandlerAddProduct {

    public static String getErrorMessage(Context context, Throwable e) {
        if (e instanceof ImageUploadErrorException) {
            return context.getString(R.string.product_label_message_error_upload_image);
        }else{
            return ErrorHandler.getErrorMessage(context, e);
        }
    }
}
