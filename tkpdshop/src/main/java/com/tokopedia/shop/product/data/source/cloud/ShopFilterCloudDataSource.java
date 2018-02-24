package com.tokopedia.shop.product.data.source.cloud;

import android.support.v4.util.ArrayMap;

import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopFilterCloudDataSource {

    public static final String DEVICE = "device";
    public static final String SOURCE = "source";
    public static final String DEFAULT_DEVICE = "android";
    public static final String SHOP_PRODUCT = "shop_product";

    private ShopApi shopApi;

    @Inject
    public ShopFilterCloudDataSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DynamicFilterModel>> getDynamicFilter() {
        return shopApi.getDynamicFilter(ShopUrl.BASE_ACE_URL+"/"+ShopUrl.SHOP_DYNAMIC_FILTER, paramSortProduct());
    }

    public static Map<String, String> paramSortProduct() {
        Map<String, String> params = new ArrayMap<>();
        params.put(DEVICE, DEFAULT_DEVICE);
        params.put(SOURCE, SHOP_PRODUCT);
        return params;
    }

}
