package com.tokopedia.shop.product.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.product.data.source.cloud.api.ShopOfficialStoreApi;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductCloudDataSource {

    private final ShopApi shopApi;
    private final ShopOfficialStoreApi shopOfficalStoreApi;

    @Inject
    public ShopProductCloudDataSource(ShopApi shopApi, ShopOfficialStoreApi shopOfficialStoreApi) {
        this.shopApi = shopApi;
        this.shopOfficalStoreApi = shopOfficialStoreApi;
    }

    public Observable<Response<DataResponse<PagingList<ShopProduct>>>> getShopProductList(ShopProductRequestModel shopProductRequestModel) {
        String baseUrl = ShopUrl.BASE_ACE_URL;
        if (shopProductRequestModel.isShopClosed()) {
            baseUrl = ShopCommonUrl.BASE_URL;
        }
        return shopApi.getShopProductList(baseUrl + ShopUrl.SHOP_PRODUCT_PATH, shopProductRequestModel.getHashMap());
    }

    public Observable<List<ShopProductCampaign>> getProductCampaigns(String idList) {
        return shopOfficalStoreApi.getProductCampaigns(idList).map(new Func1<Response<DataResponse<List<ShopProductCampaign>>>, List<ShopProductCampaign>>() {
            @Override
            public List<ShopProductCampaign> call(Response<DataResponse<List<ShopProductCampaign>>> response) {
                return response.body().getData();
            }
        });
    }
}
