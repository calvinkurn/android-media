package com.tokopedia.payment.router;

import android.content.Intent;

/**
 * @author anggaprasetiyo on 7/11/17.
 */

public interface IPaymentModuleRouter {

    String getSchemeAppLinkCartPayment();

    boolean isSupportedDelegateDeepLink(String appLinkScheme);

    Intent getIntentDeepLinkHandlerActivity();

    String getBaseUrlDomainPayment();
}
