package com.tokopedia.transaction.purchase.listener;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.Detail;

import java.util.List;

/**
 * TxVerDetailViewListener
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public interface TxVerDetailViewListener extends ViewListener {
    void renderInvoiceList(List<Detail> detail);

    void setResult(int resultCode);

    void renderErrorGetInvoiceData(String message);
}
