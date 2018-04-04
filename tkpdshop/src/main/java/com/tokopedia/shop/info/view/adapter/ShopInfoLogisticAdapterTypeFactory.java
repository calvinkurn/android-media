package com.tokopedia.shop.info.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.info.view.adapter.viewholder.ShopInfoLogisticViewHolder;
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel;

/**
 * @author by alvarisi on 12/21/17.
 */

public class ShopInfoLogisticAdapterTypeFactory extends BaseAdapterTypeFactory implements ShopInfoLogisticTypeFactory {

    @Override
    public int type(ShopInfoLogisticViewModel viewModel) {
        return ShopInfoLogisticViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopInfoLogisticViewHolder.LAYOUT) {
            return new ShopInfoLogisticViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
