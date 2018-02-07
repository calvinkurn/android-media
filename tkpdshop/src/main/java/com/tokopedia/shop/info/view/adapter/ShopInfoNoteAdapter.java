package com.tokopedia.shop.info.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopInfoNoteAdapter extends BaseListAdapter<Visitable, ShopNoteTypeFactory> {

    public ShopInfoNoteAdapter(ShopNoteTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}