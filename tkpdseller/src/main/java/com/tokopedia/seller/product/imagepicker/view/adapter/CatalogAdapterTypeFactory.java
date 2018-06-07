package com.tokopedia.seller.product.imagepicker.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.seller.product.imagepicker.view.model.CatalogModelView;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogAdapterTypeFactory extends BaseAdapterTypeFactory {

    private int imageResize;

    public CatalogAdapterTypeFactory(int imageResize) {
        this.imageResize = imageResize;
    }

    public int type(CatalogModelView catalogModelView) {
        return CatalogImageViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == CatalogImageViewHolder.LAYOUT){
            return new CatalogImageViewHolder(parent, imageResize);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}
