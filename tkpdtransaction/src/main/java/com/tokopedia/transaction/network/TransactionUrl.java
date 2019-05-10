package com.tokopedia.transaction.network;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * Created by kris on 3/29/18. Tokopedia
 */

public class TransactionUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getAPI();

    public static String CART_PROMO = "cart/v2/auto_applied_kupon/";

    public static final String PATH_CLEAR_PROMO = "clear";

}
