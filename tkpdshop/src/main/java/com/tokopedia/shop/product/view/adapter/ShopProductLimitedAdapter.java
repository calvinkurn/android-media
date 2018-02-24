package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductLimitedAdapter extends BaseListAdapter<Visitable, ShopProductLimitedTypeFactory> {

    public ShopProductLimitedAdapter(ShopProductLimitedTypeFactory shopProductLimitedTypeFactory) {
        super(shopProductLimitedTypeFactory);
    }
}