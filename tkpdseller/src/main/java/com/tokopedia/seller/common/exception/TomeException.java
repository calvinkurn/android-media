package com.tokopedia.seller.common.exception;

import android.annotation.TargetApi;
import android.os.Build;

import com.drew.lang.StringUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/27/17.
 */

public class TomeException extends IOException {
    List<String> messageErrorList;
    String errorCode;

    public List<String> getMessageError() {
        return messageErrorList;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public TomeException(String errorCode, List<String> messages) {
        super(StringUtil.join(messages, ","));
        this.errorCode = errorCode;
        this.messageErrorList = messages;
    }

    public TomeException(String errorListMessage) {
        super(errorListMessage);
    }

    public TomeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TomeException(Throwable cause) {
        super(cause);
    }

}
