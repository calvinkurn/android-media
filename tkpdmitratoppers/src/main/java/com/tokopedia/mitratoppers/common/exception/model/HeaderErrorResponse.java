package com.tokopedia.mitratoppers.common.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;

/**
 * Created by zulfikarrahman on 12/29/17.
 */

public class HeaderErrorResponse extends BaseResponseError {

    @SerializedName("header")
    @Expose
    private Header header;

    @Override
    public String getErrorKey() {
        return header.getErrorCode();
    }

    @Override
    public boolean hasBody() {
        return (header!= null && header.getMessages() != null && header.getMessages().size() > 0);
    }

    @Override
    public RuntimeException createException() {
        return new MessageErrorException(header.getMessages().get(0));
    }
}
