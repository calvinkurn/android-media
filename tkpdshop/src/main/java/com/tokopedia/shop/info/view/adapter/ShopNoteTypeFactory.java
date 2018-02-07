package com.tokopedia.shop.info.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.info.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.info.view.model.ShopNoteViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface ShopNoteTypeFactory extends AdapterTypeFactory {

    int type(ShopNoteViewModel shopNoteViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
