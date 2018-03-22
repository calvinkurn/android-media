package com.tokopedia.seller.product.common.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by nathan on 2/28/18.
 */

public class UrlUtils {

    public static boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
