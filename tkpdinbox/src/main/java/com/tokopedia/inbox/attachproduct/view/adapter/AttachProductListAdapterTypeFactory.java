package com.tokopedia.inbox.attachproduct.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.inbox.attachproduct.view.viewholder.AttachProductEmptyResultViewHolder;
import com.tokopedia.inbox.attachproduct.view.viewholder.AttachProductListItemViewHolder;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

/**
 * Created by Hendri on 14/02/18.
 */

public class AttachProductListAdapterTypeFactory extends BaseAdapterTypeFactory
        implements BaseListCheckableTypeFactory<AttachProductItemViewModel> {
    private BaseCheckableViewHolder.CheckableInteractionListener checkableInteractionListener;

    public AttachProductListAdapterTypeFactory(BaseCheckableViewHolder.CheckableInteractionListener checkableInteractionListener) {
        this.checkableInteractionListener = checkableInteractionListener;
    }

    public int type(AttachProductItemViewModel viewModel) {
        return AttachProductListItemViewHolder.LAYOUT;
    }

    public int type(EmptyResultViewModel viewModel) {
        return AttachProductEmptyResultViewHolder.LAYOUT;
    }

    public int type(ErrorNetworkModel viewModel) {
        return ErrorNetworkViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == AttachProductListItemViewHolder.LAYOUT) {
            return new AttachProductListItemViewHolder(parent,checkableInteractionListener);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new AttachProductEmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
