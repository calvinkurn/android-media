package com.tokopedia.inbox.attachinvoice.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.inbox.attachinvoice.view.model.InvoiceViewModel;

import java.util.List;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceListAdapter extends BaseListAdapter<InvoiceViewModel,AttachInvoiceListAdapterTypeFactory> {
    public AttachInvoiceListAdapter(AttachInvoiceListAdapterTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}
