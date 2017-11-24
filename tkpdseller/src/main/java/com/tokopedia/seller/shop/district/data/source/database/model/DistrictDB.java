package com.tokopedia.seller.shop.district.data.source.database.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by nathan on 10/23/17.
 */

public class DistrictDB implements ItemType {

    private static final int TYPE = 1;

    @Override
    public int getType() {
        return TYPE;
    }
}
