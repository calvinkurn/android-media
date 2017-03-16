package com.tokopedia.seller.shopscore.data.mapper;

/**
 * @author sebastianuskh on 3/2/17.
 */

public class ColorUtil {

    private static final int MAX_ALPHA_VALUE = 0xFF000000;

    public static Integer formatColorWithAlpha(String color) {
        return formatColor(color) + MAX_ALPHA_VALUE;
    }

    public static Integer formatColor(String color) {
        String hexadecimal = color.replaceAll("[^A-Za-z0-9]", "");
        return Integer.parseInt(hexadecimal, 16);
    }
}
