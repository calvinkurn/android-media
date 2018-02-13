package com.tokopedia.shop.address.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface ShopAddressTypeFactory extends AdapterTypeFactory {

    int type(ShopAddressViewModel shopNoteViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
