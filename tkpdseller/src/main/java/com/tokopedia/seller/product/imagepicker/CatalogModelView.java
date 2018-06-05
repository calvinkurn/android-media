package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogModelView implements Visitable<CatalogAdapterTypeFactory> {
    @Override
    public int type(CatalogAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
