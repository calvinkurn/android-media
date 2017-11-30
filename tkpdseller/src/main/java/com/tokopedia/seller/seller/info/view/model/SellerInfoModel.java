package com.tokopedia.seller.seller.info.view.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoModel implements ItemType {
    public static final int TYPE = 1921912;

    @Override
    public int getType() {
        return TYPE;
    }
}
