package com.tokopedia.inbox.attachinvoice.data.mapper;

import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityData;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityList;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderProduct;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicesPayloadWrapper;
import com.tokopedia.inbox.attachinvoice.data.model.GetInvoicesResponseWrapper;
import com.tokopedia.inbox.attachinvoice.data.model.InvoiceAttributesDataModel;
import com.tokopedia.inbox.attachinvoice.data.model.InvoicesDataModel;
import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;
import com.tokopedia.inbox.attachinvoice.domain.model.Product;
import com.tokopedia.inbox.attachproduct.data.model.AttachProductAPIResponseWrapper;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;
import com.tokopedia.inbox.rescenter.detailv2.data.pojo.DetailResCenterOrder;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2.OrderData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 21/03/18.
 */

public class TkpdResponseToInvoicesDataModelMapper implements Func1<Response<GetInvoicesResponseWrapper>, List<Invoice>> {
    @Override
    public List<Invoice> call(Response<GetInvoicesResponseWrapper> getInvoicesResponseWrapperResponse) {
        GetInvoicesResponseWrapper responseWrapper = getInvoicesResponseWrapperResponse.body();
        List<Invoice> domainModel = new ArrayList<>();
        if(responseWrapper.getDataWrapper() != null && responseWrapper.getDataWrapper().getPayloadWrapper() != null){
            GetInvoicesPayloadWrapper payloadWrapper = responseWrapper.getDataWrapper().getPayloadWrapper();
            if(payloadWrapper.getInvoices() != null) {
                for (InvoicesDataModel invoicesDataModel : payloadWrapper.getInvoices()) {
                    InvoiceAttributesDataModel invoiceAttributesDataModel = invoicesDataModel.getAttribute();
                    domainModel.add(new Invoice(invoiceAttributesDataModel.getStatusId(),
                            invoiceAttributesDataModel.getInvoiceNo(),
                            invoicesDataModel.getType(),
                            invoiceAttributesDataModel.getUrl(),
                            invoiceAttributesDataModel.getTitle(),
                            invoiceAttributesDataModel.getDescription(),
                            invoiceAttributesDataModel.getInvoiceDate(),
                            invoiceAttributesDataModel.getStatus(),
                            invoiceAttributesDataModel.getAmount(),
                            invoiceAttributesDataModel.getImageUrl(),
                            invoicesDataModel.getTypeId(),
                            invoiceAttributesDataModel.getInvoiceId()));
                }
            }
        }
        return domainModel;
    }
}
