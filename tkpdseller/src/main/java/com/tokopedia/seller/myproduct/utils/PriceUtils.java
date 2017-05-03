package com.tokopedia.seller.myproduct.utils;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.tokopedia.core.R;

/**
 * Created by sebastianuskh on 12/7/16.
 */

public class PriceUtils {

    public static final int CURRENCY_RUPIAH = 100;
    public static final int CURRENCY_DOLLAR = 200;
    private static final String TAG = "Price Utils";

    public static Pair<Boolean, String> validatePrice(int currency, double price, Context context){
        boolean resBoolean = false;
        String resString = null;

        try {
            switch (currency) {
                case CURRENCY_RUPIAH:
                    if(price < 100){
                        resBoolean = false;
                        resString = context.getString(R.string.addproduct_wholesale_priceShouldBigger100);
                        return new Pair<>(resBoolean, resString);}
                    if(price > 100_000_000){
                        resBoolean = false;
                        resString = context.getString(R.string.addproduct_wholesale_priceShouldLower100);
                        return new Pair<>(resBoolean, resString);}
                    if (price >= 100 && price <= 100_000_000){
                        resBoolean= true;
                        resString = null;
                        return new Pair<>(resBoolean, resString);}
                    break;
                case CURRENCY_DOLLAR:
                    if(price < 1){
                        resBoolean = false;
                        resString = context.getString(R.string.addproduct_wholesale_priceShouldBigger1Dollar);
                        return new Pair<>(resBoolean, resString);
                    }
                    if(price > 7500){
                        resBoolean = false;
                        resString = context.getString(R.string.addproduct_wholesale_priceShouldLowerDollar);
                        return new Pair<>(resBoolean, resString);
                    }
                    if(price >= 1 && price <= 7500){
                        resBoolean= true;
                        resString = null;
                        return new Pair<>(resBoolean, resString);}
                    break;
                default:
                    resBoolean = false;
                    resString = context.getString(R.string.addproduct_wholesale_currencyError);
                    return new Pair<>(resBoolean, resString);
            }
        }catch(NumberFormatException nfe){
            Log.e(TAG, nfe.getLocalizedMessage());
            resBoolean = false;
            resString = context.getString(R.string.error_problem);
            return new Pair<>(resBoolean, resString);
        }
        return null;
    }

}
