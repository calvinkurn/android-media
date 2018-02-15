package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory implements ShopProductTypeFactory {

    @Override
    public int type(ShopProductViewModel shopNoteViewModel) {
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopProductViewHolder.LAYOUT) {
            viewHolder = new ShopProductViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
