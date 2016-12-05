package com.tokopedia.transaction.cart.listener;

import android.app.Fragment;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartActionFragment {

    void replaceFragmentWithBackStack(Fragment fragment);

    void onTopPaySuccess(String paymentId, String message);

    void onTopPayFailed(String message);

    void onTopPayCanceled(String message);
}
