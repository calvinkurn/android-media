package com.tokopedia.shop.common.util;

import com.tokopedia.abstraction.common.data.model.response.PagingList;

/**
 * Created by nathan on 2/25/18.
 */

public class PagingListUtils {

    public static boolean checkNextPage(PagingList shopProductList) {
        if (shopProductList.getPaging() != null &&
                shopProductList.getPaging().getUriNext() != null &&
                !shopProductList.getPaging().getUriNext().isEmpty() &&
                !shopProductList.getPaging().getUriNext().equals("0")) {
            return true;
        } else {
            return true;
        }
    }
}
