package com.tokopedia.topads.dashboard.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.mapper.SearchProductEOFMapper;
import com.tokopedia.topads.dashboard.data.model.data.Product;
import com.tokopedia.topads.dashboard.data.source.TopAdsSearchProductDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.domain.model.ProductDomain;
import com.tokopedia.topads.dashboard.domain.model.ProductListDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Statement;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * @author normansyahputa on 2/17/17.
 */

public class CloudTopAdsSearchProductDataSource implements TopAdsSearchProductDataSource {

    private Context context;
    private TopAdsManagementService topAdsSearchProductService;
    private SearchProductEOFMapper searchProductMapper;
    private int page;
    private Map<String, String> param;
    private int rows;
    private boolean hitNext = false;
    private int sourcePage;
    private List<ProductDomain> tempProducts;

    @Inject
    public CloudTopAdsSearchProductDataSource(@ApplicationContext Context context,
                                              TopAdsManagementService topAdsSearchProductService,
                                              SearchProductEOFMapper searchProductMapper) {
        this.context = context;
        this.topAdsSearchProductService = topAdsSearchProductService;
        this.searchProductMapper = searchProductMapper;

    }

    @Override
    public Observable<ProductListDomain> searchProduct(Map<String, String> param) {
        // get initial value
        rows = Integer.parseInt(param.get(TopAdsNetworkConstant.PARAM_ROWS));
        page = Integer.parseInt(param.get(TopAdsNetworkConstant.PARAM_START)) / rows;
        sourcePage = page;
        this.param = param;
        tempProducts = new ArrayList<>();
        // get initial value
        return Statement.doWhile(topAdsSearchProductService
                .getApi()
                .searchProductAd(this.param)
                .doOnNext(new Action1<Response<DataResponse<List<Product>>>>() {
                    @Override
                    public void call(Response<DataResponse<List<Product>>> dataResponseResponse) {
                        List<Product> data = dataResponseResponse.body().getData();
                        boolean eof = dataResponseResponse.body().isEof();

                        hitNext = !eof && data.size() < rows;

                        if (hitNext) {
                            page++;
                            tempProducts.addAll(SearchProductEOFMapper.getProductDomains(data));
                            CloudTopAdsSearchProductDataSource.this.param
                                    .put(TopAdsNetworkConstant.PARAM_START, (page * rows) + "");
                        }
                    }
                }), new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return hitNext;
            }
        }).map(searchProductMapper)
                .map(new Func1<ProductListDomain, ProductListDomain>() {
                    @Override
                    public ProductListDomain call(ProductListDomain productListDomain) {
                        if (page > sourcePage) {
                            productListDomain.setProductDomains(tempProducts);
                            productListDomain.setPage(page);
                        }
                        return productListDomain;
                    }
                });
    }
}
