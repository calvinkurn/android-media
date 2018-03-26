package com.tokopedia.inbox.attachinvoice.data.repository;

import com.tokopedia.inbox.attachinvoice.data.mapper.TkpdResponseToInvoicesDataModelMapper;
import com.tokopedia.inbox.attachinvoice.data.source.service.GetTxInvoicesService;
import com.tokopedia.inbox.attachinvoice.domain.model.Invoice;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by Hendri on 21/03/18.
 */

public class AttachInvoicesRepositoryImpl implements AttachInvoicesRepository {
    private final GetTxInvoicesService service;
    private final TkpdResponseToInvoicesDataModelMapper mapper;

    public AttachInvoicesRepositoryImpl(GetTxInvoicesService service, TkpdResponseToInvoicesDataModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<Invoice>> getUserInvoices(Map<String, String> params) {
        return service.getApi().getTXOrderList(params).map(mapper);
    }


    /*
    *  private final GetShopProductService shopService;
    private final TkpdResponseToAttachProductDomainModelMapper mapper;
    public AttachProductRepositoryImpl(GetShopProductService shopService, TkpdResponseToAttachProductDomainModelMapper mapper) {
        this.shopService = shopService;
        this.mapper = mapper;
    }

    @Override
    public Observable<AttachProductDomainModel> loadProductFromShop(Map<String,String> params) {
        return shopService.getApi().getShopProduct(params).map(mapper);
    }
    * */
}
