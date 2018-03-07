package com.tokopedia.inbox.attachproduct.data.repository;

import android.util.Log;

import com.tokopedia.core.network.apiservices.ace.AceSearchService;

import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.facades.authservices.ShopApi;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.shopinfo.models.productmodel.Product;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProduct;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductResult;
import com.tokopedia.core.util.getproducturlutil.model.GetProductPass;
import com.tokopedia.inbox.attachproduct.data.model.AttachProductAPIResponseWrapper;
import com.tokopedia.inbox.attachproduct.data.source.api.TomeGetShopProductAPI;
import com.tokopedia.inbox.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductRepositoryImpl implements AttachProductRepository {
    private final GetShopProductService shopService;
    public AttachProductRepositoryImpl(GetShopProductService shopService) {
        this.shopService = shopService;
    }

    Map<String, String> getParamProductUrl(String query, String shopId, int page) {
        if(page == 0)
            page = 1;
        HashMap<String,String> param = new HashMap<>();
        param.put("keyword",query);
        param.put("shop_id",shopId);
        param.put("page",String.valueOf(page));
        return param;
    }

    @Override
    public Observable<AttachProductDomainModel> loadProductFromShop(String query, String shopId, int page) {
        Map<String,String> params = getParamProductUrl(query,shopId,page);
        return shopService.getApi().getShopProduct(params).map(new Func1<Response<TkpdResponse>, AttachProductDomainModel>() {
            @Override
            public AttachProductDomainModel call(Response<TkpdResponse> tkpdResponseResponse) {
                AttachProductDomainModel domainModel = new AttachProductDomainModel();
                TkpdResponse tkpdResponse = tkpdResponseResponse.body();
                AttachProductAPIResponseWrapper attachProductAPIResponseWrapper = tkpdResponse.convertDataObj(AttachProductAPIResponseWrapper.class);
                domainModel.setProducts(attachProductAPIResponseWrapper.getProducts());
                return domainModel;
            }
        });
    }

}
