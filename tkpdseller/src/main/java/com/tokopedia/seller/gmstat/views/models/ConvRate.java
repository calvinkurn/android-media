package com.tokopedia.seller.gmstat.views.models;

import com.tokopedia.seller.gmstat.utils.KMNumbers;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class ConvRate extends SuccessfulTransaction{

    public static final int TYPE = 121121291;
    private double convRate;

    /**
     * @param convRate multiple with 100 to get the percentage
     */
    public ConvRate(double convRate){
        super(0);
        type = TYPE;
        this.convRate = convRate*100;

        text = KMNumbers.formatString(this.convRate) + "%";

//            NumberFormat formatter = new DecimalFormat("#0.00");
//            text = formatter.format(convRate)+"%";

        //[START] obsolete things
//            text = convRate+"%";
        //[END] obsolete things
        textDescription = "Tingkat Konversi";
    }

    private ConvRate(long successTrans) {
        super(successTrans);
    }
}
