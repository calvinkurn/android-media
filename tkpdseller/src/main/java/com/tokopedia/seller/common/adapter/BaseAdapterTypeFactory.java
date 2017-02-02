package com.tokopedia.seller.common.adapter;

import android.support.annotation.CallSuper;
import android.view.View;

import com.tokopedia.seller.common.adapter.exception.TypeNotSupportedException;
import com.tokopedia.seller.common.adapter.model.EmptyModel;
import com.tokopedia.seller.common.adapter.model.LoadingModel;
import com.tokopedia.seller.common.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.seller.common.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.seller.common.adapter.viewholders.LoadingViewholder;


/**
 * @author Kulomady on 1/25/17.
 */

public class BaseAdapterTypeFactory implements AdapterTypeFactory {


    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewholder.LAYOUT;
    }

    @CallSuper
    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder creatViewHolder;
        if (type == EmptyViewHolder.LAYOUT) {
            creatViewHolder = new EmptyViewHolder(parent);
        } else if (type == LoadingViewholder.LAYOUT) {
            creatViewHolder = new LoadingViewholder(parent);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
        return creatViewHolder;
    }


}
