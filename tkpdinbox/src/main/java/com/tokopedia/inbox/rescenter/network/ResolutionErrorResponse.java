package com.tokopedia.inbox.rescenter.network;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.network.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.exception.Header;

import java.io.IOException;

/**
 * @author by yfsx on 26/07/18.
 */

public class ResolutionErrorResponse extends BaseResponseError {

    @SerializedName("header")
    @Expose
    private Header header;

    @Override
    public String getErrorKey() {
        return header.getErrorCode();
    }

    @Override
    public boolean hasBody() {
        return (header != null
                && header.getMessages() != null
                && header.getMessages().size() > 0
                && !TextUtils.isEmpty(header.getErrorCode()));
    }

    @Override
    public IOException createException() {
        return new ErrorNetworkException(header.getMessages().get(0), getErrorKey());
    }

}
