package com.tokopedia.seller.seller.info.view.util;

import com.tokopedia.seller.common.williamchart.util.GoldMerchantDateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoDateUtil {

    static final SimpleDateFormat sdf =
            new SimpleDateFormat(GoldMerchantDateUtils.YYYY_M_MDD, new Locale("in", "ID"));
    private String[] monthNames;

    public SellerInfoDateUtil(String[] monthNames){
        this.monthNames = monthNames;
    }

    public String fromUnixTime(long date){
        int conv = Integer.valueOf(sdf.format(new Date(date*1000)));
        return GoldMerchantDateUtils.getDateWithoutYear(conv, monthNames);
    }
}
