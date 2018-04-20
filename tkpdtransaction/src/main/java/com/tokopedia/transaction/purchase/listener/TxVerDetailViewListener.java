package com.tokopedia.transaction.purchase.listener;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.purchase.model.response.txverinvoice.Detail;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 13/06/2016.
 */
public interface TxVerDetailViewListener extends IBaseView {
    void renderInvoiceList(List<Detail> detail);

    void setResult(int resultCode);

    void renderErrorGetInvoiceData(String message);
}
