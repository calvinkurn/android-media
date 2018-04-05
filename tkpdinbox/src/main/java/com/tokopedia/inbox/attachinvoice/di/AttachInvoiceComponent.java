package com.tokopedia.inbox.attachinvoice.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.attachinvoice.view.fragment.AttachInvoiceFragment;

import dagger.Component;

/**
 * Created by Hendri on 05/04/18.
 */

@AttachInvoiceScope
@Component(modules = {AttachInvoiceModule.class}, dependencies = AppComponent.class)
public interface AttachInvoiceComponent {
    void inject(AttachInvoiceFragment fragment);
}
