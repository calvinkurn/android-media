package com.tokopedia.tkpdpdp;

import com.tokopedia.config.url.TokopediaUrl;

public class ProductDetailUrl {
    public static String WS_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static final String PATH_GET_DETAIL_PRODUCT = "v4/product/get_detail.pl";
}
