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
            String productTopName = "";
            String productTopImage = "";
            String status = invoice.getStatus();
            String date = invoice.getDate();
            String total = invoice.getTotal();
            String productCountDisplay = "";
            String invoiceType = "";
            String description = "";

            if(invoice.getProducts().size() > 0){
                productTopImage = invoice.getProducts().get(0).getThumbnailUrl();
                productTopName = invoice.getProducts().get(0).getName();
                if(invoice.getProducts().size() > 1){
                    description = "+"+String.valueOf(invoice.getProducts().size() - 1)+" barang lainnya";
                }
            }
            invoiceViewModels.add(new InvoiceViewModel(invoiceNumber,productTopName,productTopImage,status,date,total,productCountDisplay,invoiceType,description));
        }
        return invoiceViewModels;
    }
}
