package com.tokopedia.seller.shop.common.exception;

import android.annotation.TargetApi;
import android.os.Build;

import com.drew.lang.StringUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/27/17.
 */

public class ShopException extends IOException {
    List<String> messageErrorList;
    String errorCode;

    public List<String> getMessageError() {
        return messageErrorList;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public ShopException(String errorCode, List<String> messages) {
        super(StringUtil.join(messages, ","));
        this.errorCode = errorCode;
        this.messageErrorList = messages;
    }

    public ShopException(String errorListMessage) {
        super(errorListMessage);
    }

    public ShopException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShopException(Throwable cause) {
        super(cause);
    }

}
