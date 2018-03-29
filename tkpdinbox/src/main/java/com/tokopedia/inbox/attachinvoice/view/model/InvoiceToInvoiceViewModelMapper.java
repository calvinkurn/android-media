package com.tokopedia.inbox.attachinvoice.view.model;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 22/03/18.
 */

public class InvoiceToInvoiceViewModelMapper implements Func1<List<Invoice>, List<InvoiceViewModel>> {
    @Override
    public List<InvoiceViewModel> call(List<Invoice> invoices) {
        ArrayList<InvoiceViewModel> invoiceViewModels = new ArrayList<>();
        for(Invoice invoice:invoices){
            String invoiceNumber = invoice.getNumber();
            String productTopName = invoice.getTitle();
            String productTopImage = invoice.getImageUrl();
            String status = invoice.getStatus();
            String date = invoice.getDate();
            String total = invoice.getTotal();
            String productCountDisplay = invoice.getDesc();
            String invoiceType = invoice.getType();
            String description = invoice.getDesc();

            invoiceViewModels.add(new InvoiceViewModel(
                    invoice.getInvoiceId(),
                    invoice.getInvoiceTypeInt(),
                    invoice.getStatusInt(),
                    invoiceNumber,
                    productTopName,
                    productTopImage,
                    status,
                    date,
                    total,
                    invoiceType,
                    description,
                    invoice.getUrl()
            ));
        }
        return invoiceViewModels;
    }
}
