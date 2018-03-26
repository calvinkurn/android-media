package com.tokopedia.inbox.attachinvoice.data.repository;

import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public interface AttachInvoicesRepository {
    Observable<List<Invoice>> getUserInvoices(Map<String,String> params);
}
