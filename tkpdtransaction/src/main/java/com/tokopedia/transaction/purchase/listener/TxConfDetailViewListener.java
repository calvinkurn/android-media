package com.tokopedia.transaction.purchase.listener;

import android.os.Bundle;

import com.tokopedia.transaction.base.IBaseView;

/**
 * @author Angga.Prasetiyo on 15/06/2016.
 */
public interface TxConfDetailViewListener extends IBaseView {

    void setResultActivity(int resultCode, Bundle bundle);
}
