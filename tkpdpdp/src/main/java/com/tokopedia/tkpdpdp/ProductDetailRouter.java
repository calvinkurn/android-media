package com.tokopedia.tkpdpdp;

import android.content.Context;
import android.content.Intent;

public interface ProductDetailRouter {
    Intent getProductReputationIntent(Context context, String productId, String productName);

    String getDeviceId(Context context);
}
