package com.tokopedia.shop.sort.data.source.cloud;

import android.support.v4.util.ArrayMap;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.sort.data.source.cloud.api.ShopAceApi;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSortList;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductSortCloudDataSource {

    private static final String DEVICE = "device";
    private static final String SOURCE = "source";
    private static final String DEFAULT_DEVICE = "android";
    private static final String SHOP_PRODUCT = "shop_product";

    private final ShopAceApi shopAceApi;

    @Inject
    public ShopProductSortCloudDataSource(ShopAceApi shopAceApi) {
        this.shopAceApi = shopAceApi;
    }

    public Observable<List<ShopProductSort>> getDynamicFilter() {
        return shopAceApi.getDynamicFilter(paramSortProduct()).map(new Func1<Response<DataResponse<ShopProductSortList>>, List<ShopProductSort>>() {
            @Override
            public List<ShopProductSort> call(Response<DataResponse<ShopProductSortList>> dataResponseResponse) {
                return dataResponseResponse.body().getData().getSort();
            }
        });
    }

    private static Map<String, String> paramSortProduct() {
        Map<String, String> params = new ArrayMap<>();
        params.put(DEVICE, DEFAULT_DEVICE);
        params.put(SOURCE, SHOP_PRODUCT);
        return params;
    }

}
