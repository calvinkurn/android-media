package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductAdapter extends BaseListAdapter<Visitable, ShopProductTypeFactory> {

    public ShopProductAdapter(ShopProductTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}