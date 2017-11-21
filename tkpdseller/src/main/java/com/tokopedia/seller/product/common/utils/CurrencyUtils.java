package com.tokopedia.seller.product.common.utils;

import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;

/**
 * Created by nathan on 10/8/17.
 */

public class CurrencyUtils {

    public static String getPriceFormatted(@CurrencyTypeDef int currencyType, String price) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                return CurrencyFormatHelper.ConvertToDollar(price);
            case CurrencyTypeDef.TYPE_IDR:
            default:
                return CurrencyFormatHelper.ConvertToRupiah(price);
        }
    }
}
