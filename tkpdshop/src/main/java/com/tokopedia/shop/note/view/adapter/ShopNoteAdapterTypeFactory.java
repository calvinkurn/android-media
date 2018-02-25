package com.tokopedia.shop.note.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopNoteAdapterTypeFactory extends BaseAdapterTypeFactory {

    public int type(ShopNoteViewModel shopNoteViewModel) {
        return ShopNoteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopNoteViewHolder.LAYOUT) {
            viewHolder = new ShopNoteViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
