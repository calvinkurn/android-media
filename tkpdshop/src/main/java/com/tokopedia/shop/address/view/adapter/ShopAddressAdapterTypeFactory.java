package com.tokopedia.shop.address.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.address.view.adapter.viewholder.ShopAddressViewHolder;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopAddressAdapterTypeFactory extends BaseAdapterTypeFactory implements ShopAddressTypeFactory {

    @Override
    public int type(ShopAddressViewModel shopNoteViewModel) {
        return ShopAddressViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopAddressViewHolder.LAYOUT) {
            viewHolder = new ShopAddressViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
