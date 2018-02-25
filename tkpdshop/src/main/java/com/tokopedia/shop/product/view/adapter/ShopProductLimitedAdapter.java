package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductLimitedAdapter extends BaseListAdapter<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory> {

    public ShopProductLimitedAdapter(ShopProductLimitedAdapterTypeFactory shopProductLimitedTypeFactory) {
        super(shopProductLimitedTypeFactory);
    }

}
