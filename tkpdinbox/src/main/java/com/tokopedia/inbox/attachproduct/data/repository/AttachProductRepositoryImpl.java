package com.tokopedia.inbox.attachproduct.data.repository;

import com.tokopedia.inbox.attachproduct.data.model.mapper.TkpdResponseToAttachProductDomainModelMapper;
import com.tokopedia.inbox.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductRepositoryImpl implements AttachProductRepository {
    private static final String KEYWORD_KEY = "keyword";
    private static final String SHOP_ID_KEY = "shop_id";
    private static final String PAGE_KEY = "page";

    private final GetShopProductService shopService;
    private final TkpdResponseToAttachProductDomainModelMapper mapper;
    public AttachProductRepositoryImpl(GetShopProductService shopService, TkpdResponseToAttachProductDomainModelMapper mapper) {
        this.shopService = shopService;
        this.mapper = mapper;
    }

    Map<String, String> getParamProductUrl(String query, String shopId, int page) {
        if(page == 0) page = 1;
        HashMap<String,String> param = new HashMap<>();
        param.put(KEYWORD_KEY,query);
        param.put(SHOP_ID_KEY,shopId);
        param.put(PAGE_KEY,String.valueOf(page));
        return param;
    }

    @Override
    public Observable<AttachProductDomainModel> loadProductFromShop(String query, String shopId, int page) {
        Map<String,String> params = getParamProductUrl(query,shopId,page);
        return shopService.getApi().getShopProduct(params).map(mapper);
    }

}
