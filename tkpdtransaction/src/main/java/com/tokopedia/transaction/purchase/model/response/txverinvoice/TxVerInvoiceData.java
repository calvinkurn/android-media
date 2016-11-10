package com.tokopedia.transaction.purchase.model.response.txverinvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public class TxVerInvoiceData {
    private static final String TAG = TxVerInvoiceData.class.getSimpleName();

    @SerializedName("tx_order_detail")
    @Expose
    private TxOrderDetail txOrderDetail;

    public TxOrderDetail getTxOrderDetail() {
        return txOrderDetail;
    }

    public void setTxOrderDetail(TxOrderDetail txOrderDetail) {
        this.txOrderDetail = txOrderDetail;
    }
}
