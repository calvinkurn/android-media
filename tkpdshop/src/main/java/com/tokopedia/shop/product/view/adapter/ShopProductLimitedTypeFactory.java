package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.model.ShopFeaturedProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface ShopProductLimitedTypeFactory extends AdapterTypeFactory {

    int type(ShopFeaturedProductViewModel shopProductViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
