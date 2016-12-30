package com.tokopedia.transaction.purchase.listener;

import android.os.Bundle;

import com.tokopedia.core.product.listener.ViewListener;

/**
 * @author Angga.Prasetiyo on 15/06/2016.
 */
public interface TxConfDetailViewListener extends ViewListener {

    void setResultActivity(int resultCode, Bundle bundle);
}
