package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public interface OrderHistoryPresenter {

    void fetchHistoryData(Context context, String orderId, int userMode);

    void onDestroy();

}
