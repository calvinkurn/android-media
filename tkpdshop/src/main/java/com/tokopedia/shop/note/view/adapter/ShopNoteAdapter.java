package com.tokopedia.shop.note.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopNoteAdapter extends BaseListAdapter<Visitable, ShopNoteTypeFactory> {

    public ShopNoteAdapter(ShopNoteTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}