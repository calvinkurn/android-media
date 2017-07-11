package com.tokopedia.seller.goldmerchant.statistic.data.source;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatCloud;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class GMStatDataSource {

    private GMStatCloud gmStatCloud;
    @Inject
    public GMStatDataSource(
            GMStatCloud gmStatCloud) {
        this.gmStatCloud = gmStatCloud;
    }

    public Observable<GetProductGraph> getProductGraph(long startDate, long endDate) {
        return gmStatCloud.getProductGraph(startDate, endDate).map(new Func1<Response<GetProductGraph>, GetProductGraph>() {
            @Override
            public GetProductGraph call(Response<GetProductGraph> getProductGraphResponse) {
                if (getProductGraphResponse.isSuccessful() && getProductGraphResponse.body() != null) {
                    return getProductGraphResponse.body();
                } else {
                    return null;
                }
            }
        });
    }

    public Observable<GetPopularProduct> getPopularProduct(long startDate, long endDate) {
        return gmStatCloud.getPopularProduct(startDate, endDate).map(new Func1<Response<GetPopularProduct>, GetPopularProduct>() {
            @Override
            public GetPopularProduct call(Response<GetPopularProduct> getPopularProductResponse) {
                if (getPopularProductResponse.isSuccessful() && getPopularProductResponse.body() != null) {
                    return getPopularProductResponse.body();
                } else {
                    return null;
                }
            }
        });
    }

    public Observable<GetBuyerData> getBuyerGraph(long startDate, long endDate) {
        return gmStatCloud.getBuyerGraph(startDate, endDate).map(new Func1<Response<GetBuyerData>, GetBuyerData>() {
            @Override
            public GetBuyerData call(Response<GetBuyerData> getBuyerDataResponse) {
                if (getBuyerDataResponse.isSuccessful() && getBuyerDataResponse.body() != null) {
                    return getBuyerDataResponse.body();
                } else {
                    return null;
                }
            }
        });
    }

    public Observable<GetKeyword> getKeywordModel(String categoryId) {
        return gmStatCloud.getKeywordModel(categoryId).map(new Func1<Response<GetKeyword>, GetKeyword>() {
            @Override
            public GetKeyword call(Response<GetKeyword> getKeywordResponse) {
                if (getKeywordResponse.isSuccessful() && getKeywordResponse.body() != null) {
                    return getKeywordResponse.body();
                } else {
                    return null;
                }
            }
        });
    }

    public Observable<GetShopCategory> getShopCategory(long startDate, long endDate) {
        return gmStatCloud.getShopCategory(startDate, endDate).map(new Func1<Response<GetShopCategory>, GetShopCategory>() {
            @Override
            public GetShopCategory call(Response<GetShopCategory> getShopCategoryResponse) {
                if (getShopCategoryResponse.isSuccessful() && getShopCategoryResponse.body() != null) {
                    return getShopCategoryResponse.body();
                } else {
                    return null;
                }
            }
        });
    }
}
