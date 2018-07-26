package com.tokopedia.seller.product.edit.utils;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;

public class ProductPriceRangeUtils {
    private static final double MIN_IDR = 100;
    private static final double MAX_IDR = 100000000;
    private static final double MIN_USD = 1;
    private static final double MAX_USD = 7400;
    private static final double MAX_IDR_OS = 500000000;
    private static final double MAX_USD_OS = 37000;

    public static boolean isPriceValid(double price, @CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        double minPrice = getMinPrice(currencyType, isOfficialStore);
        double maxPrice = getMaxPrice(currencyType, isOfficialStore);
        return price >= minPrice && price <= maxPrice;
    }

    public static double getMinPrice(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                return MIN_USD;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                return MIN_IDR;
        }
    }

    public static String getMinPriceString(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                return CurrencyFormatUtil.convertPriceValue(MIN_USD, true);
            default:
            case CurrencyTypeDef.TYPE_IDR:
                return CurrencyFormatUtil.convertPriceValue(MIN_IDR, false);
        }
    }

    private static double getMaxPrice(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                if (isOfficialStore) {
                    return MAX_USD_OS;
                } else {
                    return MAX_USD;
                }
            default:
            case CurrencyTypeDef.TYPE_IDR:
                if (isOfficialStore) {
                    return MAX_IDR_OS;
                } else {
                    return MAX_IDR;
                }
        }
    }

    public static String getMaxPriceString(@CurrencyTypeDef int currencyType, boolean isOfficialStore) {
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                if (isOfficialStore) {
                    return CurrencyFormatUtil.convertPriceValue(MAX_USD_OS, true);
                } else {
                    return CurrencyFormatUtil.convertPriceValue(MAX_USD, true);
                }
            default:
            case CurrencyTypeDef.TYPE_IDR:
                if (isOfficialStore) {
                    return CurrencyFormatUtil.convertPriceValue(MAX_IDR_OS, false);
                } else {
                    return CurrencyFormatUtil.convertPriceValue(MAX_IDR, false);
                }
        }
    }
}
