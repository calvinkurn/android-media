package com.tokopedia.seller.shopscore.data.mapper;

/**
 * Created by sebastianuskh on 3/2/17.
 */

public class ColorUtil {

    private static final int MAX_ALPHA_VALUE = 0xFF000000;

    public static Integer formatColor(String color) {
        String hexadecimal = color.replaceAll("[^A-Za-z0-9]", "");
        return Integer.parseInt(hexadecimal, 16) + MAX_ALPHA_VALUE;
    }
}
