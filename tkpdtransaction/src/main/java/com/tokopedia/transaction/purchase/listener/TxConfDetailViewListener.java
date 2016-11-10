package com.tokopedia.transaction.purchase.listener;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.product.listener.ViewListener;

/**
 * TxConfDetailViewListener
 * Created by Angga.Prasetiyo on 15/06/2016.
 */
public interface TxConfDetailViewListener extends ViewListener {
    void setResult(int resultCode, Intent data);

    void setResultActivity(int resultCode, Bundle bundle);
}
