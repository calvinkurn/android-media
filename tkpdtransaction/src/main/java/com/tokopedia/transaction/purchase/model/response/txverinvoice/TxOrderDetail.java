package com.tokopedia.transaction.purchase.model.response.txverinvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public class TxOrderDetail {
    private static final String TAG = TxOrderDetail.class.getSimpleName();

    @SerializedName("detail")
    @Expose
    private List<Detail> detail = new ArrayList<Detail>();
    @SerializedName("payment")
    @Expose
    private Payment payment;

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
