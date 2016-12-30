package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

/**
 * @author Angga.Prasetiyo on 07/04/2016.
 */
public interface TxSummaryPresenter {
    void getNotificationPurcase(Context context);

    void getNotificationFromNetwork(Context context);

    void onDestroyView();

}
