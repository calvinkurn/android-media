package com.tokopedia.inbox.attachinvoice.data.mapper;

import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityData;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OpportunityList;
import com.tokopedia.core.network.entity.replacement.opportunitydata.OrderProduct;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
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

public class TkpdResponseToInvoicesDataModelMapper implements Func1<Response<TkpdResponse>, List<Invoice>> {
    @Override
    public List<Invoice> call(Response<TkpdResponse> tkpdResponseResponse) {
        List<Invoice> domainModel = new ArrayList<>();
        TkpdResponse tkpdResponse = tkpdResponseResponse.body();
        OpportunityData attachProductAPIResponseWrapper = tkpdResponse.convertDataObj(OpportunityData.class);
        List<OpportunityList> opportunityList = attachProductAPIResponseWrapper.getOpportunityList();
        for(OpportunityList opportunity:opportunityList){
            List<Product> products = new ArrayList<>();
            for(OrderProduct orderProduct:opportunity.getOrderProducts()){
                products.add(new Product(orderProduct.getProductName(),orderProduct.getProductPrice(),orderProduct.getProductPicture()));
            }
            domainModel.add(new Invoice(opportunity.getOrderDetail().getDetailInvoice(),
                    "market",
                    opportunity.getOrderDetail().getDetailPdfUri(),
                    products,
                    opportunity.getOrderDetail().getDetailOrderDate(),
                    opportunity.getOrderLast().getLastBuyerStatus(),
                    opportunity.getOrderDetail().getDetailOpenAmountIdr()));
        }

        return domainModel;
    }
}
