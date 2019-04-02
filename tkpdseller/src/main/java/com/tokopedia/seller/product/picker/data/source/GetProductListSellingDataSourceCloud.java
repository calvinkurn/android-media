package com.tokopedia.seller.product.picker.data.source;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.picker.data.api.GetProductListSellerApi;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public class GetProductListSellingDataSourceCloud {

    private final GetProductListSellerApi getProductListSellerApi;
    private final Context context;

    @Inject
    public GetProductListSellingDataSourceCloud(GetProductListSellerApi getProductListSellerApi, @ApplicationContext Context context) {
        this.getProductListSellerApi = getProductListSellerApi;
        this.context = context;
    }


    public Observable<Response<ProductListSellerModel>> getProductListSeller(TKPDMapParam<String, String> parameters) {
        return getProductListSellerApi.getProductList(AuthUtil.generateParamsNetwork(context, parameters));
    }
}
