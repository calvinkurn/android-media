package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFilterViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductFilterModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductFilterAdapterTypeFactory extends BaseAdapterTypeFactory {

    public ShopProductFilterAdapterTypeFactory() {
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        if (viewType == ShopProductFilterViewHolder.LAYOUT) {
            return new ShopProductFilterViewHolder(view);
        } else {
            return super.createViewHolder(view, viewType);
        }
    }

    public int type(ShopProductFilterModel shopProductFilterModel) {
        return ShopProductFilterViewHolder.LAYOUT;
    }
}
