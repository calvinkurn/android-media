package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.seller.opportunity.data.source.api.WsProductApi;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nakama on 30/03/18.
 */

public class CloudProductDataSource {
    private WsProductApi productApi;
    private static final String PARAM_DEVICE_ID = "device";
    private static final String VALUE_DEVICE = "android";
    private Context context;

    public CloudProductDataSource(WsProductApi productApi, Context context) {
        this.context = context;
        this.productApi = productApi;
    }

    public Observable<ProductDetailData> getProductDetail(RequestParams params){
        params.putString(PARAM_DEVICE_ID, VALUE_DEVICE);
        params.putAll(AuthUtil.generateParamsNetwork(context));
        return productApi.getProductDetail(params.getParamsAllValueInString())
                .map(new Func1<Response<TkpdResponse>, ProductDetailData>() {
                    @Override
                    public ProductDetailData call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                return response.body().convertDataObj(ProductDetailData.class);
                            } else {
                                throw new ErrorMessageException(response.body().getErrorMessageJoined());
                            }
                        } else {
                            throw new RuntimeException(String.valueOf(response.code()));
                        }
                    }
                });
    }
}
