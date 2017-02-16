package com.tokopedia.seller;

import android.content.Context;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {
    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);
}
