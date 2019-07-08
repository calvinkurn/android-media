package com.tokopedia.transaction.orders.orderlist.common;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexValidator {

    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validate(final String hex) {
        if (TextUtils.isEmpty(hex))
            return false;
        Pattern pattern = Pattern.compile(HEX_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

}