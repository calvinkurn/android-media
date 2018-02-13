package com.tokopedia.shop.address.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopAddressAdapter extends BaseListAdapter<Visitable, ShopAddressTypeFactory> {

    public ShopAddressAdapter(ShopAddressTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}