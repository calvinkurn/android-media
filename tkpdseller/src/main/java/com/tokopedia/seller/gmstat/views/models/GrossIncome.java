package com.tokopedia.seller.gmstat.views.models;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GrossIncome extends SuccessfulTransaction{
    public static final int TYPE = 1219281;

    public GrossIncome(long successTrans) {
        super(successTrans);
        type = TYPE;
        text = "Rp "+text;
        textDescription="Pendapatan Kotor";
    }
}
