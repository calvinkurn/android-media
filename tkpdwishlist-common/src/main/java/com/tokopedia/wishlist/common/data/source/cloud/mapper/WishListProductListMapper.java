package com.tokopedia.wishlist.common.data.source.cloud.mapper;

import java.util.List;

/**
 * Created by nathan on 2/20/18.
 */

public class WishListProductListMapper {

    public String convertCommaValue(List<String> productIdList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < productIdList.size(); i++) {
            stringBuilder.append(productIdList.get(i));
            if (i != productIdList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
