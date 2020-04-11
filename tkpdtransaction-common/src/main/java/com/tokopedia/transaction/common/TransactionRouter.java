package com.tokopedia.transaction.common;

import android.content.Context;
import android.content.Intent;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public interface TransactionRouter {

    Intent goToOrderDetail(Context context, String orderId);

    Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName);
}
