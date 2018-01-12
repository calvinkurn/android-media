package com.tokopedia.seller.shop.common.exception.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.retrofit.response.BaseResponseError;
import com.tokopedia.seller.shop.common.exception.ShopException;

import java.io.IOException;

/**
 * Created by zulfikarrahman on 12/29/17.
 */

public class ShopErrorResponse extends BaseResponseError {

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
        return new ShopException(header.getErrorCode(), header.getMessages());
    }
}
