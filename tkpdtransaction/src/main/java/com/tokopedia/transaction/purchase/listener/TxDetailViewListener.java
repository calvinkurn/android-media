package com.tokopedia.transaction.purchase.listener;

import android.content.Intent;

import com.tokopedia.core.product.listener.ViewListener;

/**
 * @author Angga.Prasetiyo on 28/04/2016.
 */
public interface TxDetailViewListener extends ViewListener {

    void closeWithResult(int requestCode, Intent data);

    void renderSuccessRequestCancelOrder(String message);
}
