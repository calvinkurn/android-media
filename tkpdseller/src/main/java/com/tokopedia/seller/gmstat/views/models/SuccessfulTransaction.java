package com.tokopedia.seller.gmstat.views.models;

import com.tokopedia.seller.gmstat.utils.KMNumbers;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class SuccessfulTransaction extends CommonGMModel {
    public static final int TYPE = 1282912;

    /**
     * convert long to K format ( not suitable for million )
     *
     * @param successTrans
     */
    public SuccessfulTransaction(long successTrans) {
        if (successTrans < 1_000_000) {
            Locale locale = new Locale("in", "ID");
            NumberFormat currencyFormatter = NumberFormat.getNumberInstance(locale);
            text = (currencyFormatter.format(successTrans));
        } else if (successTrans >= 1_000_000) {
            text = KMNumbers.formatNumbers(successTrans);
        }
        textDescription = "Transaksi Sukses";
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
