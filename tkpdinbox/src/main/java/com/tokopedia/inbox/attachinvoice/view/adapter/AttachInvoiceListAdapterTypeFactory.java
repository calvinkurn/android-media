package com.tokopedia.inbox.attachinvoice.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;
import com.tokopedia.inbox.attachinvoice.view.viewholder.InvoiceViewHolder;
import com.tokopedia.inbox.attachproduct.view.viewholder.AttachProductEmptyResultViewHolder;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceListAdapterTypeFactory extends BaseAdapterTypeFactory {


    public int type(InvoiceViewModel viewModel) {
        return InvoiceViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == InvoiceViewHolder.LAYOUT) {
            return new InvoiceViewHolder(parent);
        } else if (type == EmptyResultViewHolder.LAYOUT) {
            return new AttachProductEmptyResultViewHolder(parent);
        } else if (type == ErrorNetworkViewHolder.LAYOUT) {
            return new ErrorNetworkViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
