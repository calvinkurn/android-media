package com.tokopedia.seller.product.common.utils;

import android.text.TextUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by nathan on 2/28/18.
 */

public class UrlUtils {

    public static boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            String scheme = uri.getScheme();
            return !TextUtils.isEmpty(scheme) && (scheme.equals("http") || scheme.equals("https"));
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
