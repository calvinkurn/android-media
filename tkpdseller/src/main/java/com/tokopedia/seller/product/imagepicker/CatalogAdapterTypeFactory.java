package com.tokopedia.seller.product.imagepicker;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogAdapterTypeFactory extends BaseAdapterTypeFactory {
    public int type(CatalogModelView catalogModelView) {
        return CatalogImageViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == CatalogImageViewHolder.LAYOUT){
            return new CatalogImageViewHolder(parent);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}
