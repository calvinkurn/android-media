package com.tokopedia.seller.product.manage.data.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.product.manage.data.model.ResponseDeleteProductData;
import com.tokopedia.seller.product.manage.data.model.ResponseEditPriceData;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditPrice;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ZulfikarRahman on 20/9/17.
 */

public interface ProductActionApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.V4_ACTION_PRODUCT + TkpdBaseURL.Product.PATH_DELETE_PRODUCT)
    Observable<Response<DataResponse<ResponseDeleteProductData>>> delete(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.V4_ACTION_PRODUCT + TkpdBaseURL.Product.PATH_EDIT_PRICE)
    Observable<Response<DataResponse<ResponseEditPriceData>>> editPrice(@FieldMap Map<String, String> params);
}