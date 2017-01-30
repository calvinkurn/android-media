package com.tokopedia.seller.gmstat.views.models;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class ProdSold extends SuccessfulTransaction {
    public static final int TYPE = 1219231;

    public ProdSold(long successTrans) {
        super(successTrans);
        type = TYPE;
        textDescription = "Produk Terjual";
    }
}
