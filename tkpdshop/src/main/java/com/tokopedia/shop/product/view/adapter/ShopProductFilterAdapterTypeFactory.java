package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFilterSelectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFilterUnselectedViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductFilterAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final TypeFactoryListener typeFactoryListener;

    public ShopProductFilterAdapterTypeFactory(@Nullable TypeFactoryListener typeFactoryListener) {
        this.typeFactoryListener = typeFactoryListener;
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        return typeFactoryListener.getType(shopProductViewModel);
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopProductFilterUnselectedViewHolder.LAYOUT) {
            viewHolder = new ShopProductFilterUnselectedViewHolder(view);
        } else if (viewType == ShopProductFilterSelectedViewHolder.LAYOUT) {
            viewHolder = new ShopProductFilterSelectedViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }

    public interface TypeFactoryListener<E> {
        int getType(E type);
    }
}
