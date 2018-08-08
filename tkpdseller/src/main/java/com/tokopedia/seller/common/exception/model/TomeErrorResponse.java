package com.tokopedia.seller.common.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.seller.common.exception.TomeException;

import java.io.IOException;

/**
 * Created by zulfikarrahman on 12/29/17.
 */

public class TomeErrorResponse extends BaseResponseError {

    @SerializedName("header")
    @Expose
    private Header header;

    @Override
    public String getErrorKey() {
        return header.getErrorCode();
    }

    @Override
    public boolean isResponseErrorValid() {
        return hasBody();
    }

    @Override
    public boolean hasBody() {
        return (header!= null && header.getMessages() != null && header.getMessages().size() > 0);
    }

    @Override
    public IOException createException() {
        return new TomeException(header.getErrorCode(), header.getMessages());
    }
}
