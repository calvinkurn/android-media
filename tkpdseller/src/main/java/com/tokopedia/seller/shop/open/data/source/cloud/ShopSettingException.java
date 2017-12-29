package com.tokopedia.seller.shop.open.data.source.cloud;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by zulfikarrahman on 12/27/17.
 */

public class ShopSettingException extends Throwable {
    public ShopSettingException() {
        super();
    }

    public ShopSettingException(String errorListMessage) {
        super(errorListMessage);
    }

    public ShopSettingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShopSettingException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected ShopSettingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
