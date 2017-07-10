package com.tokopedia.seller.gmstat.views.models;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class ProdSeen extends SuccessfulTransaction {
    public static final int TYPE = 121231;

    public ProdSeen(long successTrans) {
        super(successTrans);
        textDescription = "Produk Dilihat";
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
